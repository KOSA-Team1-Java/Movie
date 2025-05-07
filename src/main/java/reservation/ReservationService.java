package reservation;

import member.Member;
import movie.Screening;
import movie.SeatRequest;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static util.ConnectionConst.*;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public void save(Member member, Screening screening, List<SeatRequest> seatList, int cash, int credit) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1. 예약 insert (결제 금액 포함)
            int reservationId = reservationRepository.insertReservation(
                    conn,
                    member.getLoginId(),
                    screening.getId(),
                    cash,
                    credit
            );

            // 2. 좌석 insert
            for (SeatRequest seat : seatList) {
                reservationRepository.ReservationinsertSeat(conn, reservationId, seat.getRow(), seat.getCol());
            }

            conn.commit();
            System.out.println("✅ 예매가 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ 예매 저장 실패: 롤백합니다.");
        }
    }


    private int extractReservationIdFromString(String reservationInfo) {
        // 예매 정보에서 예약 ID 추출 (예매 1번째 등)
        // 예: "예매 1번째"에서 1을 추출하는 로직을 구현
        String[] parts = reservationInfo.split("\n");
        String reservationInfoLine = parts[0];  // "예매 1번째"
        // "예매 1번째"에서 숫자 1을 추출
        String reservationIdStr = reservationInfoLine.replaceAll("[^0-9]", ""); // 숫자만 남기기
        try {
            return Integer.parseInt(reservationIdStr); // 추출한 숫자를 정수로 변환
        } catch (NumberFormatException e) {
            System.err.println("예약 ID를 추출할 수 없습니다.");
            return -1; // 예외가 발생하면 -1 반환
        }
    }


    public int getReservationIdByIndex(Member member, int index) {
        return reservationRepository.findReservationIdByIndex(member.getLoginId(), index);
    }

    public void viewReservations(Member member) {
        List<ReservationDto> reservations = reservationRepository.findReservationsByMember(member);

        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        for (ReservationDto reservation : reservations) {
            System.out.println("예약 ID : " + reservation.getReservation().getId());
            // 영화 제목
            System.out.println(reservation.getMovie().getTitle());

            // 날짜, 시작~종료 시간
            System.out.println(
                    reservation.getScreening().getScreeningDate() + " " +
                            reservation.getScreening().getStartTime() + " ~ " +
                            reservation.getScreening().getEndTime()
            );

            // 극장 위치
            System.out.println(reservation.getTheater().getLocation());

            // 좌석 정보 출력: 2명 A1, A2 형식
            int seatCount = reservation.getSeats().size();
            String seatNames = reservation.getSeats().stream()
                    .map(seat -> seat.getSeatRow() + String.valueOf(seat.getSeatCol()))
                    .collect(Collectors.joining(", "));
            System.out.println(seatCount + "명 " + seatNames);

            // 결제 내역
            System.out.println("결제 내역");
            System.out.println("현금: " + reservation.getReservation().getCash()
                    + ", 카드: " + reservation.getReservation().getCredit());

            System.out.println("-------------------------------------");
        }
    }


    public boolean cancelReservation(int reservationId, String loginId) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1. 환불 금액 조회
            String sql = "SELECT cash, credit FROM reservation WHERE id = ? AND member_loginId = ?";
            int refundCash = 0, refundCredit = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
                pstmt.setString(2, loginId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        refundCash = rs.getInt("cash");
                        refundCredit = rs.getInt("credit");
                    }
                }
            }

            // 2. 회원 환불 처리
            String refundSql = "UPDATE member SET cash = cash + ?, credit = credit + ? WHERE loginId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(refundSql)) {
                pstmt.setInt(1, refundCash);
                pstmt.setInt(2, refundCredit);
                pstmt.setString(3, loginId);
                pstmt.executeUpdate();
            }

            // 3. 좌석 삭제
            reservationRepository.deleteSeatsByReservationId(conn, reservationId);

            // 4. 예매 삭제
            reservationRepository.deleteReservationById(conn, reservationId);

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}