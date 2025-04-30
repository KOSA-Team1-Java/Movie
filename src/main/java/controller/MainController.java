//package controller;
//
//import member.MemberRepository;
//import member.MemberService;
//import movie.MovieRepository;
//import movie.MovieService;
//
//import java.util.Scanner;
//
//public class MainController {
//
//    private final MemberService memberService = new MemberService(new MemberRepository());
//    private final MovieService movieService = new MovieService(new MovieRepository());
//
//    public void call(String input) {
//        Scanner scanner = new Scanner(System.in);
//        switch (input) {
//            case "/login":
//                System.out.print("ID : ");
//                String id = scanner.nextLine();
//                System.out.print("Password : ");
//                String password = scanner.nextLine();
//                memberService.login(id, password);
//            case "/reservation":
//                movieService.showMovie();
//                System.out.print("예매할 영화 : ");
//                String movieName = scanner.nextLine();
//                movieService.showScreening(movieName);
//
//        }
//    }
//}
package controller;

import movie.MovieService;
import movie.Screening;

import java.util.List;
import java.util.Scanner;

public class MainController {

    private final MovieService movieService;

    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    public void call(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "1":
                // 1단계: 영화 목록 출력
                movieService.showMovie();
                System.out.print("어떤 영화를 선택하시겠습니까?");
                int movieNumber = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // 2단계: 지역 목록 출력
                List<String> locations = movieService.getLocationsByMovieId(movieNumber);
                if (locations.isEmpty()) {
                    System.out.println("No available locations for this movie.");
                    return;
                }

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

                // 3단계: 상영 정보 출력
                List<Screening> screenings = movieService.getScreeningsByMovieAndLocation(movieNumber, selectedLocation);
                if (screenings.isEmpty()) {
                    System.out.println("No screenings available for this location.");
                    return;
                }

                System.out.println("영화 정보");
                for (Screening s : screenings) {
                    System.out.println("번호: " + s.getId());
                    System.out.println("날짜: " + s.getScreeningDate());
                    System.out.println("시간: " + s.getStartTime() + " ~ " + s.getEndTime());
                    System.out.println("이용가능 좌석: " + s.getAvailableSeats());
                    System.out.println("-------------------------------");
                }

                // 4단계: 사용자가 screening ID 선택
                System.out.print("번호를 입력하세요: ");
                int screeningId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // 5단계: 선택한 screening ID로 상세 정보 출력
                Screening selected = screenings.stream()
                        .filter(s -> s.getId() == screeningId)
                        .findFirst()
                        .orElse(null);

                if (selected == null) {
                    System.out.println("Invalid screening ID.");
                    return;
                }

                // 선택된 상영 정보 출력
                System.out.println("✅ 선택하신 영화:");
                System.out.println("영화제목: " + selected.getMovie().getTitle());
                System.out.println("영화관: " + selected.getTheater().getLocation());
                System.out.println("날짜: " + selected.getScreeningDate());
                System.out.println("시간: " + selected.getStartTime() + " ~ " + selected.getEndTime());
                System.out.println("이용가능좌석: " + selected.getAvailableSeats());
                System.out.println("-------------------------------");

                break;

            case "2":
                System.out.println("Exit.");
                System.exit(333);
                break;

            default:
                System.out.println("다시입력해주세요");
        }
    }
}
