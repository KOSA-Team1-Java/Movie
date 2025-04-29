package controller;

import member.MemberRepository;
import member.MemberService;
import movie.MovieRepository;
import movie.MovieService;

import java.util.Scanner;

public class MainController {

    private final MemberService memberService = new MemberService(new MemberRepository());
    private final MovieService movieService = new MovieService(new MovieRepository());

    public void call(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "/login":
                System.out.print("ID : ");
                String id = scanner.nextLine();
                System.out.print("Password : ");
                String password = scanner.nextLine();
                memberService.login(id, password);
            case "/reservation":
                movieService.showMovie();
                System.out.print("예매할 영화 : ");
                String movieName = scanner.nextLine();
                movieService.showScreening(movieName);
                System.out.print("지역 선택 : ");
                String region = scanner.nextLine();
                System.out.print("상영점 선택 : ");
                String theaterName = scanner.nextLine(); //ex) 혜화점, 월드타워점
                System.out.print("월 ex)4월 : ");
                String month = scanner.nextLine();
                System.out.print("일 ex)24 : ");
                String day = scanner.nextLine();
        }
    }
}
