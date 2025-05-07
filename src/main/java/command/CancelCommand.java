package command;

import controller.MainController;
import member.Member;
import member.MemberService;
import movie.MovieService;
import movie.Screening;
import reservation.Reservation;
import reservation.ReservationDto;
import reservation.ReservationService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.ConnectionConst.*;

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
            System.out.println("로그인 후 이용해 주세요.");
            return;
        }

        // 1. 예매 내역 출력 (ReservationDto 기반)
        List<ReservationDto> reservationDtos = reservationService.viewReservationsAndReturn(member);

        if (reservationDtos.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        // 2. 예매 번호(Reservation id) 입력
        System.out.print("\n취소할 예매의 예매번호(id)를 입력하세요 (0: 취소): ");
        int reservationId;
        try {
            reservationId = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("잘못된 입력입니다.");
            return;
        }
        if (reservationId == 0) {
            System.out.println("예매 취소를 종료합니다.");
            return;
        }

        // 3. 예매 취소/환불 실행
//        List<Screening> allScreenings = movieService.getAllScreenings(); // (환불/좌석반환에 필요)
        List<Screening> allScreenings = movieService.getScreenings(reservationId);

        boolean result = reservationService.cancelReservationWithCheck(reservationId, member, memberService, allScreenings);

        if (result) {
            System.out.println("✅ 예매 취소 및 환불이 완료되었습니다.");
            System.out.printf("환불 후 예산: 현금=%d원, 카드=%d원\n", member.getCash(), member.getCredit());
        } else {
            System.out.println("❌ 선택하신 예매번호가 없거나 취소 실패했습니다.");
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

