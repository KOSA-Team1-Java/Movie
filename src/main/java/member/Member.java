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
    private int budget = 100000;

    private List<Integer> paymentHistory = new ArrayList<>();

    public Member(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public void addPaymentHistory(int amount) {
        this.paymentHistory.add(amount);
    }

    public void decreaseBudget(int price) {
        this.budget = budget;
    }

}
