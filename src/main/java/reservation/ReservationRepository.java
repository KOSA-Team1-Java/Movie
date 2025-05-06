package reservation;

import movie.Screening;

import java.sql.*;
import java.util.*;

import static util.ConnectionConst.*;

public class ReservationRepository {

    // 예매(Reservation) insert. 생성된 ID 반환
    public int insertReservation(Connection conn, String memberLoginId, int screeningId) throws SQLException {
        String sql = "INSERT INTO reservation (member_loginId, screening_id) VALUES (?, ?)";
        try {
            // 자동 커밋 비활성화 (트랜잭션을 수동으로 처리)
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, memberLoginId);
                pstmt.setInt(2, screeningId);
                int affectedRows = pstmt.executeUpdate();  // 쿼리 실행

                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int reservationId = rs.getInt(1);  // 생성된 reservation_id 반환
                            conn.commit();  // 커밋
                            return reservationId;
                        }
                    }
                } else {
                    System.out.println("예매 정보 삽입 실패");
                    conn.rollback();  // 실패 시 롤백
                }
            } catch (SQLException e) {
                conn.rollback();  // 예외 발생 시 롤백
                System.err.println("SQL 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결 오류: " + e.getMessage());
            e.printStackTrace();
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
        for (ReservationInfo info : reservationMap.values()) {
            formattedReservations.add(info.format());
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

            return String.format("%s\n%s %s ~ %s\n%s / %s\n좌석: %s",
                    movieTitle, formattedDate, formattedStartTime, formattedEndTime, location, totalPeople, formattedSeats);
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
    // 예매 취소 시 좌석 정보 삭제
    public void deleteSeatsByReservationId(Connection conn, int reservationId) throws SQLException {
        String query = "DELETE FROM reservation_seat WHERE reservation_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
    }

    // 예매 취소 시 예매 정보 삭제
    public void deleteReservationById(Connection conn, int reservationId) throws SQLException {
        String query = "DELETE FROM reservation WHERE reservation_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
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
}
