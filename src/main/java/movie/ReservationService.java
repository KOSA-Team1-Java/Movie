package movie;

import member.Member;
import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepository = new ReservationRepository();

    public boolean reserveMovie(Member member, Movie movie, int screeningId, List<SeatRequest> seatList) {
        int totalPrice = movie.getPrice() * seatList.size(); // 총 결제 금액

        System.out.println("💳 현재 잔액: " + member.getBudget() + "원");
        System.out.println("🎟️ 총 결제 금액: " + totalPrice + "원");

        if (member.getBudget() < totalPrice) {
            System.out.println("❌ 예산이 부족하여 예매할 수 없습니다.");
            return false;
        }

        int reservationId = reservationRepository.insertReservation(member.getLoginId(), screeningId);
        if (reservationId == -1) {
            System.out.println("❌ 예매 처리에 실패하였습니다.");
            return false;
        }

        for (SeatRequest seat : seatList) {
            reservationRepository.insertSeat(reservationId, seat.getRow(), seat.getCol());
        }

        member.decreaseBudget(totalPrice);
        System.out.println("✅ 예매 및 결제 완료! 남은 예산: " + (member.getBudget() - totalPrice) + "원");
        return true;
    }
}
