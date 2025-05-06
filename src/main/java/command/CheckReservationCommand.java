package command;

import controller.MainController;
import member.Member;

public class CheckReservationCommand implements Command, RequiredMember {

    private Member member;

    @Override
    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void execute(MainController context) {

    }

    @Override
    public boolean requiresLogin() {
        return true;
    }
}
