package reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import movie.Movie;
import movie.Screening;
import movie.Theater;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ReservationDto {
    Movie movie;
    Screening screening;
    Theater theater;
    Reservation reservation;
    List<ReservationSeat> seats = new ArrayList<>();
}