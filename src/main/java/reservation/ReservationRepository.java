package reservation;

import member.Member;
import movie.Screening;

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
            e.printStackTrace();
        }
    }

    // 회원 로그인 ID로 예약 내역을 조회하는 메서드
    public List<String> findReservationsByMemberLoginId(String loginId) {
        Map<Integer, ReservationInfo> reservationMap = new LinkedHashMap<>();
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
            pstmt.setString(1, loginId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int reservationId = rs.getInt("id");
                    String title = rs.getString("title");
                    int screeningId = rs.getInt("screening_id");

                    // 날짜 및 시간 컬럼을 적절히 가져오기
                    java.sql.Date screeningDate = rs.getDate("screeningdate"); // 날짜
                    java.sql.Time startTime = rs.getTime("starttime"); // 시작 시간
                    java.sql.Time endTime = rs.getTime("endtime"); // 종료 시간
                    String location = rs.getString("location");  // 영화관 위치 (이제 location으로 사용)

                    String seat = rs.getString("seat_row") + rs.getInt("seat_col");

                    // 예약 ID로 정보 집합을 묶어 저장
                    reservationMap
                            .computeIfAbsent(reservationId, id -> new ReservationInfo(id, title, screeningId, screeningDate, startTime, endTime, location))
                            .addSeat(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> formattedReservations = new ArrayList<>();
        int reservationNumber = 1;  // 예약 번호를 1부터 시작

        // 예약 번호 추가하여 출력
        for (ReservationInfo info : reservationMap.values()) {
            formattedReservations.add("예매 " + reservationNumber++ + "번째\n" + info.format());
        }
        return formattedReservations;
    }

    private static class ReservationInfo {
        int reservationId;
        String movieTitle;
        int screeningId;
        java.sql.Date screeningDate;
        java.sql.Time startTime;
        java.sql.Time endTime;
        String location;  // 영화관 위치
        List<String> seats = new ArrayList<>();

        public ReservationInfo(int reservationId, String movieTitle, int screeningId, java.sql.Date screeningDate,
                               java.sql.Time startTime, java.sql.Time endTime, String location) {
            this.reservationId = reservationId;
            this.movieTitle = movieTitle;
            this.screeningId = screeningId;
            this.screeningDate = screeningDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }

        public void addSeat(String seat) {
            seats.add(seat);
        }

        public String format() {
            // 날짜와 시간을 원하는 형식으로 변환
            String formattedDate = String.format("%1$tY.%1$tm.%1$td (%1$tA)", screeningDate);
            String formattedStartTime = String.format("%1$tH:%1$tM", startTime);
            String formattedEndTime = String.format("%1$tH:%1$tM", endTime);
            String formattedSeats = String.join(", ", seats);
            String totalPeople = String.format("%d명", seats.size());

            return String.format("[%d] %s\n%s %s ~ %s\n%s / %s\n좌석: %s",
                    reservationId, movieTitle, formattedDate, formattedStartTime, formattedEndTime, location, totalPeople, formattedSeats);
        }
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

    // 기존 메서드와 별개로 오버로드 제공: DB에 cash/credit도 저장
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
                    return rs.getInt(1); // 생성된 reservation_id 반환
                }
            }
        }
        return -1;
    }

    // 환불/취소용 findReservationById 추가
    public Reservation findReservationById(int reservationId, List<Screening> allScreenings) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int screeningId = rs.getInt("screening_id");
                    int cash = rs.getInt("cash");
                    int credit = rs.getInt("credit");
                    // Screening은 추가적으로 조회해서 생성해야 함!
                    Screening screening = allScreenings.stream()
                            .filter(s -> s.getId() == screeningId)
                            .findFirst().orElse(null);
                    return new Reservation(reservationId, screening, 1, null, cash, credit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        return reservationDto;
    }

}
