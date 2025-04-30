package movie;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String password = "root1234";

    // 영화 목록 조회
    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title FROM movie";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int movieId = rs.getInt("id");
                String title = rs.getString("title");
                movies.add(new Movie(movieId, title));
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    // 영화 ID로 상영 정보 조회
    public List<Screening> getScreenings(int movieId) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.screening_id, m.title AS movie_title, t.location,  " +
                "s.screening_date, s.start_time, s.end_time, s.total_seats, s.available_seats, t.theater_id " +
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.theater_id " +
                "WHERE s.movie_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId); // 영화 ID 설정
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int screeningId = rs.getInt("screening_id");
                String movieTitle = rs.getString("movie_title");
                String location = rs.getString("location");
                LocalDate screeningDate = rs.getDate("screening_date").toLocalDate();  // s.screening_date로 가져오기
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                int theaterId = rs.getInt("theater_id");

                Movie movie = new Movie(movieId, movieTitle);
                Theater theater = new Theater(theaterId, location);
                screenings.add(new Screening(screeningId, movie, screeningDate, startTime, endTime, theater, totalSeats, availableSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외를 출력합니다.
        }

        return screenings;
    }


    // 지역을 기준으로 상영 정보 조회 (영화 ID와 지역 필터링)
    public List<Screening> findScreeningsByMovieAndLocation(int movieId, String location) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.screening_id, m.title AS movie_title, t.location, s.screening_date, " +
                "s.start_time, s.end_time, s.total_seats, s.available_seats, t.theater_id " +
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.theater_id " +
                "WHERE m.id = ? AND t.location = ?"; // movie_id 대신 m.id를 사용, location은 t.location으로 변경

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId); // 영화 ID 설정
            pstmt.setString(2, location); // 영화관 위치 설정

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int screeningId = rs.getInt("screening_id");
                String movieTitle = rs.getString("movie_title");
                String loc = rs.getString("location");
                LocalDate screeningDate = rs.getDate("screening_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                int theaterId = rs.getInt("theater_id");

                Movie movie = new Movie(movieId, movieTitle);
                Theater theater = new Theater(theaterId, loc);
                screenings.add(new Screening(screeningId, movie, screeningDate, startTime, endTime, theater, totalSeats, availableSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외를 출력합니다.
        }

        return screenings;
    }

}