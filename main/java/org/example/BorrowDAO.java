package org.example;
import java.sql.*;
import java.time.LocalDate;
public class BorrowDAO {

    public static void addBorrow(Borrow borrow) {

            if(remainingQuantity(borrow.getBookId())>0) {
                try (Connection conn = DatabaseConnection.connectToLibrary()) {
                    String sql = "INSERT INTO borrow (user_id, book_id, borrow_date, due_date, return_date, status) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, borrow.getUserId());
                        stmt.setString(2, borrow.getBookId());
                        stmt.setObject(3, borrow.getBorrowDate()); // Sử dụng setObject cho LocalDate
                        stmt.setObject(4, borrow.getDueDate());
                        stmt.setObject(5, borrow.getReturnDate());
                        stmt.setString(6, borrow.getStatus());
                        if (stmt.executeUpdate() > 0) {
                            Noti.showSuccessMessage("Mượn sách thành công");
                            System.out.println(booksBorrowed(borrow.getUserId()));
                            UserDAO.updateBorrowed(borrow.getUserId(), booksBorrowed(borrow.getUserId()));
                            UserDAO.updateReturned(borrow.getUserId(), booksreturnwed(borrow.getUserId()));
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Lỗi khi sql khi thêm mượn sách: " + e.getMessage());
                    Noti.showFailureMessage("Lỗi khi mượn sách");
                }
        } else {
                Noti.showFailureMessage("Sách đã hết rồi");
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

    /*public static Borrow output1borrow() {
        Borrow borrow = new Borrow();
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM borrow WHERE ";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            }
            }
    }*/

    public static boolean returnBook(String userId, String bookId) {

        String sql = "UPDATE borrow b "
                + "SET b.status = ? , b.return_date = ?"
                + "WHERE b.user_id = ? AND b.book_id = ? AND b.status = 'đã mượn'";

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if(isBorrowedAndWithinDueDate(userId, bookId))
                statement.setString(1, "trả đúng hạn");
            else
                statement.setString(1, "trả quá hạn");

            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            statement.setString(3, userId);
            statement.setString(4, bookId);

            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0) {
                    UserDAO.updateBorrowed(userId, booksBorrowed(userId));
                    UserDAO.updateReturned(userId, booksreturnwed(userId));

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// kiểm tra ngay trả có quá hạn ko
        public static boolean isBorrowedAndWithinDueDate(String userId, String bookId) {
            String sql = " SELECT due_date FROM borrow" +
                    " WHERE user_id = ? AND book_id = ? AND status = 'đã mượn' ";

            try (Connection connection = DatabaseConnection.connectToLibrary();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                statement.setString(2, bookId);

             ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        Date dueDate = resultSet.getDate("due_date");
                        if (dueDate != null) {
                            LocalDate today = LocalDate.now();
                            return !today.isAfter(dueDate.toLocalDate());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
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

    public static int totalBooksBorrowed() {
        String sql = "select count(*) as total_books_borrowed from borrow\n" +
                "where status = 'đã mượn'";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
    public static int totalBooksReturned() {
        String sql = "select count(*) as total_books_borrowed from borrow\n" +
                "where status != 'đã mượn'";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static boolean isBorrowed(String bookId, String userId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE book_id = ? AND user_id = ? AND status = 'đã mượn'";
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
    public static boolean isBorrowedOrReturned(String bookId, String userId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE book_id = ? AND user_id = ?";
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

    public static int booksBorrowed(String userId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE user_id = ? AND status = 'đã mượn'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userId);

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
    public static int booksreturnwed(String userId) {
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT COUNT(*) FROM borrow WHERE user_id = ? AND (status = 'trả đúng hạn' OR status = 'trả quá hạn')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userId);

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
