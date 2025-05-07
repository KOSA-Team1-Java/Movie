package reservation;

import lombok.Data;
import movie.Movie;
import movie.Screening;
import movie.Theater;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReservationDto {
    Movie movie;
    Screening screening;
    Theater theater;
    Reservation reservation;
    // 영화관 위치
    List<String> seats = new ArrayList<>();
}