package movie;

import member.Member;
import java.util.List; // import 추가

public class ReservationService {
    private final ReservationRepository reservationRepository = new ReservationRepository(); // 인스턴스 생성

    public boolean reserveMovie(Member member, Movie movie, int screeningId, List<SeatRequest> seatList) {
        int price = movie.getPrice() * seatList.size();
        if (member.getBudget() < price) {
            System.out.println("예산이 부족하여 예매할 수 없습니다.");
            return false;
        }
        // 1. 예매 정보 등록
        int reservationId = reservationRepository.insertReservation(member.getLoginId(), screeningId);
        if (reservationId == -1) {
            System.out.println("예매 처리에 실패하였습니다.");
            return false;
        }
        // 2. 좌석 정보 등록
        for (SeatRequest seat : seatList) {
            reservationRepository.insertSeat(reservationId, seat.getRow(), seat.getCol());
        }
        // 3. 예산 차감
        member.decreaseBudget(price);
        System.out.println("예매가 완료되었습니다! 남은 예산: " + member.getBudget());
        return true;
    }
}

