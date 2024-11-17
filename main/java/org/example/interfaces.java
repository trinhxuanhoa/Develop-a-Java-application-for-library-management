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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class interfaces {
    private Stage primaryStage;
    LibraryManagement library;
    private Scene interfaceScene;

    // Lấy kích thước visual bounds (phần hiển thị không bị che bởi Taskbar/Dock)
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    public interfaces(Stage primaryStage) {
        this.primaryStage = primaryStage;
        library = new LibraryManagement(primaryStage, this);
    }
    public void interFaceAdmin(Login main) {
        Pane common = new Pane();
        common.setLayoutX(350);
        common.setLayoutY(15);

        Rectangle tableRectangle1 = rectangle(300,screenHeight-70,Color.rgb(10, 10, 240),Color.WHITE,
                0,10,10,0.8,0,70);
        Rectangle tableRectangle2 = rectangle(screenWidth,68,Color.rgb(0, 0, 240),Color.rgb(45, 45, 240),
                2,0,0,0.8,0,0);

        Circle circle = new Circle(50);
        circle.setCenterX(50);
        circle.setCenterY(50);
        Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/hoa.jpg"); // Đường dẫn đến hình ảnh
        ImageView imageView = new ImageView(image);
        // Đặt kích thước cho ImageView
        imageView.setFitWidth(100); // Kích thước chiều rộng
        imageView.setFitHeight(100); // Kích thước chiều cao
        Pane pane = new Pane();
        imageView.setLayoutX(100);
        imageView.setLayoutY(80);
        // Cắt hình ảnh thành hình tròn
        imageView.setClip(circle);
        Pane paneRectangle = new Pane(tableRectangle2,tableRectangle1);

        // Tạo các nút cho menu
HBox accountHBox =  accountHBox("Thông Tin Tài Khoản");
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

    //dashboardButton.setOnAction(e->);

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
            common.getChildren().add(library.showBooks());
        });
        statisticsButton.setOnAction(e->{
            common.getChildren().clear();
            common.getChildren().add(library.statistical());
        });
        logOutButton.setOnAction(e-> showLogoutConfirmation(main));
    // Layout
        VBox layout = new VBox(2);
        layout.getChildren().addAll(dashboardButton, notificationsButton,managerDocumentButton,
                managerUserButton, borrowButton,statisticsButton,cardButton,
                renewButton, introduceButton, helpButton,settingsButton, logOutButton
                                    );
        layout.setLayoutY(240);

        pane.getChildren().addAll(imageView,accountHBox,layout,common);
        // Dùng StackPane để xếp hình nền, hình chữ nhật và form
        StackPane root = new StackPane();
        root.getChildren().addAll(paneRectangle,pane);

    interfaceScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(interfaceScene);
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

   Button button(String s) {
       Button button0 = new Button(s);
       button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
       button0.setMinHeight(30);
       button0.setMinWidth(300);
       button0.setOnMouseEntered(e-> button0.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
       button0.setOnMouseExited(e-> button0.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));
       return button0;
   }

   HBox accountHBox(String s) {
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
           library.showUser(userId());
       });
       return accountHBox;
   }
    public static String userId() {
        String userId = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\user_id.txt"))) {
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
