package command;

import controller.MainController;
import exception.ExceptionController;
import member.MemberService;

import java.util.Scanner;

public class SignUpCommand implements Command {

    private final MemberService memberService;
    private final ExceptionController exceptionController;
    private final Scanner scanner;

    public SignUpCommand(MemberService memberService, ExceptionController exceptionController, Scanner scanner) {
        this.memberService = memberService;
        this.exceptionController = exceptionController;
        this.scanner = scanner;
    }

    @Override
    public boolean execute(MainController controller) {
        System.out.print("ID : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        System.out.print("Name : ");
        String name = scanner.nextLine();
        System.out.print("Age : ");
        int age = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        try {
            memberService.signUp(id, password, name, age);
            System.out.println("회원가입이 완료되었습니다.");
            return true;
        } catch (Exception e) {
            exceptionController.signUpError(e);
            return false;
        }
    }

    @Override
    public boolean requiresLogout() {
        return true;
    }
}
