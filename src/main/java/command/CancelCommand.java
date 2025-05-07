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
            System.out.println("로그인이 필요합니다.");
            return;
        }

        // 1. 예매 내역 조회 및 출력
        List<String> reservations = reservationService.getFormattedReservationsByMember(member);
        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        System.out.println("------------- 예매 내역 -------------");
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println((i + 1) + "번");
            System.out.println(reservations.get(i));
            System.out.println("----------------------------------------");
        }

        // 2. 예매 번호 선택
        System.out.print("취소할 예매 번호를 입력하세요 (0 입력 시 취소): ");
        int selected = scanner.nextInt();
        scanner.nextLine();
        if (selected == 0) {
            System.out.println("❌ 취소하지 않았습니다.");
            return;
        }

        // 3. 예약 ID 가져오기
        int reservationId = reservationService.getReservationIdByIndex(member, selected - 1);
        if (reservationId == -1) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        // 4. 예매 취소 (좌석 삭제 + 결제 환불 + 예매 삭제)
        boolean success = reservationService.cancelReservation(reservationId, member.getLoginId());
        if (success) {
            System.out.println("✅ 예매가 정상적으로 취소되었습니다.");
        } else {
            System.out.println("❌ 예매 취소 중 오류가 발생했습니다.");
        }
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

