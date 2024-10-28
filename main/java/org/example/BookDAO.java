package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDAO {

    public static void addBook(String title, String author, String publisher, int year) {
        String sql = "INSERT INTO Books (Title, Author, Publisher, Year) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, year);

            pstmt.executeUpdate();
            System.out.println("Thêm sách thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách: " + e.getMessage());
        }
    }
}
