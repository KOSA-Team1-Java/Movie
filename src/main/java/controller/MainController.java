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
                movieService.showMovie(); // 영화 목록 출력
                System.out.print("영화를 선택하세요 (번호 입력): ");
                int movieNumber = scanner.nextInt();
                // 선택한 영화의 ID를 이용해 스크리닝 정보 출력
                movieService.showScreening(movieNumber);
                break;
            case "2":
                System.out.println("프로그램을 종료합니다.");
                break;
            default:
                System.out.println("잘못된 입력입니다.");
        }
    }
}
