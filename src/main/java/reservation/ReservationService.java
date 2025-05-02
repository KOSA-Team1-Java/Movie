package reservation;

import member.Member;
import movie.Movie;
import movie.SeatRequest;
import pay.CashPay;
import pay.CreditPay;
import pay.Pay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;

import static JDBC.ConnectionConst.*;

public class ReservationService {
    private final ReservationRepository reservationRepository = new ReservationRepository();

    public boolean reserveMovie(Member member, Movie movie, int screeningId, List<SeatRequest> seatList) {
        int totalPrice = movie.getPrice() * seatList.size(); // 총 결제 금액

        System.out.println("💳 현재 잔액: " + member.getBudget() + "원");
        System.out.println("🎟️ 총 결제 금액: " + totalPrice + "원");

        if (member.getBudget() < totalPrice) {
            System.out.println("❌ 예산이 부족하여 예매할 수 없습니다.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("결제하시겠습니까? (1.예 / 2.아니오): ");
        int response = scanner.nextInt();
        scanner.nextLine();

        if(response!=1){
            System.out.println("결제를 취소하겠습니다.");
            return false;
        }

        System.out.print("결제 수단을 선택하세요.(1.카드 / 2.현금): ");
        int payOption = scanner.nextInt();
        scanner.nextLine();

        Pay paymethod;
        if(payOption==1){
            paymethod = new CreditPay();
        }
        else if(payOption==2){
            paymethod = new CashPay();
        }
        else{
            System.out.println("잘못된 결제 수단입니다.");
            return false;
        }

        // 실제 결제 시도
        boolean paymentSuccess = paymethod.pay(member, totalPrice);
        if (!paymentSuccess) {
            System.out.println("❌ 결제 실패: 잔액 부족 또는 오류");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 예매 등록
            int reservationId = reservationRepository.insertReservation(conn, member.getLoginId(), screeningId);
            if (reservationId == -1) {
                conn.rollback();
                System.out.println("❌ 예매 처리에 실패하였습니다.");
                return false;
            }

            // 2. 좌석 등록
            for (SeatRequest seat : seatList) {
                reservationRepository.ReservationinsertSeat(conn, reservationId, seat.getRow(), seat.getCol());
            }

            // 3. 예산 차감 및 DB 반영
            int newBudget = member.getBudget();
            reservationRepository.updateBudget(conn, member.getLoginId(), newBudget);
            member.setBudget(newBudget); // Java 객체도 함께 업데이트

            conn.commit(); // 모든 작업 성공 시 커밋
            System.out.println("✅ 예매 및 결제 완료!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 예매 중 오류가 발생했습니다.");
            return false;
        }
    }
}
