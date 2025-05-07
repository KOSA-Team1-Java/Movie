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
//        if (member == null) {
//            System.out.println("로그인이 필요합니다.");
//            return;
//        }
//
//        // 1. 사용자의 예약 내역을 조회하고 출력
//        List<String> reservations = reservationService.getReservationsByMember(member);
//        if (reservations.isEmpty()) {
//            System.out.println("예매 내역이 없습니다.");
//            return;
//        }
//
//        System.out.println("현재 예매 내역:");
//        for (int i = 0; i < reservations.size(); i++) {
//            System.out.println(reservations.get(i));
//            System.out.println("----------------------------------------");
//        }
//
//        // 2. 취소할 예매 번호를 입력받는다
//        System.out.print("취소할 예매 번호를 입력하세요 (취소하지 않으려면 0 입력): ");
//        int reservationNumber = scanner.nextInt();
//        if (reservationNumber == 0) {
//            System.out.println("취소가 취소되었습니다.");
//            return;
//        }
//
//        // 3. 선택한 예매 번호의 예약 ID 가져오기
//        int reservationId = getReservationIdByNumber(reservationNumber);
//        if (reservationId == -1) {
//            System.out.println("잘못된 번호입니다.");
//            return;
//        }
//
//        // 4. 예매 취소 처리
//        boolean success = reservationService.cancelReservation(reservationId);
//        if (success) {
//            System.out.println("✅ 예매가 취소되었습니다.");
//        } else {
//            System.out.println("❌ 예매 취소 실패");
//        }
//    }
//
//    // 예약 번호에 해당하는 예약 ID를 반환하는 메서드
//    private int getReservationIdByNumber(int reservationNumber) {
//        List<String> reservations = reservationService.getReservationsByMember(member);
//        if (reservationNumber > 0 && reservationNumber <= reservations.size()) {
//            // 예약 번호에 해당하는 예약 ID를 반환 (1부터 시작하므로 -1)
//            return reservationService.getReservationIdByIndex(reservationNumber - 1);
//        }
//        return -1; // 잘못된 번호일 경우
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

