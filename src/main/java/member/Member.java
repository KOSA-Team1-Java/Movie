package member;

import lombok.Getter;

@Getter
public class Member {

    private String loginId;
    private String password;
    private String name;
    private int age;

    public Member(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
    }

}
