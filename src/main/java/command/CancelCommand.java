package command;

import controller.MainController;
import member.Member;
import member.MemberService;
import movie.MovieService;
import reservation.Reservation;
import reservation.ReservationService;

import java.util.List;
import java.util.Scanner;

public class CancelCommand implements Command, RequiredMember{
    private final MemberService memberService;
    private final MovieService movieService;
    private final ReservationService reservationService;
    private final Scanner scanner;
    private Member member;

    public CancelCommand(MemberService memberService, MovieService movieService, ReservationService reservationService, Scanner scanner) {
        this.memberService = memberService;
        this.movieService = movieService;
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController context) {
        if (member == null) {
            member = (Member) context.getAttribute("member");
        }
        List<Reservation> reservations = (List<Reservation>) context.getAttribute("reservations");
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("취소 가능한 예매가 없습니다.");
            return;
        }
        System.out.println("취소할 예매 번호를 입력하세요:");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            System.out.printf("%d) 영화: %s | 날짜: %s | 시간: %s~%s | 예약번호: %d\n",
                    i + 1, r.getScreening().getMovie().getTitle(),
                    r.getScreening().getScreeningDate(), r.getScreening().getStartTime(),
                    r.getScreening().getEndTime(), r.getId());
        }
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx < 1 || idx > reservations.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }
        Reservation target = reservations.get(idx - 1);

        // 환불 금액 계산(예: 영화 가격 * 좌석 수)
        int refund = target.getScreening().getMovie().getPrice() * target.getPeopleCount();
        memberService.refundBudget(member, refund);
        reservationService.cancelReservation(target.getId());
        System.out.println("예매 취소 및 환불 완료! 환불 금액: " + refund + "원이 반환되었습니다.");
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

    @Override
    public void setMember(Member member) {
        this.member=member;
    }
}

