package org.example;
import java.sql.*;

public class BookReviewsDAO {

    public static void add(BookReviews bookReviews) {
        if(!hasUserReviewed(bookReviews.getBookId(), bookReviews.getUserId())) {
            String sql = "INSERT INTO book_reviews (book_id, user_id, rating, review_date) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.connectToLibrary()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, bookReviews.getBookId());
                    stmt.setString(2, bookReviews.getUserId());
                    stmt.setDouble(3, bookReviews.getRating());
                    stmt.setTimestamp(4, Timestamp.valueOf(bookReviews.getReviewDate().atStartOfDay()));  // chuyển LocalDate thành Timestamp

                    if (stmt.executeUpdate() > 0)
                        Noti.showSuccessMessage("Đánh giá thành công");
                }
            } catch (SQLException e) {
                Noti.showFailureMessage("Lỗi khi đánh giá" + e.getMessage());
            }
        } else
            Noti.showFailureMessage("bạn đã đánh giá rồi");
    }

    public static void update(BookReviews bookReviews) {
        String sql = "UPDATE book_reviews SET book_id = ?, user_id = ?, rating = ?, review_date = ? WHERE review_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookReviews.getBookId());
                stmt.setString(2, bookReviews.getUserId());
                stmt.setDouble(3, bookReviews.getRating());
                stmt.setTimestamp(4, Timestamp.valueOf(bookReviews.getReviewDate().atStartOfDay())); // chuyển LocalDate thành Timestamp
                stmt.setInt(5, bookReviews.getReviewId());

                stmt.executeUpdate();
            }
        }catch (SQLException e) {
           System.out.println("Lỗi" + e.getMessage());
        }
    }

    public static void delete(int reviewId) {
        String sql = "DELETE FROM book_reviews WHERE review_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, reviewId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Lỗi" + e.getMessage());
        }
    }

    public static String getAverageRating(String bookId) {
        String sql = "SELECT AVG(rating) AS average_rating FROM book_reviews WHERE book_id = ?";
        double averageRating = 0;

        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);
                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    averageRating = resultSet.getDouble("average_rating");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

        // Trả về giá trị trung bình dưới dạng String với định dạng 1 chữ số thập phân
        return String.format("%.1f", averageRating > 0 ? averageRating : 0);
    }

    public static boolean hasUserReviewed(String bookId, String userId) {
        String sql = "SELECT COUNT(*) FROM book_reviews WHERE book_id = ? AND user_id = ?";
        boolean hasReviewed = false;

        try (Connection conn = DatabaseConnection.connectToLibrary()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);
                stmt.setString(2, userId);
                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    // Nếu số lượng đánh giá của người dùng với cuốn sách lớn hơn 0, người dùng đã đánh giá
                    hasReviewed = count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

        return hasReviewed;
    }

}
