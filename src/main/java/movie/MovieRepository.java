package movie;

import exception.ExceptionController;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static util.ConnectionConst.*;

public class MovieRepository {

    private final ExceptionController exceptionController = new ExceptionController();

    public List<String> getMovies() {
        String sql = "SELECT title FROM movie";

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> movies = new ArrayList<>();
            while (resultSet.next()) {
                movies.add(resultSet.getString("title"));
            }
            return movies;
        } catch (SQLException e) {
            exceptionController.sqlError(e);
        }
        return null;
    }

    public Movie findByTitle(String name) {
        String sql = "SELECT id, title, price, age FROM movie WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    int price = rs.getInt("price");
                    int age = rs.getInt("age");
                    Movie movie = new Movie(id, title, price, age); // 생성자에 price/age 추가되어 있으면 거기에 맞게 생성
                    movie.setPrice(price); // setPrice/setAge 필요
                    movie.setAge(age);
                    return movie;
                }
            }
        } catch (SQLException e) {
            exceptionController.sqlError(e);
        }
        return null;
    }

    // 영화 ID로 상영 정보 조회
    public List<Screening> getScreenings(int movieId) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.id AS screening_id, s.screeningDate, s.startTime, s.endTime, " +
                "m.title AS movie_title, m.price AS movie_price, m.age AS movie_age, " +
                "t.id AS theater_id, t.location, t.total_seat " +
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.id " +
                "WHERE s.movie_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String movieTitle = rs.getString("movie_title");
                int moviePrice = rs.getInt("movie_price");
                int movieAge = rs.getInt("movie_age");
                Movie movie = new Movie(movieId, movieTitle, moviePrice, movieAge);

                int theaterId = rs.getInt("theater_id");
                String location = rs.getString("location");
                int totalSeat = rs.getInt("total_seat");
                Theater theater = new Theater(theaterId, location, totalSeat); // seat_row, seat_col 생략 가능

                int screeningId = rs.getInt("screening_id");
                LocalDate screeningDate = rs.getDate("screeningDate").toLocalDate();
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                screenings.add(new Screening(screeningId, movie, theater, screeningDate, startTime, endTime));
            }
        } catch (SQLException e) {
            exceptionController.sqlError(e);
        }
        return screenings;
    }
}