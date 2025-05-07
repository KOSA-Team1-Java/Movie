package reservation;

import exception.MovieException;
import member.Member;
import movie.Movie;
import movie.Screening;
import movie.Theater;

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

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new MovieException(e.getMessage());
        }
        return -1;
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
            throw new MovieException(e.getMessage());
        }
    }

    public List<ReservationDto> findReservationsByMember(Member member) {
        Map<Integer, ReservationDto> reservationMap = new LinkedHashMap<>();
        String sql = "SELECT r.id as reservation_id, " +
                "mv.id, mv.title, mv.price, mv.age, " +
                "s.id as screening_id, s.screeningdate, s.starttime, s.endtime, " +
                "th.id as theater_id, th.location, th.total_seat, " +
                "r.cash, r.credit, " +
                "rs.id as seat_id, rs.seat_row, rs.seat_col " +
                "FROM reservation r " +
                "JOIN screening s ON r.screening_id = s.id " +
                "JOIN movie mv ON s.movie_id = mv.id " +
                "JOIN reservationseat rs ON r.id = rs.reservation_id " +
                "JOIN theater th ON s.theater_id = th.id " +
                "WHERE r.member_loginId = ? " +
                "ORDER BY r.id, rs.id";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getLoginId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int reservationId = rs.getInt("reservation_id");
                    ReservationDto dto = reservationMap.get(reservationId);

                    if (dto == null) {
                        // Builder로 객체 생성
                        Movie movie = Movie.builder()
                                .id(rs.getInt("id"))               // mv.id
                                .title(rs.getString("title"))
                                .price(rs.getInt("price"))
                                .age(rs.getInt("age"))
                                .build();

                        Screening screening = Screening.builder()
                                .id(rs.getInt("screening_id"))
                                .screeningDate(rs.getDate("screeningdate").toLocalDate())
                                .startTime(rs.getTime("starttime").toLocalTime())
                                .endTime(rs.getTime("endtime").toLocalTime())
                                .build();

                        Theater theater = Theater.builder()
                                .id(rs.getInt("theater_id"))
                                .location(rs.getString("location"))
                                .totalSeat(rs.getInt("total_seat"))
                                .build();

                        Reservation reservation = Reservation.builder()
                                .id(reservationId)
                                .cash(rs.getInt("cash"))
                                .credit(rs.getInt("credit"))
                                .build();

                        // 좌석은 Builder에 빈 리스트로, 아래서 add
                        dto = ReservationDto.builder()
                                .movie(movie)
                                .screening(screening)
                                .theater(theater)
                                .reservation(reservation)
                                .seats(new ArrayList<>())
                                .build();

                        reservationMap.put(reservationId, dto);
                    }
                    // 좌석 생성 및 추가
                    ReservationSeat seat = ReservationSeat.builder()
                            .id(rs.getInt("seat_id"))
                            .seatRow(rs.getString("seat_row").charAt(0))
                            .seatCol(rs.getInt("seat_col"))
                            .build();

                    dto.getSeats().add(seat);
                }
            }
        } catch (SQLException e) {
            throw new MovieException(e.getMessage());
        }
        return new ArrayList<>(reservationMap.values());
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

    // 좌석(Seat) count 조회
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

    // 예약된 좌석 조회
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

    public int findReservationIdByIndex(String loginId, int index) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM reservation WHERE member_loginId = ? ORDER BY id";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (index >= 0 && index < ids.size()) {
            return ids.get(index);
        }
        return -1;
    }
    // 좌석 정보 삭제 (reservation_seat 테이블)
    public void deleteSeatsByReservationId(Connection conn, int reservationId) throws SQLException {
        String sql = "DELETE FROM reservationseat WHERE reservation_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    // 예매 정보 삭제 (reservation 테이블)
    public void deleteReservationById(Connection conn, int reservationId) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }
}


