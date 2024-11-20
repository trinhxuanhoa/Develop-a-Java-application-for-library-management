package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class Noti{
    public static void showErrorMessage(String message, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show(); // Hiển thị thông báo

        // Tạo một Timeline để tự động tắt Alert sau 1 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            alert.close(); // Đóng Alert
        }));
        timeline.play(); // Chạy Timeline
    }

    public static void showFailureMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show(); // Hiển thị thông báo

    }

    public static boolean showConfirmationDialog(String title, String header, String content) {
        // Tạo hộp thoại xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(title);       // Tiêu đề hộp thoại
        confirmAlert.setHeaderText(header); // Tiêu đề nhỏ trong hộp thoại
        confirmAlert.setContentText(content); // Nội dung chính của hộp thoại

        // Hiển thị hộp thoại và chờ kết quả
        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
