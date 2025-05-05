package movie;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // 1단계: 영화 목록 출력
    public void showMovie() {
        try {
            PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
            List<String> movies = movieRepository.getMovies();
            for (int i = 0; i< movies.size(); i++) {
                printStream.println(i+1 + "." +movies.get(i));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // 1-1 단계
    public Movie getMovieByTitle(String name) {
        return movieRepository.findByTitle(name); // 실제 구현 구조에 따라 다름
    }


    public List<Screening> getScreenings(int movieId) {
        return movieRepository.getScreenings(movieId);
    }


    // 2단계 : 영화 ID로 지역 목록 반환
    public List<String> getLocationsByMovieId(int movieId) {
        List<Screening> screenings = movieRepository.getScreenings(movieId);
        return screenings.stream()
                .map(s -> s.getTheater().getLocation())
                .distinct()
                .collect(Collectors.toList());
    }

    // 4 단계: 영화 ID + 지역으로 해당하는 상영 리스트 반환
    public List<Screening> getScreeningsByMovieAndLocation(int movieId, String location) {
        // 지역 필터링
        List<Screening> screenings = movieRepository.findScreeningsByMovieAndLocation(movieId, location);
        return screenings;
    }

    // 5 단계 : 지역을 선택한 후 상영 정보 출력
    public void showScreeningByLocation(int movieId, String location) {
        List<Screening> screenings = getScreeningsByMovieAndLocation(movieId, location);

        if (screenings.isEmpty()) {
            System.out.println("No screenings available for this movie at this location.");
        } else {
            for (Screening screening : screenings) {
                System.out.println("Screening ID: " + screening.getId());
                System.out.println("Theater: " + screening.getTheater().getLocation() + " " + screening.getTheater());
                System.out.println("Day: " + screening.getScreeningDate());
                System.out.println("Time: " + screening.getStartTime() + " ~ " + screening.getEndTime());
                System.out.println("-------------------------------");
            }
        }
    }
}
