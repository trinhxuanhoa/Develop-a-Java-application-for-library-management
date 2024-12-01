package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class RenewCard {
    private LibraryManagement library;
    private Stage primaryStage;
    private LocalDate currentExpiryDate;
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    public RenewCard(LibraryManagement library, Stage primaryStage) {
        this.library = library;
        this.primaryStage = primaryStage;
        this.currentExpiryDate = LocalDate.now();
    }

    public Node showRenewCardInterface() {
        double containerWidth = 500;
        double containerHeight = 400;

        Rectangle rectangle = library.rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, -20 );

        Pane pane = new Pane();
        Label registerLabel = new Label("Đăng Ký Thẻ Thư Viện");
        registerLabel.setFont(new Font("Arial", 30));
        registerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPrefSize(containerWidth, containerHeight);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #e6f2ff); " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-border-color: #b0c4de; -fx-border-width: 2px;");
        mainContainer.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.25)));
        mainContainer.setLayoutX(300);
        mainContainer.setLayoutY(100);

        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER);

        Label renewLabel = new Label("Gia Hạn Thẻ Thư Viện");
        renewLabel.setFont(new Font("Arial", 24));
        renewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        // titleBox.getChildren().addAll(icon, renewLabel);
        titleBox.getChildren().add(renewLabel);

        // Lấy thông tin người dùng
        Card card = getCard(Createinterface.userId());
        String cardId = card.getCardId();
        String userId = card.getUserId();
        String userName = card.getFullName();
        String cardType =  card.getTypeCard();
        String cardRegistrationDate = (card.getCardRegistrationDate()!=null)?card.getCardRegistrationDate().toString():"";
        String expiryDate= (card.getExpiryDate()!=null)?card.getExpiryDate().toString():"";
        String status = card.getStatus();

        // Tạo GridPane để sắp xếp thông tin người dùng
        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setHgap(20);
        userInfoGrid.setVgap(15);
        userInfoGrid.setAlignment(Pos.CENTER_LEFT);

        // Nhãn
        Label cardIdLabel = new Label("Mã Thẻ:");
        cardIdLabel.setFont(Font.font("Arial", 14));
        cardIdLabel.setStyle("-fx-font-weight: bold;");

        Label userIdLabel = new Label("ID Người Dùng:");
        userIdLabel.setFont(Font.font("Arial", 14));
        userIdLabel.setStyle("-fx-font-weight: bold;");

        Label userNameLabel = new Label("Tên Người Dùng:");
        userNameLabel.setFont(Font.font("Arial", 14));
        userNameLabel.setStyle("-fx-font-weight: bold;");

        Label cardTypeLabel = new Label("Loại Thẻ Hiện Tại:");
        cardTypeLabel.setFont(Font.font("Arial", 14));
        cardTypeLabel.setStyle("-fx-font-weight: bold;");

        Label cardRegistrationDateLabel = new Label("Ngày Đăng Kí:");
        cardRegistrationDateLabel.setFont(Font.font("Arial", 14));
        cardRegistrationDateLabel.setStyle("-fx-font-weight: bold;");

        Label expiryDateLabel = new Label("Ngày Hết Hạn Dự Kiến:");
        expiryDateLabel.setFont(Font.font("Arial", 14));
        expiryDateLabel.setStyle("-fx-font-weight: bold;");

        Label statusLabel = new Label("Trạng Thái:");
        statusLabel.setFont(Font.font("Arial", 14));
        statusLabel.setStyle("-fx-font-weight: bold;");

        // Giá trị
        Label cardIdValue = new Label(cardId);
        cardIdValue.setFont(Font.font("Arial", 14));
        cardIdValue.setTextFill(Color.DARKBLUE);

        Label userIdValue = new Label(userId);
        userIdValue.setFont(Font.font("Arial", 14));
        userIdValue.setTextFill(Color.DARKBLUE);

        Label userNameValue = new Label(userName);
        userNameValue.setFont(Font.font("Arial", 14));
        userNameValue.setTextFill(Color.DARKBLUE);

        Label cardTypeValue = new Label(cardType);
        cardTypeValue.setFont(Font.font("Arial", 14));
        cardTypeValue.setTextFill(Color.DARKBLUE);

        Label cardRegistrationDateValue = new Label(cardRegistrationDate.toString());
        cardRegistrationDateValue.setFont(Font.font("Arial", 14));
        cardRegistrationDateValue.setTextFill(Color.DARKBLUE);

        Label expiryDateValue = new Label(expiryDate);
        expiryDateValue.setFont(Font.font("Arial", 14));
        expiryDateValue.setTextFill(Color.DARKBLUE);

        Label satatusValue = new Label(status);
        satatusValue.setFont(Font.font("Arial", 14));
        satatusValue.setTextFill(Color.DARKBLUE);
        // Thêm vào GridPane
        userInfoGrid.add(cardIdLabel, 0, 0);
        userInfoGrid.add(cardIdValue, 1, 0);
        userInfoGrid.add(userIdLabel, 0, 1);
        userInfoGrid.add(userIdValue, 1, 1);
        userInfoGrid.add(userNameLabel, 0, 2);
        userInfoGrid.add(userNameValue, 1, 2);
        userInfoGrid.add(cardTypeLabel, 0, 3);
        userInfoGrid.add(cardTypeValue, 1, 3);
        userInfoGrid.add(expiryDateLabel, 0, 5);
        userInfoGrid.add(expiryDateValue, 1, 5);
        userInfoGrid.add(cardRegistrationDateLabel, 0, 4);
        userInfoGrid.add(cardRegistrationDateValue, 1, 4);
        userInfoGrid.add(statusLabel, 0, 6);
        userInfoGrid.add(satatusValue, 1, 6);

        // Nút Gia Hạn Thẻ
        Button renewButton = new Button("Gia Hạn Thẻ");
        renewButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;");
        renewButton.setPrefWidth(150);
        renewButton.setEffect(new DropShadow(5, Color.GRAY));

        // Hiệu ứng
        renewButton.setOnMouseEntered(e -> renewButton.setStyle("-fx-background-color: #218838; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;"));
        renewButton.setOnMouseExited(e -> renewButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;"));

        // HBox để chứa nút
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(renewButton);

        // Sự kiện khi nhấn nút Gia Hạn Thẻ
        renewButton.setOnAction(e -> {
            if(!RegisterCard.checkCard(userId)) {
                Noti.showInformationMessage("Bạn chưa có thẻ!");
            } else {
                if(!RegisterCard.checkPendingCard(userId)) {
                    if (updateCarDate(userId)) {
                        Noti.showInformationMessage("gia hạn thành công! xin vui lòng chờ duyệt từ admin!");
                        UserDAO.updateUserAttributes(cardId);
                    }
                } else {
                    Noti.showWarningMessage("Thẻ khác của bạn đang chờ duyệt");
                }
                }

        });

        // Thêm các thành phần vào mainContainer
        mainContainer.getChildren().addAll(
                titleBox,
                userInfoGrid,
                buttonBox
        );

        // Wrapper để căn giữa mainContainer
        pane.getChildren().addAll(rectangle, mainContainer);
        pane.setLayoutY(120);
        return pane;
    }

    private Card getCard(String userId) {

        Card card = new Card();
        String sql = "SELECT  * FROM cards c \n" +
                "                JOIN users u ON u.user_id = c.user_id\n" +
                "                WHERE c.user_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                card.setCardId(resultSet.getString("u.membership_id"));
                card.setUserId(resultSet.getString("u.user_id"));
                card.setTypeCard(resultSet.getString("c.card_type"));
                card.setStatus(resultSet.getString("u.membership_status"));
                card.setFullName(resultSet.getString("u.full_name"));
                card.setCardRegistrationDate(getDate(resultSet,"u.card_registration_date"));
                card.setExpiryDate(getDate(resultSet, "u.expiry_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return card;
    }

    private boolean updateCarDate(String userId) {
        String sql = "SELECT card_type FROM cards WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Lấy loại thẻ của người dùng
            statement.setString(1, userId);
            String cardType = null;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cardType = resultSet.getString("card_type");
                }
            }

            if (cardType == null) {
                System.out.println("Không tìm thấy thẻ cho user_id: " + userId);
                return false;  // Nếu không tìm thấy thẻ, trả về false
            }

            // Tính toán ngày hết hạn mới
            LocalDate expiryDate = LocalDate.now();  // Ngày hiện tại
            switch (cardType) {
                case "common":
                    expiryDate = expiryDate.plusMonths(1);  // Thêm 1 tháng
                    break;
                case "plus":
                    expiryDate = expiryDate.plusMonths(2);  // Thêm 2 tháng
                    break;
                case "vip":
                    expiryDate = expiryDate.plusMonths(6);  // Thêm 6 tháng
                    break;
                case "premium":
                    expiryDate = expiryDate.plusYears(1);   // Thêm 1 năm
                    break;
                default:
                    System.out.println("Loại thẻ không hợp lệ.");
                    return false;  // Nếu loại thẻ không hợp lệ, trả về false
            }


            // Cập nhật ngày hết hạn mới và trạng thái thành 'Pending Renewal'
            String sqlUpdate = "UPDATE cards SET expiry_date = ?, status = 'Pending Renewal' WHERE user_id = ?";
            try (PreparedStatement statement2 = connection.prepareStatement(sqlUpdate)) {
                statement2.setDate(1, Date.valueOf(expiryDate));  // Chuyển LocalDate sang java.sql.Date
                statement2.setString(2, userId);
                int rowsUpdated = statement2.executeUpdate();

                return rowsUpdated > 0;  // Nếu có ít nhất 1 bản ghi được cập nhật, trả về true
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            return false;  // Trả về false nếu có lỗi
        }
    }

    public static boolean updateCardStatusToActive(String cardId) {
        // SQL query to update the status of the card
        String sql = "UPDATE cards SET status = 'Active' WHERE card_id = ? AND status = 'Pending Renewal'";

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the userId parameter in the query
            preparedStatement.setString(1, cardId);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            // If rows are affected, the status was successfully updated
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static LocalDate getDate(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(columnName);
        return resultSet.wasNull() ? null : sqlDate.toLocalDate();
    }
    public static void setDate(PreparedStatement pstmt, int parameterIndex, LocalDate date) throws SQLException {
        if (date == null) {
            pstmt.setNull(parameterIndex, java.sql.Types.DATE); // Đẩy lên SQL là null
        } else {
            pstmt.setDate(parameterIndex, Date.valueOf(date)); // Đẩy ngày lên SQL
        }
    }

}