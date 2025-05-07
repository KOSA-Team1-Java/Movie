package command;

import controller.MainController;
import member.MemberService;

import java.util.Scanner;

public class SignUpCommand implements Command {

    private final MemberService memberService;
    private final Scanner scanner;

    public SignUpCommand(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
    }

    @Override
    public  void execute(MainController context) {
        System.out.print("ID : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        System.out.print("Name : ");
        String name = scanner.nextLine();
        System.out.print("Age : ");
        int age = scanner.nextInt();
        scanner.nextLine();
        memberService.signUp(id, password, name, age);
    }

    @Override
    public boolean requiresLogout() {
        return true;
    }
}
