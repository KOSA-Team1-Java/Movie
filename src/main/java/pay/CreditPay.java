package pay;

public class CreditPay implements Pay {
    @Override
    public int pay(int price) {
        System.out.println("💳 신용카드 결제 진행: 12000 원");
        return 0; // 성공 시 0 반환
    }
}