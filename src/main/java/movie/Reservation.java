package movie;

import lombok.Getter;
import member.Member;

@Getter
public class Reservation {
    private int id;
    private Screening screening;
    private int peopleCount;
    private Member member;
    private int totalPrice;  // 총 결제 금액

    // 생성자
    public Reservation(int id, Screening screening, int peopleCount, Member member) {
        this.id = id;
        this.screening = screening;
        this.peopleCount = peopleCount;
        this.member = member;
        this.totalPrice = calculateTotalPrice();  // 가격 계산
    }

    // 총 결제 금액 계산 (영화 가격 * 인원 수)
    private int calculateTotalPrice() {
        return screening.getMovie().getPrice() * peopleCount;
    }

    // 예산 차감 처리 메서드
    public void applyDiscount() {
        member.decreaseBudget(totalPrice);
    }
}
