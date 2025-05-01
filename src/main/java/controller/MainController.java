package controller;

import exception.ExceptionController;
import member.Member;
import member.MemberRepository;
import member.MemberService;
import movie.*;
import pay.CashPay;
import pay.CreditPay;
import pay.Pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainController {

    private final MemberService memberService = new MemberService(new MemberRepository());
    private final MovieService movieService = new MovieService(new MovieRepository());
    private static final ExceptionController exceptionController = new ExceptionController();
    private static Member member;

    public void call(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "/signup":
                System.out.print("ID : ");
                String id = scanner.nextLine();
                System.out.print("Password : ");
                String password = scanner.nextLine();
                System.out.print("Name : ");
                String name = scanner.nextLine();
                System.out.print("Age : ");
                int age = scanner.nextInt();
                try {
                    memberService.signUp(id, password, name, age);
                } catch (Exception e) {
                    exceptionController.signUpError(e);
                }
                break;

            case "/login":
                System.out.print("ID : ");
                String id1 = scanner.nextLine();
                System.out.print("Password : ");
                String password1 = scanner.nextLine();
                try {
                    member = memberService.login(id1, password1);
                    System.out.println(member.getName() + "님 로그인 되셨습니다");
                } catch (Exception e) {
                    exceptionController.loginError(e);
                }
                break;

            case "/logout":
                if (member == null) {
                    break;
                }else {
                    System.out.println(member.getName() + "님 로그아웃 되셨습니다");
                    member = null;
                    break;
                }

            case "/res":
                // 1단계: 영화 목록 출력
                System.out.println("로그인성공!!");
                movieService.showMovie();
                System.out.print("예매할 영화 번호 : ");
                int movieId = scanner.nextInt(); // 영화 번호 입력
                scanner.nextLine(); // Consume newline

                // 2단계: 영화 번호에 따른 영화관 위치 출력
                List<String> locations = movieService.getLocationsByMovieId(movieId);
                if (locations.isEmpty()) {
                    System.out.println("No available locations for this movie.");
                    return;
                }

                // 영화관 위치 출력 (번호로 선택)
                System.out.println("번호에 따라 장소를 선택하세요");
                for (int i = 0; i < locations.size(); i++) {
                    System.out.println((i + 1) + ". " + locations.get(i));
                }

                System.out.print("장소 번호 입력 : ");
                int locationChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (locationChoice < 1 || locationChoice > locations.size()) {
                    System.out.println("Invalid location number.");
                    return;
                }

                String selectedLocation = locations.get(locationChoice - 1);

                // 3단계: 선택한 장소에 따른 상영 시간대 출력
                List<Screening> screenings = movieService.getScreeningsByMovieAndLocation(movieId, selectedLocation);
                if (screenings.isEmpty()) {
                    System.out.println("No screenings available for this location.");
                    return;
                }

                System.out.println("선택한 영화 상영 시간대");
                for (Screening s : screenings) {
                    System.out.println("번호: " + s.getId());
                    System.out.println("시간: " + s.getStartTime() + " ~ " + s.getEndTime());
                    System.out.println("이용가능 좌석: " + s.getAvailableSeats());
                    System.out.println("-------------------------------");
                }

                // 4단계: 상영 선택
                System.out.print("상영 번호를 입력하세요: ");
                int screeningId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Screening selectedScreening = screenings.stream()
                        .filter(s -> s.getId() == screeningId)
                        .findFirst()
                        .orElse(null);

                if (selectedScreening == null) {
                    System.out.println("Invalid screening ID.");
                    return;
                }

                // 5단계: 선택된 상영 정보 출력
                System.out.println("✅ 선택하신 영화:");
                System.out.println("영화제목: " + selectedScreening.getMovie().getTitle());
                System.out.println("영화관: " + selectedScreening.getTheater().getLocation());
                System.out.println("날짜: " + selectedScreening.getScreeningDate());
                System.out.println("시간: " + selectedScreening.getStartTime() + " ~ " + selectedScreening.getEndTime());
                System.out.println("이용가능좌석: " + selectedScreening.getAvailableSeats());
                System.out.println("-------------------------------");

                System.out.print("인원: ");
                int peopleCount = scanner.nextInt();
                scanner.nextLine();

                List<SeatRequest> seatList = new ArrayList<>();
                for (int i = 0; i < peopleCount; i++) {
                    System.out.print("예약할 좌석 번호를 입력하세요 (예: A1): ");
                    String seatInput = scanner.nextLine().trim().toUpperCase();
                    char row = seatInput.charAt(0);
                    int col = Integer.parseInt(seatInput.substring(1));
                    seatList.add(new SeatRequest(row, col));
                }

                ReservationService reservationService = new ReservationService();
                boolean success = reservationService.reserveMovie(member, selectedScreening.getMovie(), selectedScreening.getId(), seatList);

                if (success) {
                    System.out.println("✅ 예매 성공!");
                } else {
                    System.out.println("❌ 예매 실패.");
                }


            case "2":
                System.out.println("Exit.");
                System.exit(333);
                break;

            default:
                System.out.println("다시입력해주세요");
        }
    }
}
