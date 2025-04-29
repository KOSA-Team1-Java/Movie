//import controller.MainController;
//
//import java.util.Scanner;
//
//public class Main {
//
//    private static final MainController controller = new MainController();
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        boolean login = false;
//        System.out.println("KingSmile 영화관에 오신걸 환영합니다.");
//        System.out.println("/ 입력 : 커맨드 보기");
//        while(true) {
//            String input = scanner.nextLine();
//            if(input.equals("exit")) {
//                break;
//            } else controller.call(input);
//        }
//        System.out.println("안녕히 가세요");
//    }
//}
import controller.MainController;
import movie.MovieRepository;
import movie.MovieService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // MovieRepository와 MovieService 객체를 생성
        MovieRepository movieRepository = new MovieRepository();
        MovieService movieService = new MovieService(movieRepository);

        // MainController 생성 시 MovieService 전달
        MainController controller = new MainController(movieService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("CGV영화관입니다! ");
        System.out.println("영화를 선택해주세요");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            } else {
                controller.call(input);
            }
        }
        System.out.println("안녕히 가세요");
    }
}

