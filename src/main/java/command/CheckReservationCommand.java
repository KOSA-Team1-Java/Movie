package command;

import controller.MainController;
import member.Member;
import reservation.ReservationService;

public class CheckReservationCommand implements Command, RequiredMember {

    private final ReservationService reservationService;
    private Member member;

    public CheckReservationCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void execute(MainController context) {
        reservationService.viewReservations(member);
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
