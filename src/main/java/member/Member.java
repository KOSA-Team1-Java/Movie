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
    private int budget = 100000; // 기본 예산 100,000원

    private List<Integer> paymentHistory = new ArrayList<>();

    public Member(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    // 결제 이력 추가
    public void addPaymentHistory(int amount) {
        this.paymentHistory.add(amount);
    }

    public void decreaseBudget(int amount) {
        if (amount <= 0) {
            System.out.println("❌ 차감할 금액은 0보다 커야 합니다.");
            return;
        }

        if (this.budget < amount) {
            System.out.println("❌ 예산이 부족하여 차감할 수 없습니다.");
            return;
        }
        this.budget -= amount;
    }

    // 예산 업데이트 메서드 (DB 업데이트용)
    public void setBudget(int budget) {
        this.budget = budget;
    }
}
