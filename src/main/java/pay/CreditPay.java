package pay;

public class CreditPay implements Pay {
    @Override
    public int pay(int price) {
        System.out.println("💳 신용카드 결제 진행: 12000 원");
        System.out.println("결제가 완료되었습니다.");
        // 실제 카드 승인 로직 구현
        return 0; // 성공 시 0 반환
    }
}