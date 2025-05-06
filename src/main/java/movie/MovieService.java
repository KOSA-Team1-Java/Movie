package movie;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;
import reservation.ReservationRepository;

public class MovieService {

    private final MovieRepository movieRepository;
    private final ReservationRepository reservationRepository;

    public MovieService(MovieRepository movieRepository, ReservationRepository reservationRepository) {
        this.movieRepository = movieRepository;
        this.reservationRepository = reservationRepository;
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
        return movieRepository.findByTitle(name);
    }

    public List<Screening> getScreenings(int movieId) {
        return movieRepository.getScreenings(movieId);
    }

    // 상영 정보로 예약된 좌석 리스트 가져오기
    public List<String> getReservedSeatsByScreeningId(int screeningId) {
        return reservationRepository.findReservedSeatsByScreeningId(screeningId);
    }

    // 상영 정보로 이용가능 좌석수 반환
    public int getAvailableSeatsByScreening(Screening screening) {
        int reservedCount = reservationRepository.countReservedSeatsByScreeningId(screening.getId());
        return screening.getTheater().getTotalSeat() - reservedCount;
    }

    public void printSeatMap(int screeningId) {
        List<String> reservedSeats = getReservedSeatsByScreeningId(screeningId);
        PrintSeatMap.printSeatMap(reservedSeats); // 기존 좌석표 출력 함수 재사용
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
