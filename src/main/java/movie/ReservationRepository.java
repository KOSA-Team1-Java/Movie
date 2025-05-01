package movie;

import java.sql.*;
import static JDBC.ConnectionConst.*;

public class ReservationRepository {

    // 예매(Reservation) insert. 생성된 ID 반환
    public int insertReservation(Connection conn, String memberLoginId, int screeningId) throws SQLException {
        String sql = "INSERT INTO reservation (member_loginId, screening_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, memberLoginId);
            pstmt.setInt(2, screeningId);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // 예산 업데이트
    public void updateBudget(Connection conn, String loginId, int newBudget) throws SQLException {
        String sql = "UPDATE Member SET budget = ? WHERE loginId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newBudget);
            pstmt.setString(2, loginId);
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("예산 업데이트 실패: 해당 로그인 ID 없음");
            }
        }
    }

    // 좌석(Seat) insert
    public void insertSeat(Connection conn, int reservationId, char seatRow, int seatCol) throws SQLException {
        String sql = "INSERT INTO seat (reservation_id, reservation_seat_row, reservation_seat_col) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, String.valueOf(seatRow));
            pstmt.setInt(3, seatCol);
            pstmt.executeUpdate();
        }
    }
}
