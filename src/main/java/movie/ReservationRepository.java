package movie;

import java.sql.*;

import static JDBC.ConnectionConst.*;

public class ReservationRepository {
    // 예매 정보 insert 후 생성된 ID 반환
    public int insertReservation(String memberLoginId, int screeningId) {
        String sql = "INSERT INTO reservation (member_loginId, screening_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, memberLoginId);
            pstmt.setInt(2, screeningId);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // 생성된 reservation_id 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 좌석 예약 insert
    public void insertSeat(int reservationId, char seatRow, int seatCol) {
        String sql = "INSERT INTO seat (reservation_id, reservation_seat_row, reservation_seat_col) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, String.valueOf(seatRow));
            pstmt.setInt(3, seatCol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
