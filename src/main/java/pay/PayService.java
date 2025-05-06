package pay;

import exception.MovieException;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class PayService {

    private MemberService memberService;

    public PayService(MemberService memberService) {
        this.memberService = memberService;
    }

    public void pay(Member member, int totalPrice, Scanner scanner) {
        System.out.println("💳 현재 잔액: 현금 " + member.getCash() + "원, 카드 " + member.getCredit() +"원");
        System.out.println("🎟️ 총 결제 금액: " + totalPrice + "원");

        if (member.getCash() + member.getCredit() < totalPrice) {
            throw new MovieException("❌ 예산이 부족하여 예매할 수 없습니다.");
        }

        System.out.println("결제 수단을 선택하세요 (1.현금 / 2.카드)");
        System.out.print("입력 : ");
        int payMethod = scanner.nextInt();
        scanner.nextLine();

        if (payMethod == 1) {
            Pay cashPay;
            if (member.getCash() < totalPrice) {
                int cashAmount = member.getCash();
                int creditAmount = totalPrice - cashAmount;
                cashPay = new CashPay(member, cashAmount);
                Pay creditPay = new CreditPay(member, creditAmount);
                cashPay.pay();
                creditPay.pay();
            } else {
                cashPay = new CashPay(member, totalPrice);
                cashPay.pay();
            }
        }
        else if (payMethod == 2) {
            Pay creditPay;
            if (member.getCredit() < totalPrice) {
                int creditAmount = member.getCredit();
                int cashAmount = totalPrice - creditAmount;
                creditPay = new CreditPay(member, creditAmount);
                Pay cashPay = new CashPay(member, cashAmount);
                creditPay.pay();
                cashPay.pay();
            } else {
                creditPay = new CreditPay(member, totalPrice);
                creditPay.pay();
            }
        }

        memberService.updateBudget(member);

        System.out.println("✅ 결제 완료!");
        System.out.println("💳 남은 예산 : 현금 " + member.getCash() + "원, 카드 " + member.getCredit() +"원");
    }
}
