package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import java.time.LocalDate;
import java.util.concurrent.atomic.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class RegisterCard {
    private LibraryManagement library;
    private Stage primaryStage;
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();
    private String selectedTypeCard = null;
    private StackPane focusedStackPane = null;
    boolean checkPressStackPane = false;
    private FadeTransition fadeIn;
    private ScaleTransition scaleUp;
    AtomicReference<String> atomicTypeCard = new AtomicReference<>("common");
    public RegisterCard(LibraryManagement library, Stage primaryStage) {
        this.library = library;
        this.primaryStage = primaryStage;
    }

    // Phương thức xác định giao diện nào sẽ được hiển thị (đăng ký thẻ hoặc thay đổi loại thẻ)
    public Node showRegisterCardInterface() {

        Rectangle rectangle = library.rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, -20 );

        Pane pane = new Pane();
        Label registerLabel = new Label("Đăng Ký Thẻ Thư Viện");
        registerLabel.setFont(new Font("Arial", 30));
        registerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Image imageCard1 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/common.png");
        Image imageCard2 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/plus.png");
        Image imageCard3 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/vip.png");
        Image imageCard4 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/premium.png");

        Image imageConten1 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/c1.png");
        Image imageConten2 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/c2.png");
        Image imageConten3 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/c3.png");
        Image imageConten4 = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/c4.png");

        Button button = new Button("Đăng Kí");

        StackPane cardType1 = CreateCardType(imageCard1, Color.rgb(255, 228, 225),
                "#D2B48C", pane, imageConten1, "common.txt", "common");
        StackPane cardType2 = CreateCardType(imageCard2, Color.rgb(50, 205, 50),
                "#228B22", pane, imageConten2, "plus.txt", "plus");
        StackPane cardType3 = CreateCardType(imageCard3, Color.rgb(186, 85, 211),
                "#8B008B", pane, imageConten3, "vip.txt", "vip");
        StackPane cardType4 = CreateCardType(imageCard4, Color.rgb(255, 255, 0),
                "#808000", pane, imageConten4, "premium.txt", "premium");
        // Gán sự kiện focus cho các StackPane
        setupStackPanes(primaryStage.getScene(), cardType1, cardType2, cardType3, cardType4);

        // Cài đặt màu nền và bo góc cho nút
        button.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");

        // Hiệu ứng khi di chuột vào
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #0000CD; -fx-text-fill: white; " +
                    "-fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white;" +
                    " -fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");
        });
         String userId = Createinterface.userId();
        button.setOnMouseClicked(e -> {
            if (!checkPendingCard(userId)) {
                if (!checkCard(userId)) {
                    if (focusedStackPane != null && registerNewCard(userId,selectedTypeCard)) {
                        Noti.showInformationMessage("Bạn đã đăng ký thẻ thành công, vui lòng chờ admin duyệt.");
                        UserDAO.updateUserAttributes(getCardId(userId));
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING, "Bạn có muốn tiếp tục?", ButtonType.OK, ButtonType.CANCEL);
                    alert.setHeaderText("Thẻ cũ sẽ bị vô hiệu!");
                    // Đặt cửa sổ của Alert làm cửa sổ con của Stage hiện tại
                    alert.initOwner(primaryStage);

                    // Hiển thị cảnh báo và đợi người dùng nhấn nút
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            System.out.println("Người dùng đã nhấn OK.");
                            deleteCard(userId);
                            if (focusedStackPane != null && registerNewCard(userId, selectedTypeCard))
                                Noti.showInformationMessage("Bạn đã đăng ký thẻ thành công, vui lòng chờ admin duyệt.");
                        } else {
                            System.out.println("Người dùng đã nhấn Cancel.");
                        }
                    });

                }
            } else {
                Noti.showWarningMessage("Thẻ khác của bạn đang chờ duyệt");
            }
        });

        HBox hBox =  new HBox(100, cardType3, cardType4, button);
        hBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox vBoxCard = new VBox(100,new HBox(100, cardType1, cardType2), hBox);
        vBoxCard.setLayoutX(170);
        vBoxCard.setLayoutY(150);

        pane.getChildren().addAll(rectangle,registerLabel, vBoxCard);
        pane.setLayoutY(120);

        return pane;
    }
    // Đăng ký thẻ mới trong cơ sở dữ liệu
    private boolean registerNewCard(String userId, String cardType) {

                try (Connection connection = DatabaseConnection.connectToLibrary()) {
                    String query = "INSERT INTO cards (card_id, user_id, card_type, expiry_date, status) VALUES (?, ?, ?, ?, 'Pending')";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    LocalDate expiryDate;
                    if (cardType.compareTo("common") == 0)
                        expiryDate = LocalDate.now().plusMonths(1);
                    else if (cardType.compareTo("plus") == 0)
                        expiryDate = LocalDate.now().plusMonths(2);
                    else if (cardType.compareTo("vip") == 0)
                        expiryDate = LocalDate.now().plusMonths(6);
                    else if (cardType.compareTo("premium") == 0)
                        expiryDate = LocalDate.now().plusYears(1);
                    else
                        expiryDate = LocalDate.now();


                    // Thiết lập các giá trị cho câu truy vấn
                    preparedStatement.setString(1, userId + cardType); // Tạo card_id tùy chỉnh
                    preparedStatement.setString(2, userId); // user_id
                    preparedStatement.setString(3, cardType); // card_type
                    preparedStatement.setDate(4, java.sql.Date.valueOf(expiryDate)); // expiry_date chuyển sang kiểu java.sql.Date

                    // Thực thi câu lệnh
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                } catch (SQLException e) {
                    System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
                }

        return false;
    }
    // kiểm tra duyệt
    public static boolean checkPendingCard(String userId) {
        String query = "SELECT COUNT(*) FROM cards WHERE user_id = ? AND status IN ('Pending', 'Pending Renewal')";
        try (Connection connection = DatabaseConnection.connectToLibrary(); // Thay thế bằng hàm kết nối DB của bạn
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId); // Thiết lập giá trị cho tham số userId
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Trả về true nếu có ít nhất 1 thẻ đang chờ duyệt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // kiểm tra thẻ có hoạt động không
    public static boolean checkActiveCard(String userId) {
        String query = "SELECT COUNT(*) FROM cards WHERE user_id = ? AND status = 'active'";
        try (Connection connection = DatabaseConnection.connectToLibrary(); // Thay thế bằng hàm kết nối DB của bạn
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId); // Thiết lập giá trị cho tham số userId
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Trả về true nếu có ít nhất 1 thẻ đang chờ duyệt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // kiểm tra có thẻ ko
    public static boolean checkCard(String userId) {
        String query = "SELECT COUNT(*) FROM cards WHERE user_id = ? AND status != 'Pending'";
        try (Connection connection = DatabaseConnection.connectToLibrary(); // Thay thế bằng hàm kết nối DB của bạn
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId); // Thiết lập giá trị cho tham số userId
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Trả về true nếu có ít nhất 1 thẻ đang chờ duyệt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // kiểm tra thẻ có bị khóa hay không
    public static boolean checkLockCard(String userId) {
        String query = "SELECT COUNT(*) FROM cards WHERE user_id = ? AND status = 'locked'";
        try (Connection connection = DatabaseConnection.connectToLibrary(); // Thay thế bằng hàm kết nối DB của bạn
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId); // Thiết lập giá trị cho tham số userId
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Trả về true nếu có ít nhất 1 thẻ đang chờ duyệt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // kiểm thẻ hết hạn hay ko
    public static boolean checkExpiredCard(String userId) {
        String query = "SELECT COUNT(*) FROM cards WHERE user_id = ? AND status != 'Expired'";
        try (Connection connection = DatabaseConnection.connectToLibrary(); // Thay thế bằng hàm kết nối DB của bạn
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId); // Thiết lập giá trị cho tham số userId
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Trả về true nếu có ít nhất 1 thẻ đang chờ duyệt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // duệt
    public static boolean updateCardStatusToActive(String cardId) {
        // SQL query to update the status of the card
        String sql = "UPDATE cards SET status = 'Active' WHERE card_id = ? AND status = 'Pending'";

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
    //kiểm tra trạng thái thẻ
    public static boolean checkStatusCard(String userId) {

        checkExpiredCard(userId);

        String sql = "SELECT membership_status FROM users WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                String currentStatus = resultSet.getString("membership_status");
                System.out.println(currentStatus);
                return currentStatus.compareTo("active")==0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    // giao diện từng thẻ
    public void deleteCard(String userId)  {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "DELETE FROM cards WHERE user_id = " + "'" + userId + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("the đã được xóa thành công!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa the: " + e.getMessage());
        }
    }

    public static String getCardId(String userId) {
        String sql = "select card_id from cards where user_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userId);

            try(ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getString("card_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static boolean checkAndExpireCard(String userId) {
        String sql = "SELECT expiry_date FROM cards WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Lấy ngày hết hạn của thẻ
            statement.setString(1, userId);
            LocalDate expiryDate = null;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    expiryDate = resultSet.getDate("expiry_date").toLocalDate();
                }
            }

            if (expiryDate == null) {
                System.out.println("Không tìm thấy thẻ cho user_id: " + userId);
                return false; // Không tìm thấy thẻ, trả về false
            }

            // So sánh ngày hết hạn với ngày hiện tại
            LocalDate today = LocalDate.now();
            if (today.isAfter(expiryDate)) {
                // Ngày hiện tại sau ngày hết hạn -> Cập nhật trạng thái thành "Expired"
                String sqlUpdate = "UPDATE cards SET status = 'Expired' WHERE user_id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                    updateStatement.setString(1, userId);
                    int rowsUpdated = updateStatement.executeUpdate();
                    UserDAO.updateUserAttributes(getCardId(userId));
                    return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
                }
            } else {
                // Thẻ chưa hết hạn
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }



    private StackPane CreateCardType(Image image, Color cardColor, String buttonColor,
                                     Pane pane, Image contenImage, String fileName, String cardType) {
        // Tạo StackPane để chứa các thành phần
        StackPane stackPane = new StackPane();
        stackPane.setMinSize(300, 150);
        stackPane.setMaxSize(300, 150);

        // Hình chữ nhật nền bo tròn các cạnh
        Rectangle rectangle = new Rectangle(300, 150);
        rectangle.setFill(cardColor);
        rectangle.setArcWidth(20); // Độ bo tròn cạnh
        rectangle.setArcHeight(20);

        // Ảnh
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(50);

        // Nút với hai cạnh dưới bo tròn
        Button button = new Button("Xem thông tin");
        button.setMinSize(300, 30);
        button.setMaxSize(300, 30);
        button.setStyle("-fx-background-color: " + buttonColor + ";"
                + "-fx-background-radius: 0 0 11 11;" // Bo tròn hai góc dưới
                + "-fx-text-fill: white;" // Chữ màu trắng
                + "-fx-font-weight: bold;"// Chữ in đậm
                + "-fx-font-size: 14px;");
        button.setOnAction(e->{
            Node node = pane.getChildren().getLast();
            pane.getChildren().removeLast();
            pane.getChildren().add(notiCard(fileName,contenImage, pane, node));
                });

        // Tạo VBox để sắp xếp các thành phần theo trục dọc
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.setSpacing(30); // Khoảng cách giữa các thành phần
        VBox.setMargin(imageView, new Insets(20, 0, 0, 0)); // Thêm khoảng cách trên cho ảnh

        vBox.getChildren().addAll(imageView, button);

        // Thêm các thành phần vào StackPane
        stackPane.getChildren().addAll(rectangle, vBox);
        String userId = Createinterface.userId();
        stackPane.setOnMouseClicked(event -> {
            selectedTypeCard = cardType; // Cập nhật thẻ được chọn
            focusedStackPane = stackPane; // Cập nhật thẻ đang tập trung
            System.out.println("Thẻ được chọn: " + cardType);
        });

        return stackPane;
    }

    // Hàm tạo hiệu ứng focus sáng lấp lánh và thoát ra
    private void applyFocusEffect(StackPane stackPane) {
        if(focusedStackPane!=null)
            removeFocusEffect(focusedStackPane);
        stackPane.setStyle("-fx-border-color: red; -fx-border-width: 5px;  -fx-border-radius: 11px;");
        // Tạo hiệu ứng lấp lánh cho biên
        /*fadeIn = new FadeTransition(Duration.millis(500), stackPane);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(Timeline.INDEFINITE);
        fadeIn.setAutoReverse(true);*/

        // Tạo hiệu ứng phóng to
        scaleUp = new ScaleTransition(Duration.millis(500), stackPane);
        scaleUp.setFromX(1.0);
        scaleUp.setToX(1.05);
        scaleUp.setFromY(1.0);
        scaleUp.setToY(1.05);
        scaleUp.setCycleCount(Timeline.INDEFINITE);
        scaleUp.setAutoReverse(true);

        // Chạy hiệu ứng lấp lánh và phóng to
        //fadeIn.play();
        scaleUp.play();
    }
    // Hàm loại bỏ hiệu ứng focus
    private void removeFocusEffect(StackPane stackPane) {
        // Loại bỏ biên và hiệu ứng động
        stackPane.setStyle("-fx-border-color: transparent; -fx-border-width: 0px;");

        // Dừng các hiệu ứng nếu chúng đang chạy
        if (fadeIn != null) {
            fadeIn.stop();
        }
        if (scaleUp != null) {
            scaleUp.stop();
        }

        // Đặt lại các thuộc tính của StackPane
        stackPane.setOpacity(1.0);  // Đặt độ mờ lại bình thường
        stackPane.setScaleX(1.0);
        stackPane.setScaleY(1.0);
        stackPane.setEffect(null);  // Loại bỏ mọi hiệu ứng
    }
    // Hàm thêm sự kiện focus vào từng StackPane
    private void addFocusEvents(StackPane stackPane) {
        stackPane.setOnMouseEntered(event -> {
            if (focusedStackPane != stackPane&&!checkPressStackPane) { // Chỉ sáng lên nếu không phải StackPane được focus
                applyFocusEffect(stackPane);
            }
        });

        stackPane.setOnMouseExited(event -> {
            if (focusedStackPane != stackPane&&!checkPressStackPane) { // Chỉ tắt sáng nếu không phải StackPane được focus
                removeFocusEffect(stackPane);
            }
        });

        stackPane.setOnMousePressed(event -> {
            if (focusedStackPane != null) {
                removeFocusEffect(focusedStackPane); // Xóa focus hiệu ứng trên StackPane cũ
            }
            focusedStackPane = stackPane; // Cập nhật StackPane được focus
            applyFocusEffect(stackPane);  // Áp dụng hiệu ứng focus
            checkPressStackPane = true;
        });
    }
    // Hàm setupStackPanes
    private void setupStackPanes(Scene scene, StackPane... stackPanes) {
        for (StackPane stackPane : stackPanes) {
            addFocusEvents(stackPane); // Gán sự kiện focus cho từng StackPane
        }

        // Thêm sự kiện vào Scene để xóa focus khi nhấn ra ngoài
        scene.setOnMousePressed(event -> {
            // Nếu không nhấn vào StackPane nào trong danh sách, bỏ focus
            if (focusedStackPane != null) {
                // Lấy tọa độ chuột và của StackPane hiện tại
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();

                double stackPaneX = focusedStackPane.localToScene(0, 0).getX();
                double stackPaneY = focusedStackPane.localToScene(0, 0).getY();
                double stackPaneWidth = focusedStackPane.getWidth();
                double stackPaneHeight = focusedStackPane.getHeight();

                // Kiểm tra nếu chuột nhấn ra ngoài StackPane hiện tại
                if (mouseX < stackPaneX || mouseX > stackPaneX + stackPaneWidth ||
                        mouseY < stackPaneY || mouseY > stackPaneY + stackPaneHeight) {
                    // Nếu chuột không nằm trong phạm vi StackPane hiện tại, bỏ focus
                    removeFocusEffect(focusedStackPane); // Loại bỏ hiệu ứng focus
                    focusedStackPane = null; // Reset focusedStackPane
                    checkPressStackPane = false;
                }
            }
        });
    }

    private Node notiCard(String fileName, Image image, Pane pane, Node node) {
        // Tạo ImageView để hiển thị hình ảnh
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300);
        imageView.setFitHeight(150);
        // Đọc nội dung từ file
        FileHandler fileHandler = new FileHandler(fileName);
        String conten = fileHandler.readFromFileAsString();

        // Tạo Label và thiết lập cỡ chữ, cho phép xuống dòng
        Label label = new Label(conten);
        label.setWrapText(true); // Cho phép nội dung xuống dòng khi dài
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: black;"); // Cỡ chữ và màu sắc cho Label
        VBox vBoxLabel = new VBox(label);
        vBoxLabel.setMinSize(800,300);
        vBoxLabel.setMaxSize(800,300);

        Button btn = new Button("Thoát");
        // Cài đặt màu nền và bo góc cho nút
        btn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");

        // Hiệu ứng khi di chuột vào
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white;" +
                    " -fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; " +
                    "-fx-font-size: 14px; -fx-background-radius: 20px;-fx-font-weight: bold;");
        });
        btn.setOnAction(e->{
            pane.getChildren().removeLast();
            pane.getChildren().add(node);
        });
        // Tạo VBox để chứa hình ảnh và Label
        VBox vBox = new VBox(20, imageView, label);
        vBox.setMinSize(800, 450);
        vBox.setMaxSize(800, 450);

        vBox.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(50, vBox, btn);
        hBox.setLayoutX(150);
        hBox.setLayoutY(40);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        return hBox;
    }

}
