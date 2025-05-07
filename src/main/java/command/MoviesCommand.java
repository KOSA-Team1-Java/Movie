package command;

import controller.MainController;
import movie.MovieService;

public class MoviesCommand implements Command {
    private final MovieService movieService;

    public MoviesCommand(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void execute(MainController context) {
        movieService.showMovie();
    }
}
