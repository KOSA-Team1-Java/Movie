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

        }
    }
}
