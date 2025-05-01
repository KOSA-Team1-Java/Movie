package pay;

import member.Member;

public class CashPay implements Pay {
    @Override
    public boolean pay(Member member, int amount) {
        if (member.getBudget() >= amount) {
            member.decreaseBudget(amount);
            System.out.println("💵 현금 결제가 완료되었습니다.");
            return true;
        }
        return false;
    }
}
