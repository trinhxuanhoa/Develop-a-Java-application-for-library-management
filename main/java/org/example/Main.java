package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class Main extends Application {
    private Stage primaryStage;
    LibraryManagement library = new LibraryManagement();
    private Scene mainScene;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Library Management");
        Label label = new Label("Enter your name:");
        TextField textField = new TextField();

        // Tạo các nút cho menu
        Button addDocButton = new Button("Add Document");
        Button removeDocButton = new Button("Remove Document");
        Button updateDocButton = new Button("Update Documents");
        Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/Screenshot 2023-11-02 173434.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500); // Kích thước chiều rộng
        imageView.setFitHeight(500); // Kích thước chiều cao
        imageView.setPreserveRatio(true); // Giữ tỷ lệ khung hình
        // Thêm các sự kiện cho nút
        addDocButton.setOnAction(e -> {
            library.addDocument(primaryStage,this);
        });
        removeDocButton.setOnAction(e -> {
            library.removeDocument(primaryStage,this);
        });
        updateDocButton.setOnAction(e -> library.updateDocument(primaryStage,this));

        // Layout đơn giản
        VBox layout = new VBox(10);
        layout.getChildren().addAll(addDocButton, removeDocButton, updateDocButton,label, textField,imageView);

        mainScene = new Scene(layout, 1500, 750);
        primaryStage.setScene( mainScene);
        primaryStage.show();
    }

    public void showMainScene() {
        primaryStage.setScene(mainScene); // Quay lại Scene chính
    }

    public static void main(String[] args) {
        launch(args);
    }
}
