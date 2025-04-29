package controller;

import member.MemberService;

import java.util.Scanner;

public class MainController {

    private static MemberService memberService = new MemberService();
    public void call(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "/login":
                System.out.print("ID : ");
                String id = scanner.nextLine();
                System.out.print("Password : ");
                String password = scanner.nextLine();
                memberService.login(id, password);
        }
    }
}
