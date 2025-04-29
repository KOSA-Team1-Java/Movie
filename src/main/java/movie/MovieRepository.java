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

    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title FROM movie";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // 연결 성공 시 메시지 출력
            System.out.println("Connection success!!");

            while (rs.next()) {
                int movieId = rs.getInt("id");
                String title = rs.getString("title");
                movies.add(new Movie(movieId, title));
            }
        } catch (SQLException e) {
            System.out.println("연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public List<Screening> getScreenings(int movieId) {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT s.screening_id, m.title AS movie_title, t.location, t.room_number, t.screening_date, " +
                "s.start_time, s.end_time, s.total_seats, s.available_seats, t.theater_id " + // theater_id 추가
                "FROM screening s " +
                "JOIN movie m ON s.movie_id = m.id " +
                "JOIN theater t ON s.theater_id = t.theater_id " +
                "WHERE s.movie_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int screeningId = rs.getInt("screening_id");
                String movieTitle = rs.getString("movie_title");
                String location = rs.getString("location");
                int roomNumber = rs.getInt("room_number");
                LocalDate screeningDate = rs.getDate("screening_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");

                // 영화 객체 생성
                Movie movie = new Movie(movieId, movieTitle);

                // 극장 객체 생성
                int theaterId = rs.getInt("theater_id"); // theater_id 가져오기
                Theater theater = new Theater(theaterId, location, roomNumber); // 극장 객체에 theaterId 전달

                // Screening 객체 생성
                screenings.add(new Screening(screeningId, movie, screeningDate, startTime, endTime, theater, totalSeats, availableSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return screenings;
    }
}
