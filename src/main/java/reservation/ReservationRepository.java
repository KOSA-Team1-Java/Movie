package reservation;

import member.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static util.ConnectionConst.*;

public class ReservationRepository {

    public int insertReservation(Connection conn, String memberLoginId, int screeningId, int cash, int credit) throws SQLException {
        String sql = "INSERT INTO reservation (member_loginId, screening_id, cash, credit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, memberLoginId);
            pstmt.setInt(2, screeningId);
            pstmt.setInt(3, cash);
            pstmt.setInt(4, credit);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int countReservedSeatsByScreeningId(int screeningId) {
        String sql = "SELECT COUNT(*) FROM reservationseat WHERE reservation_id IN " +
                "(SELECT id FROM reservation WHERE screening_id = ?)";
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

    public List<String> findReservedSeatsByScreeningId(int screeningId) {
        List<String> reservedSeats = new ArrayList<>();
        String sql = "SELECT seat_row, seat_col FROM reservationseat WHERE reservation_id IN " +
                "(SELECT id FROM reservation WHERE screening_id = ?)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, screeningId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String seat = rs.getString("seat_row") + rs.getInt("seat_col");
                    reservedSeats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservedSeats;
    }

    public List<ReservationDto> findReservationsByMember(Member member) {
        List<ReservationDto> reservationDto = new ArrayList<>();
        String sql = "SELECT r.id, mv.title, r.screening_id, s.screeningdate, s.starttime, s.endtime, th.location, " +
                "rs.seat_row, rs.seat_col " +
                "FROM reservation r " +
                "JOIN screening s ON r.screening_id = s.id " +
                "JOIN movie mv ON s.movie_id = mv.id " +
                "JOIN reservationseat rs ON r.id = rs.reservation_id " +
                "JOIN theater th ON s.theater_id = th.id " +
                "WHERE r.member_loginId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getLoginId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}