package command;

import exception.ExceptionController;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class LoginCommand implements Command {
    private final MemberService memberService;
    private final ExceptionController exceptionController;
    private final Scanner scanner;
    private Member member;

    public LoginCommand(MemberService memberService, ExceptionController exceptionController, Scanner scanner) {
        this.memberService = memberService;
        this.exceptionController = exceptionController;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        executeReturnBoolean();
    }

    @Override
    public boolean executeReturnBoolean() {
        System.out.print("ID : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        try {
            member = memberService.login(id, password);
            System.out.println(member.getName() + "님 로그인 되셨습니다.");
            return true;
        } catch (Exception e) {
            exceptionController.loginError(e);
            return false;
        }
    }
}
