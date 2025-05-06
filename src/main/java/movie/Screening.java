package movie;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class Screening {
    private int id;
    private Movie movie;
    private Theater theater;
    private LocalDate screeningDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Screening(int id, Movie movie, Theater theater, LocalDate screeningDate, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.screeningDate = screeningDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
