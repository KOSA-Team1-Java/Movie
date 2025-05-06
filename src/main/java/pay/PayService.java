package pay;

import exception.MovieException;
import member.Member;

import java.util.Scanner;

public class PayService {

    public void pay(Member member, int totalPrice, Scanner scanner) {
        System.out.println("💳 현재 잔액: 현금 " + member.getCash() + "원, 카드 " + member.getCredit() +"원");
        System.out.println("🎟️ 총 결제 금액: " + totalPrice + "원");

        if (member.getCash() + member.getCredit() < totalPrice) {
            throw new MovieException("❌ 예산이 부족하여 예매할 수 없습니다.");
        }

        System.out.println("결제 수단을 선택하세요 (1.현금 / 2.카드");
        int payMethod = scanner.nextInt();
        scanner.nextLine();

        if (payMethod == 1) {
            CashPay cashPay;
            if (member.getCash() < totalPrice) {
                int cashAmount = member.getCash();
                int creditAmount = totalPrice - cashAmount;
                cashPay = new CashPay(member, cashAmount);
                CreditPay creditPay = new CreditPay(member, creditAmount);
                int cashBalance = cashPay.pay();
                int creditBalance = creditPay.pay();
            } else {
                cashPay = new CashPay(member, totalPrice);
                int cashBalance = cashPay.pay();
                int creditBalance = member.getCredit();
            }
        }
        else if (payMethod == 2) {
            CreditPay creditPay;
            if (member.getCredit() < totalPrice) {
                int creditAmount = member.getCredit();
                int cashAmount = totalPrice - creditAmount;
                creditPay = new CreditPay(member, creditAmount);
                CashPay cashPay = new CashPay(member, cashAmount);
                int creditBalance = creditPay.pay();
                int cashBalance = cashPay.pay();
            } else {
                creditPay = new CreditPay(member, totalPrice);
                int creditBalance = creditPay.pay();
                int cashBalance = member.getCash();
            }
        }
        System.out.println("✅ 결제 완료!");
        System.out.println("💳 남은 예산 : 현금 " + member.getCash() + "원, 카드 " + member.getCredit() +"원");
    }
}
