import controller.MainController;
import exception.ExceptionController;
import member.MemberRepository;
import member.MemberService;
import movie.MovieRepository;
import movie.MovieService;
import reservation.ReservationRepository;
import reservation.ReservationService;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        PrintStream printStream = new PrintStream(System.out, true, "UTF-8");

        MemberRepository memberRepository = new MemberRepository();
        MovieRepository movieRepository = new MovieRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        MemberService memberService = new MemberService(memberRepository);
        MovieService movieService = new MovieService(movieRepository, reservationRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);
        ExceptionController exceptionController = new ExceptionController();
        MainController mainController = new MainController(memberService, movieService, reservationService, exceptionController, scanner);

        printStream.println("🎞️🎬🎥📽️1조 영화관에 오신 걸 환영합니다.📽️🎥🎬🎞️");

        while (true) {
            printStream.println("/command : 사용할 수 있는 명령어 보기");
            printStream.print("입력 : ");
            String input = scanner.nextLine();

            if (input.equals("/exit")) {
                printStream.println("안녕히 가세요.");
                break;
            }
            mainController.call(input);
        }
        printStream.close();
    }
}
