package reservation;

import member.Member;
import movie.SeatRequest;

import java.sql.*;
import java.util.List;

import static util.ConnectionConst.*;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 예매 내역 번호별 문자열 포맷으로 조회 (번호 선택 UI에 적합)
    public List<String> getFormattedReservationsByMember(Member member) {
        return reservationRepository.getFormattedReservationsByMember(member);
    }

    // 예매 내역 DTO 반환 (상세 조회/기타 활용 가능)
    public void viewReservations(Member member) {
        List<String> reservations = reservationRepository.getFormattedReservationsByMember(member);
        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }
        System.out.println("====== 나의 예매 내역 ======");
        for (String line : reservations) {
            System.out.println(line);
            System.out.println("---------------------------");
        }
    }

    // 인덱스로 예약 id 얻기 (번호 선택 후 내부적으로 reservationId로 변환)
    public int getReservationIdByIndex(Member member, int index) {
        return reservationRepository.findReservationIdByIndex(member.getLoginId(), index);
    }

    // 예약 저장 (예매)
    public void save(Member member, int screeningId, List<SeatRequest> seatList, int cash, int credit) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1. 예약 insert (결제 금액 포함)
            int reservationId = reservationRepository.insertReservation(
                    conn, member.getLoginId(), screeningId, cash, credit
            );

            // 2. 좌석 정보 저장
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

    // 예약 취소 (환불 + 좌석반환 + 예매삭제)
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
