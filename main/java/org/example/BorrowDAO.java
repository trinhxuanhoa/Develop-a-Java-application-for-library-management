package org.example;
import java.sql.*;

public class BorrowDAO {

    public static void add(Borrow borrow) {

            if(remainingQuantity(borrow.getBookId())>0) {
                try (Connection conn = DatabaseConnection.connectToLibrary()) {
                    String sql = "INSERT INTO borrow (user_id, book_id, borrow_date, due_date, return_date, status) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, borrow.getUserId());
                        stmt.setString(2, borrow.getBookId());
                        stmt.setObject(3, borrow.getBorrowDate()); // Sử dụng setObject cho LocalDate
                        stmt.setObject(4, borrow.getReturnDate());
                        stmt.setObject(5, borrow.getDueDate());
                        stmt.setString(6, borrow.getStatus());
                        if (stmt.executeUpdate() > 0)
                            Noti.showSuccessMessage("Mượn sách thành công");
                    }
                } catch (SQLException e) {
                    System.err.println("Lỗi khi sql khi thêm mượn sách: " + e.getMessage());
                    Noti.showFailureMessage("Lỗi khi mượn sách");
                }
        } else {
                Noti.showFailureMessage("Sách đã hết rồi");
            }
    }

    public static boolean delete(int borrowId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "DELETE FROM borrow WHERE borrow_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, borrowId);
                return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
            }
        }catch (SQLException e) {
            System.err.println("Lỗi khi sql khi xóa mượn sách: " + e.getMessage());
            return false;
        }
    }

    public static boolean update(Borrow borrow) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "UPDATE borrow SET user_id = ?, book_id = ?, borrow_date = ?, return_date = ?, status = ? WHERE borrow_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, borrow.getUserId());
                stmt.setString(2, borrow.getBookId());
                stmt.setObject(3, borrow.getBorrowDate()); // Sử dụng setObject cho LocalDate
                stmt.setObject(4, borrow.getReturnDate());
                stmt.setString(5, borrow.getStatus());
                stmt.setInt(6, borrow.getBorrowId());
                return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
            }
        }catch (SQLException e) {
            System.err.println("Lỗi khi sql khi sửa mượn sách: " + e.getMessage());
            return false;
        }
    }

    public static boolean returnBook(String userId, String bookId) {

        String sql = "UPDATE borrow b "
                + "SET b.status = ? "
                + "WHERE b.user_id = ? AND b.book_id = ?";
        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "returned");
            statement.setString(2, userId);
            statement.setString(3, bookId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    public static boolean updateQuantity(String bookId,  int quantity) {
        if(quantity>0)
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "UPDATE books SET quantity = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setString(2, bookId);

               return stmt.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật số lượng: " + e.getMessage());
        }
        return false;
    }

    public static boolean isBorrowed(String bookId, String userId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE book_id = ? AND user_id = ? AND status = 'borrowed'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);
                stmt.setString(2, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0; // Nếu có ít nhất 1 bản ghi, trả về true
                    }
                }
            }
        }catch (SQLException e) {
            System.err.println("lỗi khi kiểm tra sách đã mượn: " + e.getMessage());
        }
        return false; // Trả về false nếu không có bản ghi nào hoặc xảy ra lỗi
    }

    public static int downloads(String bookId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE book_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count; // Nếu có ít nhất 1 bản ghi, trả về true
                    }
                }
            }
        }catch (SQLException e) {
            System.err.println("lỗi khi kiểm tra sách đã mượn: " + e.getMessage());
        }
        return 0; // Trả về false nếu không có bản ghi nào hoặc xảy ra lỗi
    }

    public static int remainingQuantity(String bookId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT quantity FROM books WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int quantity = rs.getInt(1);
                        return quantity;
                    }
                }
            }
        }catch (SQLException e) {
            System.err.println("lỗi khi kiểm tra số lượng sách: " + e.getMessage());
        }
        return 0; // Trả về false nếu không có bản ghi nào hoặc xảy ra lỗi
    }
}
