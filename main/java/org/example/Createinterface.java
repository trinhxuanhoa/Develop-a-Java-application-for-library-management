package org.example;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.concurrent.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Createinterface {
    private Stage primaryStage;
    LibraryManagement library;
    private Scene interfaceScene;
    private Help help;
    private RenewCard renewCard;
    private RegisterCard registerCard;
    // Lấy kích thước visual bounds (phần hiển thị không bị che bởi Taskbar/Dock)
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    public Createinterface(Stage primaryStage) {
        this.primaryStage = primaryStage;
        library = new LibraryManagement(primaryStage, this);
        help = new Help(library);
        renewCard = new RenewCard(library, primaryStage);
        registerCard = new RegisterCard(library, primaryStage);
    }
    public void interFaceAdmin(Login login) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        StackPane root = new StackPane();
        Pane common = new Pane();
        common.setLayoutX(350);
        common.setLayoutY(15);

        Rectangle tableRectangle1 = rectangle(300,screenHeight-70,Color.rgb(10, 10, 240),Color.WHITE,
                0,10,10,0.8,0,70);
        Rectangle tableRectangle2 = rectangle(screenWidth,68,Color.rgb(0, 0, 240),Color.rgb(45, 45, 240),
                2,0,0,0.8,0,0);
        ImageView imageViewLibrary = new ImageView(new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/library.png"));
        imageViewLibrary.setPreserveRatio(true); // Giữ tỷ lệ ảnh
        imageViewLibrary.setLayoutX(10);
        imageViewLibrary.setLayoutY(0);
        imageViewLibrary.setFitWidth(300);
        imageViewLibrary.setFitHeight(100);

        Label  roleLabel = new Label(UserDAO.getRole(userId()));
        roleLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill: white");

        Circle circle = new Circle(50);
        circle.setCenterX(50);
        circle.setCenterY(50);
        Image image = UserDAO.getAvatar(userId()); // Đường dẫn đến hình ảnh
        ImageView imageView = new ImageView(image);
        // Đặt kích thước cho ImageView
        imageView.setFitWidth(100); // Kích thước chiều rộng
        imageView.setFitHeight(100); // Kích thước chiều cao
        Pane pane = new Pane();
        imageView.setLayoutX(100);
        imageView.setLayoutY(80);
        // Cắt hình ảnh thành hình tròn
        imageView.setClip(circle);


        Circle circle2 = new Circle(20);
        circle2.setCenterX(20);
        circle2.setCenterY(20);
        Image image2 = UserDAO.getAvatar(userId()); // Đường dẫn đến hình ảnh
        // Đặt kích thước cho ImageView
        ImageView imageView2 = new ImageView(image2);
        imageView2.setClip(circle2);
        imageView2.setFitWidth(40); // Kích thước chiều rộng
        imageView2.setFitHeight(40); // Kích thước chiều cao

        Pane paneRectangle = new Pane(tableRectangle2,tableRectangle1);

        // Tạo các nút cho menu
HBox accountHBox =  accountHBox("Thông Tin Tài Khoản", login);
        Button dashboardButton = button("Bảng Điều Khiển","file:C:/Users/Dell/IdeaProjects/library/src/main/icons/speedometer.png");
    Button managerDocumentButton = button("Quản Lý Tài Liệu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/file.png");
    Button managerUserButton = button("Quản Lý Người Dùng", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/management.png");
    Button requestButton =  button("Yêu Cầu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/request.png");
    Button borrowButton = button("Mượn Tài Liệu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/book.png");
        Button statisticsButton = button("Thống Kê", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/analytics.png");
    Button cardButton = button("Đăng Kí Thẻ", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/card.png");
        Button renewButton = button("Gia Hạn Thẻ", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/card2.png");
        Button notificationsButton = button("Thông Báo", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/notification-bell.png");
    Button introduceButton = button("Giới Thiệu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/list.png");
    Button helpButton = button("Trợ Giúp", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/help.png");
        Button settingsButton = button("Cài Đặt", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/gear.png");
    Button logOutButton = button("Đăng Xuất", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/logout.png");

        common.getChildren().clear();
        common.getChildren().add(library.dashboard());

        Task<Void> loadInterfaceTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }
        };

        notificationsButton.setOnAction(e -> {
            common.getChildren().clear();
            Notification notification = new Notification(library);
            common.getChildren().add(notification.showNotifications());
        });

        dashboardButton.setOnAction(e->{
        common.getChildren().clear();
        common.getChildren().add(library.dashboard());
       });
        requestButton.setOnAction(e->{
          common.getChildren().clear();
         common.getChildren().add(library.requestFromUser());
            });
        managerDocumentButton.setOnAction(e-> {
                common.getChildren().clear();
                common.getChildren().add(library.managerDocument());
        });
        managerUserButton.setOnAction(e->{
                common.getChildren().clear();
                common.getChildren().add(library.managerUser());
        });
        borrowButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.showBooks(borrowButton));
        });
        statisticsButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.statistical(Createinterface.userId()));
        });
        cardButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(registerCard.showRegisterCardInterface());

        });
        renewButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(renewCard.showRenewCardInterface());

        });
        helpButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(help.showHelpOptions());
        });
        introduceButton.setOnAction(e-> {
            common.getChildren().clear();
            common.getChildren().add(library.Introduction());
        });
        settingsButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.Setting());
        });
        logOutButton.setOnAction(e-> showLogoutConfirmation(login));
    // Layout
        VBox layout = new VBox(2);
        layout.getChildren().addAll(dashboardButton, notificationsButton, requestButton,
                managerDocumentButton, managerUserButton, borrowButton,statisticsButton,
                cardButton, renewButton, introduceButton, settingsButton, helpButton, logOutButton
                                    );
        layout.setLayoutY(240);

        VBox roleVBox = new VBox(imageView2, roleLabel);
        roleVBox.setAlignment(Pos.CENTER);
        roleVBox.setLayoutY(5);

        pane.getChildren().addAll(imageView,accountHBox,layout,common, imageViewLibrary, roleVBox);
        // Dùng StackPane để xếp hình nền, hình chữ nhật và form

        root.getChildren().addAll(paneRectangle,pane);

    interfaceScene = new Scene(root, screenWidth, screenHeight);
        Platform.runLater(() -> {
            double screenWidth = interfaceScene.getWidth(); // Lấy chiều rộng của Scene
            roleVBox.setLayoutX(screenWidth - roleVBox.getWidth() - 10); // Cách cạnh phải 10px
        });
        primaryStage.setScene(interfaceScene);
        // Đảm bảo đóng ExecutorService sau khi hoàn thành
        loadInterfaceTask.setOnSucceeded(event -> executor.shutdown());
        loadInterfaceTask.setOnFailed(event -> executor.shutdown());
        executor.submit(loadInterfaceTask);
    }
    public void interFaceUser(Login login)  {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        StackPane root = new StackPane();
        Pane common = new Pane();
        common.setLayoutX(350);
        common.setLayoutY(15);

        Rectangle tableRectangle1 = rectangle(300,screenHeight-70,Color.rgb(10, 10, 240),Color.WHITE,
                0,10,10,0.8,0,70);
        Rectangle tableRectangle2 = rectangle(screenWidth,68,Color.rgb(0, 0, 240),Color.rgb(45, 45, 240),
                2,0,0,0.8,0,0);
        ImageView imageViewLibrary = new ImageView(new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/library.png"));
        imageViewLibrary.setPreserveRatio(true); // Giữ tỷ lệ ảnh
        imageViewLibrary.setLayoutX(10);
        imageViewLibrary.setLayoutY(0);
        imageViewLibrary.setFitWidth(300);
        imageViewLibrary.setFitHeight(100);

        Label  roleLabel = new Label(UserDAO.getRole(userId()));
        roleLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill: white");

        Circle circle = new Circle(50);
        circle.setCenterX(50);
        circle.setCenterY(50);
        Image image = UserDAO.getAvatar(userId()); // Đường dẫn đến hình ảnh
        ImageView imageView = new ImageView(image);
        // Đặt kích thước cho ImageView
        imageView.setFitWidth(100); // Kích thước chiều rộng
        imageView.setFitHeight(100); // Kích thước chiều cao
        Pane pane = new Pane();
        imageView.setLayoutX(100);
        imageView.setLayoutY(80);
        // Cắt hình ảnh thành hình tròn
        imageView.setClip(circle);


        Circle circle2 = new Circle(20);
        circle2.setCenterX(20);
        circle2.setCenterY(20);
        Image image2 = UserDAO.getAvatar(userId()); // Đường dẫn đến hình ảnh
        // Đặt kích thước cho ImageView
        ImageView imageView2 = new ImageView(image2);
        imageView2.setClip(circle2);
        imageView2.setFitWidth(40); // Kích thước chiều rộng
        imageView2.setFitHeight(40); // Kích thước chiều cao

        Pane paneRectangle = new Pane(tableRectangle2,tableRectangle1);

        // Tạo các nút cho menu
        HBox accountHBox =  accountHBox("Thông Tin Tài Khoản", login);
        Button dashboardButton = button("Bảng Điều Khiển","file:C:/Users/Dell/IdeaProjects/library/src/main/icons/speedometer.png");
        Button borrowButton = button("Mượn Tài Liệu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/book.png");
        Button statisticsButton = button("Thống Kê", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/analytics.png");
        Button cardButton = button("Đăng Kí Thẻ", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/card.png");
        Button renewButton = button("Gia Hạn Thẻ", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/card2.png");
        Button notificationsButton = button("Thông Báo", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/notification-bell.png");
        Button introduceButton = button("Giới Thiệu", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/list.png");
        Button helpButton = button("Trợ Giúp", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/help.png");
        Button logOutButton = button("Đăng Xuất", "file:C:/Users/Dell/IdeaProjects/library/src/main/icons/logout.png");

        common.getChildren().clear();
        common.getChildren().add(library.dashboard());

        Task<Void> loadInterfaceTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }
        };

        notificationsButton.setOnAction(e -> {
            common.getChildren().clear();
            Notification notification = new Notification(library);
            common.getChildren().add(notification.showNotifications());
        });

        dashboardButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.dashboard());
        });
        borrowButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.showBooks(borrowButton));
        });
        statisticsButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.statistical(Createinterface.userId()));
        });
        cardButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(registerCard.showRegisterCardInterface());

        });
        renewButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(renewCard.showRenewCardInterface());

        });
        helpButton.setOnAction(e -> {
            common.getChildren().clear();
            common.getChildren().add(help.showHelpOptions());
        });
        introduceButton.setOnAction(e-> {
            common.getChildren().clear();
            common.getChildren().add(library.Introduction());
        });
        logOutButton.setOnAction(e-> showLogoutConfirmation(login));
        // Layout
        VBox layout = new VBox(2);
        layout.getChildren().addAll(dashboardButton, notificationsButton, borrowButton,statisticsButton,
                cardButton, renewButton, introduceButton, helpButton, logOutButton
        );
        layout.setLayoutY(240);

        VBox roleVBox = new VBox(imageView2, roleLabel);
        roleVBox.setAlignment(Pos.CENTER);
        roleVBox.setLayoutY(5);

        pane.getChildren().addAll(imageView,accountHBox,layout,common, imageViewLibrary, roleVBox);
        // Dùng StackPane để xếp hình nền, hình chữ nhật và form

        root.getChildren().addAll(paneRectangle,pane);

        interfaceScene = new Scene(root, screenWidth, screenHeight);
        Platform.runLater(() -> {
            double screenWidth = interfaceScene.getWidth(); // Lấy chiều rộng của Scene
            roleVBox.setLayoutX(screenWidth - roleVBox.getWidth() - 10); // Cách cạnh phải 10px
        });
        primaryStage.setScene(interfaceScene);
        // Đảm bảo đóng ExecutorService sau khi hoàn thành
        loadInterfaceTask.setOnSucceeded(event -> executor.shutdown());
        loadInterfaceTask.setOnFailed(event -> executor.shutdown());
        executor.submit(loadInterfaceTask);
    }

   public void showInterfaceScene() {
       primaryStage.setScene(interfaceScene); // Quay lại Scene chính
   }

   public Rectangle rectangle(double width,double High, Color fillColor, Color strokeColor,
                              int strokeWidth,int arcWidth, int arcHeight, double opacity, int x, int y) {
       Rectangle rectangle0 = new Rectangle(width, High); // kích thước
       rectangle0.setFill(fillColor); // Màu nền
       rectangle0.setStroke(strokeColor); // Màu viền
       rectangle0.setStrokeWidth(strokeWidth); // Độ dày viền
       rectangle0.setArcWidth(arcWidth); // Bán kính bo tròn theo chiều ngang
       rectangle0.setArcHeight(arcHeight); // Bán kính bo tròn theo chiều dọc
       rectangle0.setOpacity(opacity); // Độ trong suốt (tùy chọn)
       //StackPane.setAlignment(rectangle0, p); // vị trí
       rectangle0.setLayoutX(x);
       rectangle0.setLayoutY(y);

       return rectangle0;
   }

    Button button(String s, String i) {
        ImageView imageViewButton = imageViewButton(i);
        Button button0 = new Button(s);
        button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: normal;");
        button0.setMinHeight(30);
        button0.setMinWidth(300);

        button0.setGraphic(imageViewButton); // Thêm hình ảnh vào nút
        button0.setContentDisplay(ContentDisplay.LEFT); // Đặt hình ảnh bên trái chữ

        button0.setPadding(new Insets(0, 0, 0, 50)); // Padding trái 100px
        button0.setAlignment(Pos.BASELINE_LEFT); // Đặt căn trái cho nội dung của nút
        button0.setOnMouseEntered(e -> button0.setStyle("-fx-background-color: #004C99; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;"));
        button0.setOnMouseExited(e -> button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: normal;"));
        return button0;
    }

    ImageView imageViewButton(String s) {
        Image image = new Image(s); // Đảm bảo đường dẫn đúng
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);  // Đặt chiều rộng cho ảnh
        imageView.setFitHeight(30); // Đặt chiều cao cho ảnh
        return imageView;
    }

    HBox accountHBox(String s, Login login) {
       Label accountLabel = new Label(s);
       accountLabel.setStyle("-fx-text-fill: white;-fx-font-size: 15px;"); // Màu chữ
       // Thêm sự kiện di chuột để gạch chân
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

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\user_id.txt"))) {
            userId = reader.readLine();  // Đọc dòng đầu tiên (chỉ một dòng)
            System.out.println("Đã đọc ID từ file: " + userId);
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }

        return userId;  // Trả về ID đã đọc (hoặc null nếu file rỗng)
    }
    // Hàm hiển thị cửa sổ xác nhận đăng xuất
    private void showLogoutConfirmation(Login main) {
        // Tạo một stage mới cho cửa sổ xác nhận
        Stage logoutStage = new Stage();
        logoutStage.initModality(Modality.APPLICATION_MODAL); // Chặn thao tác với các cửa sổ khác
        logoutStage.setTitle("Xác Nhận Đăng Xuất");

        // Label hiển thị thông báo
        Label messageLabel = new Label("Bạn có chắc chắn muốn đăng xuất không?");
messageLabel.setStyle("-fx-text-fill: red;-fx-font-size: 15px; -fx-font-weight: bold;");
        // Nút xác nhận đăng xuất
        Button confirmButton = new Button("Xác Nhận");
        confirmButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        confirmButton.setMinHeight(15);
        confirmButton.setMinWidth(50);
        confirmButton.setOnMouseEntered(e-> confirmButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        confirmButton.setOnMouseExited(e-> confirmButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        confirmButton.setOnAction(e -> {
            logoutStage.close();
            main.showLoginScene();
        }); // Đóng cửa sổ khi nhấn xác nhận

        // Bố trí các thành phần
        VBox layout = new VBox(10, messageLabel, confirmButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Tạo scene cho cửa sổ xác nhận
        Scene scene = new Scene(layout, 400, 100);
        logoutStage.setScene(scene);

        // Hiển thị cửa sổ xác nhận
        logoutStage.showAndWait(); // Đợi người dùng đóng cửa sổ này trước khi tiếp tục
    }
}
