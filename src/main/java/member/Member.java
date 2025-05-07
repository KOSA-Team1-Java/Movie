package member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
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
        this.cash -= amount;
    }

    public void decreaseCredit(int amount) {
        this.credit -= amount;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
