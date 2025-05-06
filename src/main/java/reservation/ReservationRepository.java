package reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.ConnectionConst.*;

public class ReservationRepository {

    // 예매(Reservation) insert. 생성된 ID 반환
    public int insertReservation(Connection conn, String memberLoginId, int screeningId) throws SQLException {
        String sql = "INSERT INTO reservation (member_loginId, screening_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
    public void ReservationinsertSeat(Connection conn, int reservationId, char seatRow, int seatCol) throws SQLException {
        String sql = "INSERT INTO ReservationSeat (reservation_id, seat_row, seat_col) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, String.valueOf(seatRow));
            pstmt.setInt(3, seatCol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> findReservedSeatsByScreeningId(int screeningId) {
        List<String> reservedSeats = new ArrayList<>();
        String sql = "SELECT rs.seat_row, rs.seat_col " +
                "FROM reservation r " +
                "JOIN reservationseat rs ON r.id = rs.reservation_id " +
                "WHERE r.screening_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, screeningId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String seatCode = rs.getString("seat_row") + rs.getInt("seat_col");
                    reservedSeats.add(seatCode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservedSeats;
    }


    public List<Integer> findReservationIdsByMemberLoginId(String loginId) {
        List<Integer> reservationIds = new ArrayList<>();
        String sql = "SELECT id FROM reservation WHERE member_loginId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservationIds.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationIds;
    }




    public int countReservedSeatsByScreeningId(int screeningId) {
        String sql = "SELECT COUNT(*) FROM reservation r " +
                "JOIN reservationseat rs ON r.id = rs.reservation_id " +
                "WHERE r.screening_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, screeningId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteReservationSeatsByReservationId(int reservationId) {
        String sql = "DELETE FROM reservationseat WHERE reservation_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservationById(int reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
