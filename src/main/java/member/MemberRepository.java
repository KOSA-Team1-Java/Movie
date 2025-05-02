package member;

import exception.MovieException;

import java.sql.*;

import static JDBC.ConnectionConst.*;

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
            throw new MovieException("회원가입 오류 입니다.");
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
                int budget = rs.getInt("budget");
                Member member = new Member(id, password, name, age);
                member.setBudget(budget);
                return member;
            }
        } catch (SQLException e) {
            throw new MovieException(e);
        }
        return null;
    }
    public void updateBudget(Member member) {
        String sql = "UPDATE member SET budget = ? WHERE loginid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getBudget());
            pstmt.setString(2, member.getLoginId());

            pstmt.executeUpdate();
            System.out.println("💰 예산이 성공적으로 갱신되었습니다.");
        } catch (SQLException e) {
            throw new MovieException(e);
        }
    }
}