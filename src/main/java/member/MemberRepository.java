package member;

import exception.MovieException;
import util.PasswordHasher;

import java.sql.*;

import static util.ConnectionConst.*;

public class MemberRepository {

    public void save(Member member) {
        String sql = "INSERT INTO member (loginId, password, name, age, cash, credit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getLoginId());
            pstmt.setString(2, PasswordHasher.hash(member.getPassword()));
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setInt(5, member.getCash());
            pstmt.setInt(6, member.getCredit());

            pstmt.executeUpdate();
            System.out.println("successfully signup");
        } catch (SQLException e) {
            throw new MovieException(e);
        }
    }

    public Member findById(String loginId) {
        String sql = "SELECT * FROM member WHERE loginid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString("loginid");
                String password = rs.getString("password");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                int cash = rs.getInt("cash");
                int credit = rs.getInt("credit");
                return new Member(id, password, name, age, cash, credit);
            }
        } catch (SQLException e) {
            throw new MovieException(e);
        }
        return null;
    }

    public Member updateBudget(Member member) {
        String sql = "UPDATE member SET cash = ?, credit =? WHERE loginid = ?";
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getCash());
            pstmt.setInt(2, member.getCredit());
            pstmt.setString(3, member.getLoginId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MovieException(e);
        }
        return member;
    }
}