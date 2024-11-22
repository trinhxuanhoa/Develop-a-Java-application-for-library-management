package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;

public class interfaces {
    private Stage primaryStage;
    LibraryManagement library;
    private Scene interfaceScene;
    Help help;

    // Get visual bounds (the portion of the screen not occupied by the taskbar)
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    public interfaces(Stage primaryStage) {
        this.primaryStage = primaryStage;
        library = new LibraryManagement(primaryStage, this);
        help = new Help(library);
    }

    public void interFaceAdmin(Login login) {
        Pane common = new Pane();
        common.setLayoutX(320);
        common.setLayoutY(70);
        common.setPrefWidth(screenWidth - 320);
        common.setPrefHeight(screenHeight - 70);

        Rectangle tableRectangle1 = rectangle(300, screenHeight - 70, Color.rgb(10, 10, 240), Color.WHITE,
                0, 10, 10, 0.8, 0, 70);
        Rectangle tableRectangle2 = rectangle(screenWidth, 68, Color.rgb(0, 0, 240), Color.rgb(45, 45, 240),
                2, 0, 0, 0.8, 0, 0);

        Circle circle = new Circle(50);
        circle.setCenterX(50);
        circle.setCenterY(50);
        Image image = new Image("file:src/main/image/hoa.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        Pane pane = new Pane();
        imageView.setLayoutX(100);
        imageView.setLayoutY(80);
        imageView.setClip(circle);
        Pane paneRectangle = new Pane(tableRectangle2, tableRectangle1);

        HBox accountHBox = accountHBox("Thông Tin Tài Khoản", login);
        Button dashboardButton = button("Bảng Điều Khiển");
        Button managerDocumentButton = button("Quản Lý Tài Liệu");
        Button managerUserButton = button("Quản Lý Người Dùng");
        Button borrowButton = button("Mượn Tài Liệu");
        Button statisticsButton = button("Thống Kê");
        Button cardButton = button("Đăng Kí Thẻ");
        Button renewButton = button("Gia Hạn Thẻ");
        Button notificationsButton = button("Thông Báo");
        Button introduceButton = button("Giới Thiệu");
        Button helpButton = button("Trợ Giúp");
        Button settingsButton = button("Cài Đặt");
        Button logOutButton = button("Đăng Xuất");

        // Event for notificationsButton
        notificationsButton.setOnAction(e -> {
            common.getChildren().clear();
            Notification notification = new Notification(library);
            common.getChildren().add(notification.showNotifications());
        });


        managerDocumentButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(library.managerDocument());
        });
        managerUserButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(library.managerUser());
        });
        borrowButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(library.showBooks());
        });
        statisticsButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(library.statistical(userId()));
        });
        logOutButton.setOnAction(e -> showLogoutConfirmation(login));

        helpButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(help.showHelpOptions());
        });

        VBox layout = new VBox(2);
        layout.getChildren().addAll(dashboardButton, notificationsButton, managerDocumentButton,
                managerUserButton, borrowButton, statisticsButton, cardButton,
                renewButton, introduceButton, helpButton, settingsButton, logOutButton
        );
        layout.setLayoutY(240);

        pane.getChildren().addAll(imageView, accountHBox, layout);

        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(paneRectangle, pane, common);

        AnchorPane.setTopAnchor(common, 70.0);
        AnchorPane.setLeftAnchor(common, 320.0);

        interfaceScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(interfaceScene);
        primaryStage.setTitle("Admin Interface");
        primaryStage.show();
    }

    public void showInterfaceScene() {
        primaryStage.setScene(interfaceScene); // Return to the main Scene
    }

    public Rectangle rectangle(double width, double High, Color fillColor, Color strokeColor,
                               int strokeWidth, int arcWidth, int arcHeight, double opacity, int x, int y) {
        Rectangle rectangle0 = new Rectangle(width, High);
        rectangle0.setFill(fillColor);
        rectangle0.setStroke(strokeColor);
        rectangle0.setStrokeWidth(strokeWidth);
        rectangle0.setArcWidth(arcWidth);
        rectangle0.setArcHeight(arcHeight);
        rectangle0.setOpacity(opacity);
        rectangle0.setLayoutX(x);
        rectangle0.setLayoutY(y);

        return rectangle0;
    }

    Button button(String s) {
        Button button0 = new Button(s);
        button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        button0.setMinHeight(30);
        button0.setMinWidth(300);
        button0.setOnMouseEntered(e -> button0.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        button0.setOnMouseExited(e -> button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));
        return button0;
    }

    HBox accountHBox(String s, Login login) {
        Label accountLabel = new Label(s);
        accountLabel.setStyle("-fx-text-fill: white;-fx-font-size: 15px;");
        accountLabel.setOnMouseEntered(e -> accountLabel.setStyle("-fx-text-fill: white; -fx-underline: true;-fx-font-size: 15px;"));
        accountLabel.setOnMouseExited(e -> accountLabel.setStyle("-fx-text-fill: white; -fx-underline: false;-fx-font-size: 15px;"));
        HBox accountHBox = new HBox(accountLabel);
        accountHBox.setAlignment(Pos.CENTER);
        accountHBox.setMinWidth(300);
        accountHBox.setLayoutY(0);
        accountHBox.setLayoutY(185);
        accountLabel.setOnMouseClicked(e -> {
            library.showUser(userId(), login);
        });
        return accountHBox;
    }

    public static String userId() {
        String userId = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("user_id.txt"))) {
            userId = reader.readLine();
            System.out.println("Đã đọc ID từ file: " + userId);
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }

        return userId;
    }

    private void showLogoutConfirmation(Login main) {
        Stage logoutStage = new Stage();
        logoutStage.initModality(Modality.APPLICATION_MODAL);
        logoutStage.setTitle("Xác Nhận Đăng Xuất");

        Label messageLabel = new Label("Bạn có chắc chắn muốn đăng xuất không?");
        messageLabel.setStyle("-fx-text-fill: red;-fx-font-size: 15px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Xác Nhận");
        confirmButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        confirmButton.setOnAction(e -> {
            logoutStage.close();
            main.showLoginScene();
        });

        VBox layout = new VBox(10, messageLabel, confirmButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 150);
        logoutStage.setScene(scene);
        logoutStage.showAndWait();
    }
}
