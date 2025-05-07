package command;

import controller.MainController;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class LoginCommand implements Command {
    private final MemberService memberService;
    private final Scanner scanner;
    private Member member;

    public LoginCommand(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController controller) {
        System.out.print("ID : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();

        member = memberService.login(id, password);
        controller.setLoginMember(member);
        System.out.println(member.getName() + "님 로그인 되었습니다.");
    }

    @Override
    public boolean requiresLogout() {
        return true;
    }
}
