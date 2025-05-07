package movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Theater {
    private int id;
    private String location;
    private int totalSeat;
}
