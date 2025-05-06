package member;

import exception.DataAccessException;
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
            throw new DataAccessException(e);
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
            throw new DataAccessException(e);
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
            throw new DataAccessException(e);
        }
        return member;
    }

    public Member updateName(Member member, String newName) {
        String sql = "UPDATE member SET name = ? WHERE loginid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newName);
            pstmt.setString(2, member.getLoginId());
            pstmt.executeUpdate();

            return member; // 변경된 객체를 반환
        } catch (SQLException e) {
            throw new MovieException(e);
        }
    }

    public Member updatePassword(Member member, String newPassword) {
        String sql = "UPDATE member SET password = ? WHERE loginid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 새로운 비밀번호 해시화
            String hashedPassword = PasswordHasher.hash(newPassword);
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, member.getLoginId());

            pstmt.executeUpdate();

            // 비밀번호 변경 후 객체의 비밀번호 갱신
            member.setPassword(hashedPassword); // 객체 내 비밀번호를 갱신
            return member;
        } catch (SQLException e) {
            throw new MovieException(e);
        }
    }




}