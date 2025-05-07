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
}
