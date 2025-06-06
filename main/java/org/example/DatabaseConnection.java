package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URLL = "jdbc:mysql://127.0.0.1:3308/library"; // URL của cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = "Toilabongdem1$"; // Mật khẩu MySQL

    public static Connection connectToLibrary() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URLL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
        return conn;
    }
}
