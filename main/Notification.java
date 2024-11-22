package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Date;

public class Notification {

    private LibraryManagement library;

    public Notification(LibraryManagement library) {
        this.library = library;
    }

    // Hiển thị giao diện thông báo
    public Node showNotifications() {
        double width = 1150; 
        double height = 700; 

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        Label headerLabel = new Label("Thông Báo");
        headerLabel.setFont(new Font("Arial", 22));
        headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Lấy và hiển thị danh sách thông báo
        Node notificationsContent = fetchAndDisplayNotifications();

        // Thêm tiêu đề và nội dung thông báo vào container chính
        mainContainer.getChildren().addAll(headerLabel, notificationsContent);

       // cuộn
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true); 
        scrollPane.setPannable(true); // kéo nội dung bằng chuột
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Hiển thị thanh cuộn dọc khi cần
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Không hiển thị thanh cuộn ngang
        scrollPane.setPrefSize(width, height);

        return scrollPane;
    }

    // Lấy và hiển thị danh sách thông báo
    private Node fetchAndDisplayNotifications() {
        String userId = interfaces.userId(); // Lấy mã người dùng từ file
        List<NotificationItem> notifications = new ArrayList<>();

        // Lấy thông báo từ tất cả các nguồn
        notifications.addAll(getBorrowingNotifications(userId));
        notifications.addAll(getHelpRequestNotifications(userId));
        notifications.addAll(getBookReviewNotifications(userId));

        // Sắp xếp danh sách thông báo
        notifications.sort(Comparator.comparing(NotificationItem::getTimestamp).reversed());

        VBox notificationsBox = new VBox(10);
        notificationsBox.setPadding(new Insets(10));
        notificationsBox.setAlignment(Pos.TOP_LEFT);

        for (NotificationItem notification : notifications) {
            notificationsBox.getChildren().add(createNotificationCard(notification.getMessage()));
        }

        ScrollPane notificationsScrollPane = new ScrollPane(notificationsBox);
        notificationsScrollPane.setFitToWidth(true);
        notificationsScrollPane.setPannable(true);
        notificationsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        notificationsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 
        notificationsScrollPane.setMaxHeight(600);

        return notificationsScrollPane;
    }

    private HBox createNotificationCard(String notification) {
        HBox card = new HBox();
        card.setPadding(new Insets(10));
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(900); 

        // Tạo viền và nền cho thẻ thông báo
        card.setStyle("-fx-border-color: #0080FF; -fx-border-width: 2px; -fx-background-color: #f9f9f9; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label notificationLabel = new Label(notification);
        notificationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        notificationLabel.setWrapText(true); // Cho phép xuống dòng nếu nội dung quá dài

        card.getChildren().add(notificationLabel);
        return card;
    }

    // Lấy thông báo mượn sách
    private List<NotificationItem> getBorrowingNotifications(String userId) {
        List<NotificationItem> notifications = new ArrayList<>();
        String query = """
            SELECT b.title, br.borrow_date
            FROM borrow br
            JOIN books b ON br.book_id = b.id
            WHERE br.user_id = ?
            """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String borrowDate = resultSet.getString("borrow_date");
                System.out.println("Lấy borrowDate: " + borrowDate);

                Date timestamp = parseTimestamp(borrowDate);
                String message = String.format("Bạn đã mượn sách: \"%s\", vào ngày: %s", title, borrowDate);
                notifications.add(new NotificationItem(message, timestamp));
                System.out.println("Đã thêm thông báo: " + message + ", thời gian: " + timestamp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getBorrowingNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        return notifications;
    }

    // Lấy thông báo yêu cầu trợ giúp
    private List<NotificationItem> getHelpRequestNotifications(String userId) {
        List<NotificationItem> notifications = new ArrayList<>();
        String query = """
            SELECT title, request_date
            FROM help_request
            WHERE user_id = ?
            """;

        try (Connection connection = DatabaseConnection.connectToLibrary(); // Dữ liệu nằm trong DB library
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String requestDate = resultSet.getString("request_date");
                System.out.println("Lấy requestDate: " + requestDate);

                Date timestamp = parseTimestamp(requestDate);
                String message = String.format("Bạn đã gửi một yêu cầu trợ giúp: \"%s\", vào ngày: %s", title, requestDate);
                notifications.add(new NotificationItem(message, timestamp));
                System.out.println("Đã thêm thông báo: " + message + ", thời gian: " + timestamp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getHelpRequestNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        return notifications;
    }

    // Lấy thông báo đánh giá sách
    private List<NotificationItem> getBookReviewNotifications(String userId) {
        List<NotificationItem> notifications = new ArrayList<>();
        String query = """
            SELECT b.title, br.rating, br.review_date
            FROM book_reviews br
            JOIN books b ON br.book_id = b.id
            WHERE br.user_id = ?
            """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                double rating = resultSet.getDouble("rating");
                String reviewDate = resultSet.getString("review_date");
                System.out.println("Lấy reviewDate: " + reviewDate);

                Date timestamp = parseTimestamp(reviewDate);
                String message = String.format("Bạn đã đánh giá sách: \"%s\" %.1f sao, vào ngày: %s", title, rating, reviewDate);
                notifications.add(new NotificationItem(message, timestamp));
                System.out.println("Đã thêm thông báo: " + message + ", thời gian: " + timestamp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getBookReviewNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        return notifications;
    }

    // Chuyển chuỗi thời gian thành đối tượng Date
    private Date parseTimestamp(String dateTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (dateTime.contains(":")) {
                return dateTimeFormat.parse(dateTime);
            } else {
                return dateOnlyFormat.parse(dateTime + " 12:00:00");
            }
        } catch (ParseException e) {
            System.err.println("Không thể parse thời gian: " + dateTime);
            e.printStackTrace();
            return new Date();
        }
    }
}
