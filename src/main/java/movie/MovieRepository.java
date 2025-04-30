package movie;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static JDBC.ConnectionConst.*;

public class MovieRepository {

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
            e.printStackTrace();
        }
        return null;
    }

    // 영화 ID로 상영 정보 조회
    public List<Screening> getScreenings(int movieId) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.id AS screening_id, m.title AS movie_title, t.location, " +
                "s.screeningDate, s.startTime, s.endTime, t.id AS theater_id, t.seat_row, t.seat_col " +
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.id " +
                "WHERE s.movie_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int screeningId = rs.getInt("screening_id");
                String movieTitle = rs.getString("movie_title");
                String location = rs.getString("location");
                LocalDate screeningDate = rs.getDate("screeningDate").toLocalDate();
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                int theaterId = rs.getInt("theater_id");

                Movie movie = new Movie(movieId, movieTitle);
                Theater theater = new Theater(theaterId, location); // seat_row, seat_col 생략 가능
                screenings.add(new Screening(screeningId, movie, screeningDate, startTime, endTime, theater, 0, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return screenings;
    }

    // 지역을 기준으로 상영 정보 조회
    public List<Screening> findScreeningsByMovieAndLocation(int movieId, String location) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.id AS screening_id, m.title AS movie_title, t.location, " +
                "s.screeningDate, s.startTime, s.endTime, t.id AS theater_id, t.seat_row, t.seat_col " +
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.id " +
                "WHERE m.id = ? AND t.location = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, location);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int screeningId = rs.getInt("screening_id");
                String movieTitle = rs.getString("movie_title");
                String loc = rs.getString("location");
                LocalDate screeningDate = rs.getDate("screeningDate").toLocalDate();
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                int theaterId = rs.getInt("theater_id");

                Movie movie = new Movie(movieId, movieTitle);
                Theater theater = new Theater(theaterId, loc);
                screenings.add(new Screening(screeningId, movie, screeningDate, startTime, endTime, theater, 0, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return screenings;
    }
}
