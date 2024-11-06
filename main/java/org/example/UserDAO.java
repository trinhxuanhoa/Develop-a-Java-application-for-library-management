package org.example;
import java.sql.*;

public class UserDAO {

    //thêm
    public void addUser(User user) {
        String sql = "INSERT INTO Users (user_id, full_name, date_of_birth, address, " +
                "phone_number, email, username, password_hash, membership_id, " +
                "join_date, membership_status, total_books_borrowed, overdue_count, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToUsers();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getFullName());
            pstmt.setDate(3, java.sql.Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getUsername());
            pstmt.setString(8, user.getPasswordHash());
            pstmt.setString(9, user.getMembershipId());
            pstmt.setDate(10, java.sql.Date.valueOf(user.getJoinDate()));
            pstmt.setString(11, user.getMembershipStatus());
            pstmt.setInt(12, user.getTotalBooksBorrowed());
            pstmt.setInt(13, user.getOverdueCount());
            pstmt.setString(14, user.getRole());

            pstmt.executeUpdate(); // Thực thi câu lệnh SQL

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //sửa
    public void updateUser(User user) {
        String sql = "UPDATE Users SET full_name = ?, date_of_birth = ?, address = ?, " +
                "phone_number = ?, email = ?, username = ?, password_hash = ?, " +
                "membership_id = ?, join_date = ?, membership_status = ?, " +
                "total_books_borrowed = ?, overdue_count = ?, role = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.connectToUsers();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setDate(2, java.sql.Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getUsername());
            pstmt.setString(7, user.getPasswordHash());
            pstmt.setString(8, user.getMembershipId());
            pstmt.setDate(9, java.sql.Date.valueOf(user.getJoinDate()));
            pstmt.setString(10, user.getMembershipStatus());
            pstmt.setInt(11, user.getTotalBooksBorrowed());
            pstmt.setInt(12, user.getOverdueCount());
            pstmt.setString(13, user.getRole());
            pstmt.setString(14, user.getUserId()); // Sử dụng userId để xác định người dùng cần sửa

            pstmt.executeUpdate(); // Thực thi câu lệnh SQL

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //xoá
    public void deleteUser(String userId) {
    String sql = "DELETE FROM Users WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToUsers();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, userId);
        pstmt.executeUpdate(); // Thực thi câu lệnh SQL

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
