package org.example;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.sql.Timestamp;
import javafx.scene.control.TextArea;
import javafx.scene.control.Control;

public class CommentsDAO {


    // Lấy danh sách bình luận và hiển thị trong List VBox

    public static void addComments(Comments comments) {
        if(!comments.getComment().isEmpty()) {
            // Kết nối cơ sở dữ liệu
            try (Connection conn = DatabaseConnection.connectToLibrary()) {
                // Câu lệnh SQL chèn dữ liệu
                String sql = "INSERT INTO comments (comment_id, book_id, user_id, comment, timestamp) " +
                        "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    // Thiết lập giá trị cho từng tham số trong câu lệnh SQL
                    pstmt.setInt(1, comments.getCommentId()); // comment_id
                    pstmt.setString(2, comments.getBookId()); // book_id
                    pstmt.setString(3, comments.getUserId()); // user_id
                    pstmt.setString(4, comments.getComment()); // comment
                    pstmt.setObject(5, comments.getTimestamp()); // timestamp (LocalDate)

                    // Thực thi câu lệnh SQL
                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                       System.out.println("CommentsDAO ko lỗi");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Lỗi CommentsDAO" + e.getMessage());
            }
        }
    }
    public static void editComment(Integer commentId, String newComment) {
        String sql = "UPDATE comments SET comment = ? WHERE comment_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newComment); // Gán giá trị mới cho nội dung bình luận
            pstmt.setInt(2, commentId);    // Gán giá trị commentId vào câu lệnh SQL
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                Noti.showSuccessMessage("Chỉnh sửa bình luận thành công!");
            } else {
                Noti.showFailureMessage("Không tìm thấy bình luận với ID: " + commentId);
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi khi chỉnh sửa bình luận: " + e.getMessage());
        }
    }
    public static void deleteComment(Integer commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, commentId); // Gán giá trị commentId vào câu lệnh SQL
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                Noti.showSuccessMessage("Xóa bình luận thành công!");
            } else {
                Noti.showFailureMessage("Không tìm thấy bình luận với ID: " + commentId);
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi khi xóa bình luận: " + e.getMessage());
        }
    }

    public static Integer getMaxCommentId() {
        Integer maxCommentId = 0; // Giá trị mặc định nếu không có bản ghi nào
        String sql = "SELECT MAX(comment_id) AS max_id FROM comments";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                maxCommentId = rs.getInt("max_id"); // Lấy giá trị của cột max_id
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi khi truy vấn comment_id lớn nhất: " + e.getMessage());
        }

        return maxCommentId;
    }

    public static VBox createCommentBox(String username, Timestamp timestamp, String commentText) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.TOP_LEFT);

        // Tên người bình luận và thời gian cùng trên một dòng

        // TextField cho tên người dùng
        TextField userField = new TextField(username);
        userField.setEditable(false); // Không cho phép chỉnh sửa
        userField.setStyle("-fx-border-color: transparent;" +
                " -fx-border-width: 1;" +
                " -fx-background-color: white; " +
                "-fx-focus-color: transparent;" +
                " -fx-text-fill: blue;" +
                " -fx-font-weight: bold;" +
                "-fx-focus-color: transparent; " + // Xóa màu focus
                "-fx-faint-focus-color: transparent; " + // Xóa viền mờ khi focus
                " -fx-font-size: 14px;"+
                "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );

        // Thời gian tính từ lúc bình luận đến hiện tại
        LocalDateTime commentTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(commentTime, now);
        String timeAgo = formatDuration(duration);

        // TextField cho thời gian
        TextField timeField = new TextField(timeAgo);
        timeField.setEditable(false); // Không cho phép chỉnh sửa
        timeField.setStyle("-fx-border-color: transparent; -fx-border-width: 1; -fx-background-color: white; " +
                "-fx-focus-color: transparent; -fx-text-fill: gray; -fx-font-size: 16px;");


        // Thêm TextField vào HBox

        // TextArea cho nội dung bình luận
        TextArea commentArea = new TextArea(commentText);
        commentArea.setWrapText(true);
        commentArea.setEditable(false); // Không cho phép chỉnh sửa
        commentArea.setStyle(
                "-fx-border-color: transparent; " +
                        "-fx-background-color: white; " +               // Nền trắng
                        "-fx-focus-color: transparent; " +              // Loại bỏ hiệu ứng xanh focus
                        "-fx-background-insets: 0; " +                  // Loại bỏ khoảng cách viền
                        "-fx-text-fill: black; " +                      // Màu chữ đen
                        "-fx-highlight-fill: transparent; " +           // Loại bỏ màu nền highlight
                        "-fx-highlight-text-fill: black; " +
                        "-fx-focus-color: transparent; " + // Xóa màu focus
                        "-fx-faint-focus-color: transparent; " + // Xóa viền mờ khi focus
                        " -fx-font-size: 16px;"+
                        "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );             // Màu chữ khi chọn


        commentArea.setPrefRowCount(1); // Để TextArea chỉ có 1 dòng mặc định
        commentArea.setWrapText(true);  // Cho phép tự động xuống dòng khi quá chiều rộng

        // Thêm các phần tử vào VBox
        vbox.getChildren().addAll(userField, commentArea, timeField);
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: white;-fx-focus-color: transparent;");
vbox.setMinWidth(1116);
        vbox.setMaxWidth(1116);
        return vbox;
    }

    public static String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        if (days > 0) {
            return days + " ngày trước";
        } else if (hours > 0) {
            return hours + " giờ trước";
        } else {
            return minutes + " phút trước";
        }
    }

    public static List<VBox> getCommentDisplay(String bookId) {
        Comments comments = new Comments();
        List<VBox> commentVBoxes = new ArrayList<>();

        String query = "SELECT comments.comment, comments.timestamp, users.full_name " +
                "FROM comments JOIN users ON comments.user_id = users.user_id " +
                "WHERE comments.book_id = ? ORDER BY comments.timestamp DESC";

        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, bookId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String comment = resultSet.getString("comment");
                    Timestamp timestamp = resultSet.getTimestamp("timestamp");
                    String username = resultSet.getString("full_name");

                    // Tạo VBox cho từng bình luận
                    VBox commentBox = createCommentBox(username, timestamp, comment);
                    commentVBoxes.add(commentBox);
                }
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi: " + e.getMessage());
        }
        return commentVBoxes;
    }

    public static int totalComments(String book_id) {
        String sql = "select count(*) as total_comments from comments\n" +
                "where book_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book_id);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int adjustTextFieldWidth(TextField textField) {
        // Tính toán chiều rộng dựa trên độ dài của nội dung
        String text = textField.getText();
        int length = text.length();
        int charWidth = 10;  // ước tính mỗi ký tự sẽ chiếm khoảng 8px

        // Điều chỉnh chiều rộng theo độ dài văn bản, giới hạn giữa minWidth và maxWidth
        return length * charWidth;
    }
}
