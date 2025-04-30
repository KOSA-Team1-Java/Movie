package movie;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Movie {
    private int id;
    private String title;
    private int price;
    private List<Screening> screenings;

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getPrice() {
        return this.price;
    }
}
