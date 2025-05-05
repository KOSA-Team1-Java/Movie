package reservation;

import member.Member;
import movie.Movie;
import movie.SeatRequest;
import pay.CashPay;
import pay.CreditPay;
import pay.Pay;
import pay.PayService;

import java.util.List;
import java.util.Scanner;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PayService payService;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.payService = new PayService();
    }

    public boolean bookMovie(Member member, Movie movie, int screeningId, List<SeatRequest> seatList) {


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
//        boolean paymentSuccess = paymethod.pay(member, totalPrice);
//        if (!paymentSuccess) {
//            System.out.println("❌ 결제 실패: 잔액 부족 또는 오류");
//            return false;
//        }
//
//        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
//            conn.setAutoCommit(false); // 트랜잭션 시작
//
//            // 1. 예매 등록
//            int reservationId = reservationRepository.insertReservation(conn, member.getLoginId(), screeningId);
//            if (reservationId == -1) {
//                conn.rollback();
//                System.out.println("❌ 예매 처리에 실패하였습니다.");
//                return false;
//            }
//
//            // 2. 좌석 등록
//            for (SeatRequest seat : seatList) {
//                reservationRepository.ReservationinsertSeat(conn, reservationId, seat.getRow(), seat.getCol());
//            }
//
//            // 3. 예산 차감 및 DB 반영
//            int newBudget = member.getCash();
//            reservationRepository.updateBudget(conn, member.getLoginId(), newBudget);
//            member.setCash(newBudget); // Java 객체도 함께 업데이트
//
//            conn.commit(); // 모든 작업 성공 시 커밋
//            System.out.println("✅ 예매 및 결제 완료!");
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("❌ 예매 중 오류가 발생했습니다.");
//            return false;
//        }
        return true;
    }
}
