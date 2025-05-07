package reservation;

import member.Member;
import movie.Movie;
import movie.Screening;
import movie.SeatRequest;
import pay.CashPay;
import pay.CreditPay;
import pay.Pay;
import pay.PayService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static util.ConnectionConst.*;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<String> getReservationsByMember(Member member) {
        return reservationRepository.findReservationsByMemberLoginId(member.getLoginId());
    }


    public void save(Member member, Screening screening, List<SeatRequest> seatList, int cash, int credit) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. reservation 테이블에 저장
            int reservationId = reservationRepository.insertReservation(conn, member.getLoginId(), screening.getId(), cash, credit);

            // 2. 좌석 정보 저장 (reservation_seat 테이블)
            for (SeatRequest seat : seatList) {
                reservationRepository.ReservationinsertSeat(conn, reservationId, seat.getRow(), seat.getCol());
            }

            conn.commit(); // 모든 작업 성공 시 커밋
            System.out.println("✅ 예매 정보가 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ 예매 정보 저장 실패: 롤백됩니다.");
        }
    }
    public boolean cancelReservation(int reservationId) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // 예매와 좌석 정보 삭제
            reservationRepository.deleteSeatsByReservationId(conn, reservationId);
            reservationRepository.deleteReservationById(conn, reservationId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public int getReservationIdByIndex(int index) {
//        // 예약 내역을 가져와서 index에 맞는 예약 ID를 반환
//        List<String> reservations = reservationRepository.findReservationsByMemberLoginId(member.getLoginId());
//        if (index >= 0 && index < reservations.size()) {
//            String reservation = reservations.get(index);
//            // 예매 정보에서 예약 ID 추출 (예: "예매 1번째" -> 1)
//            return extractReservationIdFromString(reservation);
//        }
//        return -1;
//    }

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


    public void cancelReservation(int reservationId) {
        // 1. 좌석정보(ReservationSeat) 삭제
        reservationRepository.deleteReservationSeatsByReservationId(reservationId);
        // 2. 예약정보(Reservation) 삭제
        reservationRepository.deleteReservationById(reservationId);
    }
}