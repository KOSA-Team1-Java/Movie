package pay;

import member.Member;

public class CashPay implements Pay {

    private Member member;
    private int amount;

    public CashPay(Member member, int amount) {
        this.member = member;
        this.amount = amount;
    }

    @Override
    public int pay() {
        if (member.getCash() >= amount) {
            member.decreaseCash(amount);
            System.out.println("💵 현금 결제가 완료되었습니다.");
            return member.getCash();
        }
        return 0;
    }
}
