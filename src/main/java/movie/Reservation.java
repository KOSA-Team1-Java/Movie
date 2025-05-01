package movie;

import member.Member;

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

    // Getter 메서드들
    public int getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public Member getMember() {
        return member;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    // 예산 차감 처리 메서드
    public void applyDiscount() {
        member.decreaseBudget(totalPrice);
    }
}
