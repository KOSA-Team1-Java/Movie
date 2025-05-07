package reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationSeat {
    private final int id;
    private final Reservation reservation;
    private final char seatRow;
    private final int seatCol;
}
