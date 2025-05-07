package reservation;

import member.Member;
import movie.Screening;
import movie.SeatRequest;

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


    public List<Integer> getReservationsByMember(String loginId) {
        return reservationRepository.findReservationIdsByMemberLoginId(loginId);
    }


    public void cancelReservation(int reservationId) {
        // 1. 좌석정보(ReservationSeat) 삭제
        reservationRepository.deleteReservationSeatsByReservationId(reservationId);
        // 2. 예약정보(Reservation) 삭제
        reservationRepository.deleteReservationById(reservationId);
    }
}