package pay;

import member.Member;

public class CreditPay implements Pay {

    private Member member;
    private int amount;

    public CreditPay(Member member, int amount) {
        this.member = member;
        this.amount = amount;
    }

    @Override
    public int pay() {
        if (member.getCredit() >= amount) {
            member.decreaseCredit(amount);
            System.out.println("💳 카드 결제가 완료되었습니다.");
            return member.getCredit();
        }
        return 0;
    }
}
