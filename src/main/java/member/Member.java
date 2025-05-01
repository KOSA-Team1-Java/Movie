package member;

import controller.MainController;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Member {

    private String loginId;
    private String password;
    private String name;
    private int age;
<<<<<<< HEAD
    private int budget =100000;
=======
    private int budget = 100000;
    public int peopleCount =0 ;
>>>>>>> 998ccc0 (Reservation)

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
<<<<<<< HEAD
        this.budget = budget;
    }

=======
        this.budget = budget - 15000 * peopleCount;
    }
>>>>>>> 998ccc0 (Reservation)
}
