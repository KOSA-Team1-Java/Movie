package command;

import controller.MainController;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class ChangePasswordCommand implements Command, RequiredMember {

    private final MemberService memberService;
    private Scanner scanner;
    private Member member;

    public ChangePasswordCommand(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController context) {
        if (member == null) {
            System.out.println("로그인 후에 비밀번호를 변경할 수 있습니다.");
            return;
        }

        System.out.print("새 비밀번호를 입력하세요: ");
        String newPassword = scanner.nextLine();

        // 비밀번호 변경 처리
        member = memberService.updatePassword(member, newPassword); // DB 반영 및 객체 갱신
        System.out.println("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Override
    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }
}
