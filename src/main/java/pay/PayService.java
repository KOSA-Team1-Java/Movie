package pay;

import exception.MovieException;
import member.Member;
import movie.Movie;
import movie.SeatRequest;

import java.util.List;

public class PayService {

    public void pay(Member member, Movie movie, List<SeatRequest> seatList) {
        int totalPrice = movie.getPrice() * seatList.size(); // 총 결제 금액

        System.out.println("💳 현재 잔액: 현금 " + member.getCash() + "원, 카드 " + member.getCredit() +"원");
        System.out.println("🎟️ 총 결제 금액: " + totalPrice + "원");

        if (member.getCash() + member.getCredit() < totalPrice) {
            System.out.println("❌ 예산이 부족하여 예매할 수 없습니다.");
            throw new MovieException("잔액이 부족합니다.");
        }
    }
}
