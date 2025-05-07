package movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Movie {
    private int id;
    private String title;
    private int price;
    private int age;

    public void setPrice(int price) {

    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setDirector(String director) {}

    public void setGenre(String genre) {
    }
}
