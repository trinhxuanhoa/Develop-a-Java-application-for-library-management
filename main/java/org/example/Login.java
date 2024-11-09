package org.example;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login {

    Stage primaryStage;
    Scene loginScene;

    public void login(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        double screenHeight = visualBounds.getHeight();

        interfaces inf = new interfaces(primaryStage);
        Image backgroundImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/kho-tang-tri-thuc-vu-tru.jpg"); // Đường dẫn ảnh
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(screenWidth); // Đặt kích thước nền
        backgroundImageView.setFitHeight(screenHeight);
        //backgroundImageView.setPreserveRatio(true); // Giữ tỷ lệ ảnh

        // Tạo form đăng nhập
        VBox loginBox = new VBox(15); // Khoảng cách giữa các phần tử là 20
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                "-fx-padding: 600px; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Tạo hình chữ nhật màu trắng với viền và bo tròn
        Rectangle whiteRectangle = new Rectangle(400, 200);
        whiteRectangle.setFill(Color.WHITE); // Màu nền
        whiteRectangle.setStroke(Color.BLACK); // Màu viền
        whiteRectangle.setStrokeWidth(1); // Độ dày viền
        whiteRectangle.setArcWidth(30); // Bán kính bo tròn theo chiều ngang
        whiteRectangle.setArcHeight(30); // Bán kính bo tròn theo chiều dọc
        whiteRectangle.setOpacity(0.8); // Độ trong suốt (tùy chọn)

        // Các thành phần trong form đăng nhập
        Label sign = new Label("Đăng nhập để bắt đầu");
        //sign.setFont(new Font("Arial", 20));
        sign.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;"); // Màu chữ đỏ

        TextField usernameField = new TextField();
        usernameField.setPromptText("Tên đăng nhập ");
        usernameField.setMinHeight(30);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mật khẩu");
        passwordField.setMinHeight(30);

        Button loginButton = new Button("Đăng nhập");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Đặt màu cho nút

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals("admin") && password.equals("12345")) {
                System.out.println("Đăng nhập thành công!");
                inf.interFaceAdmin(this);
            } else {
                System.out.println("Sai tài khoản hoặc mật khẩu!");
                inf.interFaceAdmin(this);
            }
        });

        // Thêm các thành phần vào VBox
        loginBox.getChildren().addAll(sign, usernameField, passwordField, loginButton);

        // Dùng StackPane để xếp hình nền, hình chữ nhật và form
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, whiteRectangle, loginBox);

        // Đặt vị trí của hình chữ nhật và form đăng nhập
        StackPane.setAlignment(whiteRectangle, Pos.CENTER);
        StackPane.setAlignment(loginBox, Pos.CENTER);

        // Tạo Scene và hiển thị Stage
        loginScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Library Manager");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
    }
    public void showLoginScene() {
        primaryStage.setScene(loginScene); // Quay lại scene đăng nhập
    }
}
