package command;

import controller.MainController;
import exception.CustomException;
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
    public void execute(MainController context) {
        System.out.print("ID : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        try {
            member = memberService.login(id, password);
            context.setLoginMember(member);
            System.out.println(member.getName() + "님 로그인 되었습니다.");
        } catch (Exception e) {
            throw new CustomException("로그인 실패",e);
        }
    }

    @Override
    public boolean requiresLogout() {
        return true;
    }
}
