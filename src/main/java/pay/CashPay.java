package pay;

public class CashPay implements Pay {
    @Override
    public int pay(int price) {
        System.out.println("현금 결제 진행: " + price + "원");
        return 0; // 성공 시 0 반환
    }
}