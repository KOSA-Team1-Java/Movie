package command;

import controller.MainController;
import exception.ExceptionController;
import member.Member;
import member.MemberService;
import movie.*;
import reservation.Reservation;
import reservation.ReservationRepository;
import reservation.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static movie.PrintSeatMap.printSeatMap;

public class ReservationCommand implements Command {
    private final MemberService memberService;
    private final MovieService movieService;
    private final ExceptionController exceptionController;
    private final Scanner scanner;
    private Reservation reservation;
    private Movie movie;
    private Member member;

    public ReservationCommand(MemberService memberService, MovieService movieService, ExceptionController exceptionController, Scanner scanner) {
        this.memberService = memberService;
        this.movieService = movieService;
        this.exceptionController = exceptionController;
        this.scanner = scanner;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean execute(MainController context) {
        if (member == null) {
            System.out.println("로그인된 사용자가 없습니다.");
            return false;
        }

        // 1단계: 영화 목록 출력
        movieService.showMovie();
        System.out.print("예매할 영화 번호 : ");
        int movieId = scanner.nextInt(); // 영화 번호 입력
        scanner.nextLine(); // Consume newline

        // 1-1 영화 정보(객체) 가져오기 (MovieService/Repository에서 Movie 객체 반환)
        Movie selectedMovie = movieService.getMovieById(movieId);
        if (selectedMovie == null) {
            System.out.println("올바른 영화 번호가 아닙니다.");
            return false;
        }

//         1.2 나이 제한 체크!
        if (selectedMovie.getAge() != 0 && member.getAge() < selectedMovie.getAge()) {
            System.out.println("❌ 청소년 관람불가 영화입니다. 예매하실 수 없습니다.");
            return false;
        }

        // 2단계: 영화 번호에 따른 영화관 위치 출력
        List<String> locations = movieService.getLocationsByMovieId(movieId);
        if (locations.isEmpty()) {
            System.out.println("올바른 영화 번호가 아닙니다.");
            return false;
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
            System.out.println("올바른 영화관 번호가 아닙니다.");
            return false;
        }

        String selectedLocation = locations.get(locationChoice - 1);

        // 3단계: 선택한 장소에 따른 상영 시간대 출력
        List<Screening> screenings = movieService.getScreeningsByMovieAndLocation(movieId, selectedLocation);
        if (screenings.isEmpty()) {
            System.out.println("올바른 영화 시간대가 아닙니다.");
            return false;
        }

        System.out.println("선택한 영화 상영 시간대");
        for (int i = 0; i < screenings.size(); i++) {
            Screening s = screenings.get(i);
            System.out.println("번호: " + (i + 1));
            System.out.println("시간: " + s.getStartTime() + " ~ " + s.getEndTime());
            System.out.println("이용가능 좌석: " + s.getAvailableSeats());
            System.out.println("-------------------------------");
        }

        // 4단계: 상영 선택
        System.out.print("상영 번호를 입력하세요: ");
        int screeningChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (screeningChoice < 1 || screeningChoice > screenings.size()) {
            System.out.println("잘못된 상영 번호입니다.");
            return false;
        }
        Screening selectedScreening = screenings.get(screeningChoice - 1); // 인덱스로 선택


        // 5단계: 선택된 상영 정보 출력
        System.out.println("✅ 선택하신 영화:");
        System.out.println("영화제목: " + selectedScreening.getMovie().getTitle());
        System.out.println("영화관: " + selectedScreening.getTheater().getLocation());
        System.out.println("날짜: " + selectedScreening.getScreeningDate());
        System.out.println("시간: " + selectedScreening.getStartTime() + " ~ " + selectedScreening.getEndTime());
//        System.out.println("이용가능좌석: " + selectedScreening.getAvailableSeats());
        System.out.println("-------------------------------");

        System.out.print("인원: ");
        int peopleCount = scanner.nextInt();
        scanner.nextLine();

        ReservationRepository reservationRepository = new ReservationRepository();
        List<String> reservedSeats = reservationRepository.findReservedSeatsByScreeningId(selectedScreening.getId());

        // 좌석표 출력 함수
        printSeatMap(reservedSeats);

        List<SeatRequest> seatList = new ArrayList<>();
        for (int i = 0; i < peopleCount; i++) {
            while (true) {
                System.out.print("예약할 좌석 번호를 입력하세요 (예: A1): ");
                String seatInput = scanner.nextLine().trim().toUpperCase();
                if (reservedSeats.contains(seatInput)) {
                    System.out.println("이미 예약된 좌석입니다. 다른 좌석을 선택하세요.");
                    continue;
                }
                char row = seatInput.charAt(0);
                int col = Integer.parseInt(seatInput.substring(1));
                seatList.add(new SeatRequest(row, col));
                reservedSeats.add(seatInput); // 지금 선택한 것도 예약 예정으로 추가(중복입력 방지)
                break;
            }
        }

        ReservationService reservationService = new ReservationService();
        boolean success = reservationService.reserveMovie(member, selectedScreening.getMovie(), selectedScreening.getId(), seatList);

        if (success) {
            System.out.println("💳 남은 예산: " + member.getBudget() + "원");
            System.out.print("예매내역을 조회하시겠습니까? (1: 네 / 2: 나가기): ");
            String viewChoice = scanner.nextLine();

            if(viewChoice.equals("1")){
                System.out.println("-------------예매내역 조회-------------");
                System.out.println("영화제목: " + selectedScreening.getMovie().getTitle());
                System.out.println("날짜: " + selectedScreening.getScreeningDate());
                System.out.println("시간: " + selectedScreening.getStartTime() + " ~ " + selectedScreening.getEndTime());
                System.out.println("영화관: " + selectedScreening.getTheater().getLocation());
                System.out.println("총 인원: " + peopleCount);

                // 좌석 정보 출력
                System.out.print("좌석: ");
                for (int i = 0; i < seatList.size(); i++) {
                    SeatRequest seat = seatList.get(i);
                    System.out.print(seat.getRow() + "열 " + seat.getCol() + "번");
                    if (i < seatList.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(); // 줄바꿈
                System.out.println("-----------------------------------");
            }
        }

        return true;
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }
}
