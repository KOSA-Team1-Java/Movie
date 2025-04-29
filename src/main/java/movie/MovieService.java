package movie;

import java.util.List;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void showMovie() {
        List<Movie> movies = movieRepository.getMovies();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle());
        }
    }

    public void showScreening(int movieId) {
        List<Screening> screenings = movieRepository.getScreenings(movieId);
        for (Screening screening : screenings) {
            System.out.println("스크리닝 ID: " + screening.getId());
            System.out.println("상영관: " + screening.getTheater().getLocation() + " " + screening.getTheater().getRoomNumber());
            System.out.println("상영 날짜: " + screening.getScreeningDate());
            System.out.println("상영 시간: " + screening.getStartTime() + " ~ " + screening.getEndTime());
//            System.out.println("전체 좌석: " + screening.getTotalSeats());
//            System.out.println("남은 좌석: " + screening.getAvailableSeats());
            System.out.println("-------------------------------");
        }
    }
}
