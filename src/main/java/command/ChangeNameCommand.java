package command;

import controller.MainController;
import member.Member;
import member.MemberService;

import java.util.Scanner;

public class ChangeNameCommand implements Command, RequiredMember {

    private Member member;
    private final MemberService memberService;

    public ChangeNameCommand(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void execute(MainController context) {
        if (member == null) {
            System.out.println("로그인 후에 이름을 변경할 수 있습니다.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("새 이름을 입력하세요: ");
        String newName = scanner.nextLine();

        member = memberService.updateName(member, newName); // DB 반영 및 객체 갱신
        System.out.println("이름이 성공적으로 변경되었습니다.");
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }
}
