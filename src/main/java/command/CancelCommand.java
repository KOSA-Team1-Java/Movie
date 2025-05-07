package command;

import controller.MainController;
import member.Member;
import member.MemberService;
import movie.MovieService;
import movie.Screening;
import reservation.Reservation;
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

        // 1. 프로그램에 이미 존재하는 모든 Screening 리스트 재활용
        List<Screening> allScreenings = movieService.getAllScreenings();

        // 2. 예매 내역 리스트 조회 (Repository, Service 통해)
        List<Reservation> reservations = new ArrayList<>();
        // ReservationRepository에는 by memberLoginId + screening list를 받아 Reservation을 반환하는 메서드를 직접 만드세요.
        // 아래는 직접 ReservationRepository에서 가져오는 예제
        String loginId = member.getLoginId();
        try {
            String sql = "SELECT * FROM reservation WHERE member_loginId = ?";
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, loginId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int reservationId = rs.getInt("id");
                        int screeningId = rs.getInt("screening_id");
                        int cash = rs.getInt("cash");
                        int credit = rs.getInt("credit");
                        Screening screening = allScreenings.stream()
                                .filter(s -> s.getId() == screeningId)
                                .findFirst().orElse(null);
                        // Reservation 생성자(생성자 구조에 맞게 peopleCount 등 채우기)
                        reservations.add(new Reservation(reservationId, screening, 1, member, cash, credit));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        // 2. 예매 목록 출력
        System.out.println("=== 예매 내역 ===");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            System.out.printf("%d. [%s] %s %s (%d명, 총 %d원)\n",
                    i + 1,
                    r.getScreening().getMovie().getTitle(),
                    r.getScreening().getScreeningDate(),
                    r.getScreening().getStartTime(),
                    r.getPeopleCount(),
                    r.getTotalPrice()
            );
        }

        System.out.print("취소할 예매 번호를 입력하세요(0번: 취소): ");
        int idx = 0;
        try {
            idx = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("올바른 번호를 입력하세요.");
            return;
        }
        if (idx <= 0 || idx > reservations.size()) {
            System.out.println("예매 취소를 종료합니다.");
            return;
        }

        Reservation toCancel = reservations.get(idx - 1);

        // 3. 환불·좌석반환·예매삭제 한번에 처리 (Service에 위임)
        reservationService.cancelReservation(toCancel.getId(), member, memberService, allScreenings);

        System.out.println("✅ 예매 취소 및 환불이 완료되었습니다.");
        System.out.printf("환불 후 예산: 현금=%d원, 카드=%d원\n", member.getCash(), member.getCredit());

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

