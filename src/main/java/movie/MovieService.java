package movie;

import java.util.List;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void showMovie() {
        List<Movie> movies = movieRepository.getMovies();
        for (Movie movie : movies) {
            System.out.println(movie.getTitle());
        }
    }

    public void showScreening(String movie) {
        movieRepository.getScreenings();
    }

//    public Reservation book(Screening screening) {
//    }
}
