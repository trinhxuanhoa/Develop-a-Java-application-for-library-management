package org.example;

import javafx.scene.control.Button;
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

public class interfaces {
    private Stage primaryStage;
    LibraryManagement library = new LibraryManagement();
    private Scene mainScene;;

    // Lấy kích thước visual bounds (phần hiển thị không bị che bởi Taskbar/Dock)
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    public interfaces(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void interFace() {
        Pane common = new Pane();
        common.setLayoutX(370);
        common.setLayoutY(0);

        Rectangle tableRectangle1 = rectangle(300,screenHeight-50,Color.rgb(10, 10, 240),Color.WHITE,
                0,10,10,0.8,0,50);
        Rectangle tableRectangle2 = rectangle(screenWidth,48,Color.rgb(0, 0, 240),Color.rgb(45, 45, 240),
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
        imageView.setLayoutY(70);
        // Cắt hình ảnh thành hình tròn
        imageView.setClip(circle);
        VBox table = new VBox(tableRectangle2,tableRectangle1);

        // Tạo các nút cho menu
    Button addDocButton = button("Thêm Sách");
    Button removeDocButton = button("Xóa Sách");
    Button updateDocButton = button("Sửa Sách");
    Button findDocButton = button("Tìm Sách");
    Button displayDocButton = button("Danh Sách Người Dùng");
    Button addUser = button("Thêm Người Dùng");

    // Thêm các sự kiện cho nút
        addDocButton.setOnAction(e -> {
        library.addDocument(primaryStage,this);
    });
        removeDocButton.setOnAction(e -> {
        library.removeDocument(primaryStage,this);
    });
        updateDocButton.setOnAction(e -> library.updateDocument(primaryStage,this));
        findDocButton.setOnAction(e-> common.getChildren().add(library.findDocument(primaryStage,this)));

    // Layout
        VBox layout = new VBox(2);
        layout.getChildren().addAll(addDocButton,
                                    removeDocButton, updateDocButton,
                                    findDocButton, displayDocButton,
                                    addUser
                                    );
        layout.setLayoutY(200);

        pane.getChildren().addAll(imageView,layout,common);
        // Dùng StackPane để xếp hình nền, hình chữ nhật và form
        StackPane root = new StackPane();
        root.getChildren().addAll(table,pane);

    mainScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(mainScene);
    }

   public void showMainScene() {
       primaryStage.setScene(mainScene); // Quay lại Scene chính
   }
   public Scene sms() {
        return mainScene;
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
       rectangle0.setLayoutX(y);

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
}
