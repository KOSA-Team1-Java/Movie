package movie;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void showMovie() {
        try {
            PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
            List<String> movies = movieRepository.getMovies();
            for (String movie : movies) {
                printStream.println(movie);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void showScreening(String movie) {
//        movieRepository.getScreenings();
//    }

//    public Reservation book(Screening screening) {
//    }
}
