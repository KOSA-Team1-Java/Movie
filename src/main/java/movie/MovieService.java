package movie;

import reservation.ReservationRepository;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
}
