package command;

import controller.MainController;
import movie.MovieService;

import java.util.Scanner;

public class MoviesCommand implements Command {
    private final MovieService movieService;
    private final Scanner scanner;

    public MoviesCommand(MovieService movieService, Scanner scanner) {
        this.movieService = movieService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController context) {
        movieService.showMovie();
    }
}
