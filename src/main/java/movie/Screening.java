package movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screening {
    private int id;
    private Movie movie;
    private Theater theater;
    private LocalDate screeningDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
