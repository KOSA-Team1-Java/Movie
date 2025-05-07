package reservation;

import member.Member;
import movie.Screening;
import movie.SeatRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public void viewReservations(Member member) {
        List<ReservationDto> reservations = reservationRepository.findReservationsByMember(member);

        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        for (ReservationDto reservation : reservations) {
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

    public void cancelReservation(int reservationId) {
        // 1. 좌석정보(ReservationSeat) 삭제
        reservationRepository.deleteReservationSeatsByReservationId(reservationId);
        // 2. 예약정보(Reservation) 삭제
        reservationRepository.deleteReservationById(reservationId);
    }
}