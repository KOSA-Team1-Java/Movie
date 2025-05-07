package command;

import controller.MainController;
import movie.Movie;
import movie.MovieService;

import java.util.Scanner;

public class MoviesCommand implements Command {
    private final MovieService movieService;

    public MoviesCommand(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void execute(MainController context) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // 1. 영화 목록 출력
            movieService.showMovie();

            // 2. 입력 받기
            System.out.print("상세 정보를 볼 영화 번호를 입력하세요 (0 입력 시 초기화면): ");
            int input = scanner.nextInt();

            if (input == 0) {
                System.out.println("🎬 영화 상세 정보 조회를 종료합니다.");
                break;
            }

            // 3. 영화 정보 조회
            Movie movie = movieService.getMovieById(input);
            if (movie != null) {
                System.out.println("--------------------------------");
                System.out.println("🎬 영화 상세 정보");
                System.out.println("제목: " + movie.getTitle());
                System.out.println("가격: " + movie.getPrice() + "원");
                System.out.println("연령 등급: " + movie.getAge() + "세 이상 관람가");
                System.out.println("--------------------------------");
            } else {
                System.out.println("❌ 해당 번호의 영화가 존재하지 않습니다.");
            }
        }
    }
}

