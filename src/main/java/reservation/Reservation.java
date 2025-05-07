package reservation;

import lombok.Getter;
import member.Member;
import movie.Screening;

@Getter
public class Reservation {
    private int id;
    private Member member;
    private Screening screening;
    private int peopleCount;
    private int totalPrice;  //결제 금액
    private int cash;
    private int credit;

    // 생성자
    public Reservation(int id, Screening screening, int peopleCount, Member member, int cash, int credit) {
        this.id = id;
        this.screening = screening;
        this.peopleCount = peopleCount;
        this.member = member;
        this.cash = cash;
        this.credit = credit;
        this.totalPrice = calculateTotalPrice();  // 가격 계산
    }

    public int getCash() { return cash; }
    public int getCredit() { return credit; }

    // 총 결제 금액 계산 (영화 가격 * 인원 수)
    private int calculateTotalPrice() {
        return screening.getMovie().getPrice() * peopleCount;
    }

    // 예산 차감 처리 메서드
    public void applyDiscount() {
        member.decreaseCash(totalPrice);
    }
}
