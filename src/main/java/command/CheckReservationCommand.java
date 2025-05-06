package command;

import controller.MainController;
import member.Member;
import reservation.ReservationRepository;

import java.util.List;

public class CheckReservationCommand implements Command, RequiredMember {

    private Member member;
    private final ReservationRepository reservationRepository;

    // 생성자에서 ReservationRepository를 주입받도록 수정
    public CheckReservationCommand(ReservationRepository reservationRepository) {
        if (reservationRepository == null) {
            throw new IllegalArgumentException("ReservationRepository cannot be null");
        }
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void execute(MainController context) {
        if (member == null) {
            System.out.println("로그인 후 예약을 확인할 수 있습니다.");
            return;
        }

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
    public boolean requiresLogin() {
        return true;
    }
}
