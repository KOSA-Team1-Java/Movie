package reservation;

import member.Member;
import movie.Movie;
import movie.Screening;
import movie.Theater;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static util.ConnectionConst.*;

public class ReservationRepository {
    // INSERT 예매(예약)
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
        }
        return -1;
    }

    // INSERT 좌석
    public void ReservationinsertSeat(Connection conn, int reservationId, char seatRow, int seatCol) throws SQLException {
        String sql = "INSERT INTO ReservationSeat (reservation_id, seat_row, seat_col) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, String.valueOf(seatRow));
            pstmt.setInt(3, seatCol);
            pstmt.executeUpdate();
        }
    }

    // 예매 내역을 번호별 포맷 문자열로 반환 (UI용)
    public List<String> getFormattedReservationsByMember(Member member) {
        List<String> result = new ArrayList<>();
        List<ReservationDto> dtos = findReservationsByMember(member);
        for (ReservationDto dto : dtos) {
            StringBuilder sb = new StringBuilder();
            sb.append("예매번호: ").append(dto.getReservation().getId()).append("\n");
            sb.append("영화: ").append(dto.getMovie().getTitle()).append("\n");
            sb.append("상영: ").append(dto.getScreening().getScreeningDate())
                    .append(" ")
                    .append(dto.getScreening().getStartTime()).append("~").append(dto.getScreening().getEndTime()).append("\n");
            sb.append("극장: ").append(dto.getTheater().getLocation()).append("\n");
            sb.append("좌석: ").append(String.join(", ", dto.getSeats())).append("\n");
            sb.append("결제: 현금 ").append(dto.getReservation().getCash())
                    .append(", 카드 ").append(dto.getReservation().getCredit());
            result.add(sb.toString());
        }
        return result;
    }

    // 예매 내역 + 결제정보 + 좌석정보 DTO로 반환
    public List<ReservationDto> findReservationsByMember(Member member) {
        Map<Integer, ReservationDto> reservationMap = new LinkedHashMap<>();
        String sql = "SELECT r.id, mv.title, r.screening_id, s.screeningDate, s.startTime, s.endTime, " +
                "th.location, rs.seat_row, rs.seat_col, r.cash, r.credit " +
                "FROM reservation r " +
                "JOIN screening s ON r.screening_id = s.id " +
                "JOIN movie mv ON s.movie_id = mv.id " +
                "JOIN reservationseat rs ON r.id = rs.reservation_id " +
                "JOIN theater th ON s.theater_id = th.id " +
                "WHERE r.member_loginId = ? " +
                "ORDER BY r.id, rs.seat_row, rs.seat_col";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getLoginId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int reservationId = rs.getInt("id");
                    String title = rs.getString("title");
                    int screeningId = rs.getInt("screening_id");

                    java.sql.Date screeningDate = rs.getDate("screeningDate"); // 대소문자 일치!
                    java.sql.Time startTime = rs.getTime("startTime");
                    java.sql.Time endTime = rs.getTime("endTime");
                    String location = rs.getString("location");

                    String seat = rs.getString("seat_row") + rs.getInt("seat_col");

                    int cash = rs.getInt("cash");
                    int credit = rs.getInt("credit");

                    // ReservationDto를 reservationMap에 group by
                    ReservationDto dto = reservationMap.get(reservationId);
                    if (dto == null) {
                        ReservationDto newDto = new ReservationDto();
                        newDto.setReservation(new Reservation(reservationId, member, null, cash, credit));
                        newDto.setMovie(new Movie(0, title, 0, 0)); // id, price, age 등 필요하면 select에 추가
                        newDto.setScreening(new Screening(screeningId, null, null, screeningDate.toLocalDate(),
                                startTime.toLocalTime(), endTime.toLocalTime()));
                        newDto.setTheater(new Theater(0, location, 0)); // id, seat 등 필요하면 select에 추가
                        reservationMap.put(reservationId, newDto);
                        dto = newDto;
                    }
                    dto.getSeats().add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(reservationMap.values());
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

    // 예약번호 인덱스→ID 변환
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

    // 좌석정보 삭제
    public void deleteSeatsByReservationId(Connection conn, int reservationId) throws SQLException {
        String sql = "DELETE FROM reservationseat WHERE reservation_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    // 예매정보 삭제
    public void deleteReservationById(Connection conn, int reservationId) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }
}
