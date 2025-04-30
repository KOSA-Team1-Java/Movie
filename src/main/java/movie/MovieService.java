package movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // 1단계: 영화 목록 출력
    public void showMovie() {
        List<Movie> movies = movieRepository.getMovies();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle());
        }
    }

    // 2단계: 해당 영화의 상영 정보 전체 출력
    public void showScreening(int movieId) {
        List<Screening> screenings = movieRepository.getScreenings(movieId);
        for (Screening screening : screenings) {
            System.out.println("영화 번호: " + screening.getId());
            System.out.println("영화관: " + screening.getTheater().getLocation() + " " + screening.getTheater());
            System.out.println("시간: " + screening.getScreeningDate());
            System.out.println("Time: " + screening.getStartTime() + " ~ " + screening.getEndTime());
            System.out.println("-------------------------------");
        }
    }

    // 3단계 : 영화 ID로 지역 목록 반환 (중복 제거)
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
