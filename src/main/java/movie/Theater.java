package movie;

import lombok.Getter;

@Getter
public class Theater {
    private int id;
    private String location;
    private int totalSeat;

    public Theater(int id, String location, int totalSeat) {
        this.id = id;
        this.location = location;
        this.totalSeat = totalSeat;
    }
}
