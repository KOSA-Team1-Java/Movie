package member;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Member {

    private String loginId;
    private String password;
    private String name;
    private int age;
    private int cash; // 현금 예산 100,000원
    private int credit; // 카드 예산 100,000원

    private List<Integer> paymentHistory = new ArrayList<>();

    public Member(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.cash = 100000;
        this.credit = 100000;
    }

    public Member(String loginId, String password, String name, int age, int cash, int credit) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.cash = cash;
        this.credit = credit;
    }

    public void decreaseCash(int amount) {
        if (amount <= 0) {
            System.out.println("❌ 차감할 금액은 0보다 커야 합니다.");
            return;
        }
        if (this.cash < amount) {
            System.out.println("❌ 예산이 부족하여 차감할 수 없습니다.");
            return;
        }
        this.cash -= amount;
    }

    public void decreaseCredit(int amount) {
        if (amount <= 0) {
            System.out.println("❌ 차감할 금액은 0보다 커야 합니다.");
            return;
        }
        if (this.credit < amount) {
            System.out.println("❌ 예산이 부족하여 차감할 수 없습니다.");
            return;
        }
        this.credit -= amount;
    }
}
