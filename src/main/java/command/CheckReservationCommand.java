package command;

import controller.MainController;
import member.Member;
import reservation.ReservationService;

import java.util.List;

public class CheckReservationCommand implements Command, RequiredMember {

    private final ReservationService reservationService;
    private Member member;

    public CheckReservationCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void execute(MainController context) {
        // 로그인된 회원의 예약 정보를 조회
        List<String> reservations = reservationRepository.findReservationsByMemberLoginId(member.getLoginId());

        if (reservations.isEmpty()) {
            System.out.println("예약 내역이 없습니다.");
        } else {
            System.out.println("------------- 예매내역 조회 -------------");
            for (String reservation : reservations) {
                System.out.println(reservation);
                System.out.println("----------------------------------------");
            }
            System.out.println("------------- 예매내역 조회 끝 -------------");
        }
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
