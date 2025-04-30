package controller;

import exception.ExceptionController;
import member.Member;
import member.MemberRepository;
import member.MemberService;
import movie.MovieRepository;
import movie.MovieService;

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
                } catch (Exception e) {
                    exceptionController.loginError(e);
                }
                break;

            case "/logout":
                member=null;
                break;

            case "/reservation":
                movieService.showMovie();

//                System.out.print("예매할 영화 : ");
//                String movieName = scanner.nextLine();
//
//                movieService.showScreening(movieName);
//
//                System.out.print("지역 선택 : ");
//                String region = scanner.nextLine();
//                System.out.print("상영점 선택 : ");
//                String theaterName = scanner.nextLine(); //ex) 혜화점, 월드타워점
//
//                movieService.showScreening(movieName);
//
//                System.out.print("월 ex)4월 : ");
//                String month = scanner.nextLine();
//                System.out.print("일 ex)24 : ");
//                String day = scanner.nextLine();
//
//                System.out.print("인원 : ");
//                int peopleCount = scanner.nextInt();
//                scanner.nextLine();
////                movieService.showTheater(Screening screening);
//
//                selectedSeats.clear();
//                for (int i = 0; i < peopleCount; i++) {
//                    System.out.println("좌석 : ");
//                    String s = scanner.nextLine();
//                    selectedSeats.add(s);
//                }
//                memberService.bookMovie();

        }
        scanner.close();
    }
}
