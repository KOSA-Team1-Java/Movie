package movie;

import member.Member;
<<<<<<< HEAD
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
=======
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

>>>>>>> 998ccc0 (Reservation)
