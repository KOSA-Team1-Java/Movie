package command;

import controller.MainController;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class ChangeNameCommand implements Command, RequiredMember {

    private final MemberService memberService;
    private final Scanner scanner;
    private Member member;

    public ChangeNameCommand(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController context) {
        System.out.print("새 이름을 입력하세요 : ");
        String newName = scanner.nextLine();

        member = memberService.updateName(member, newName);
        System.out.println(member.getName() + "님 이름이 성공적으로 변경되었습니다.");
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
