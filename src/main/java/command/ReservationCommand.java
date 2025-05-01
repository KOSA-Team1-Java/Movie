package command;

import exception.ExceptionController;
import member.Member;
import member.MemberService;
import movie.Movie;
import movie.MovieService;
import movie.Reservation;
import movie.Screening;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    @Override
    public void execute() {
        // 1단계: 영화 목록 출력
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

        System.out.println("✅ 결제가 완료되었습니다.");
    }
}
