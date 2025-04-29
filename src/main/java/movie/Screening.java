package movie;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Screening {
    private int id;
    private Movie movie;
    private LocalDate screeningDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Theater theater;
    private int totalSeats;
    private int availableSeats;

    public Screening(int id, Movie movie, LocalDate screeningDate, LocalTime startTime, LocalTime endTime, Theater theater, int totalSeats, int availableSeats) {
        this.id = id;
        this.movie = movie;
        this.screeningDate = screeningDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.theater = theater;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }


}
