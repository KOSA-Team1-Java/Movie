package command;

import controller.MainController;
import member.Member;
import member.MemberService;
import movie.MovieService;
import reservation.ReservationService;

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
        // 1. 예매 내역 조회 및 출력
        reservationService.viewReservations(member);

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

            // 환불된 최신 Member 정보 반영
            Member refreshed = memberService.findByLoginId(member.getLoginId());
            member.setCash(refreshed.getCash());
            member.setCredit(refreshed.getCredit());

            System.out.println("💸 환불 완료! 현재 잔액 - 현금: " + member.getCash() + "원, 카드: " + member.getCredit() + "원");

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

