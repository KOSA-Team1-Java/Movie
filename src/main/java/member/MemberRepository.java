package member;

import exception.MovieException;
import util.PasswordHasher;

import java.sql.*;

import static util.ConnectionConst.*;

public class MemberRepository {

    public void save(Member member) {
        String sql = "INSERT INTO member (loginId, password, name, age) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getLoginId());
            pstmt.setString(2, PasswordHasher.hash(member.getPassword()));
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());

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
                return new Member(id, password, name, age);
            }
        } catch (SQLException e) {
            throw new MovieException(e);
        }
        return null;
    }
}