package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
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

    public static void showInformationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  // Kiểu thông báo thông thường
        alert.setTitle("Thông tin");
        alert.setHeaderText(null);  // Không có tiêu đề
        alert.setContentText(message);  // Nội dung thông báo

        alert.show();  // Hiển thị thông báo
    }

    public static void showFormattedMessage(String top, String messageBottom) {
        Alert alert = new Alert(AlertType.INFORMATION);

        // Set the title to the 'top' string
        alert.setHeaderText(top);  // This replaces the default "Message" with the 'top' string

        // Tạo phần văn bản
        Text textBottom = new Text(messageBottom);
        textBottom.setFont(Font.font("System", FontWeight.BOLD, 12));

        // Tạo một VBox để chứa cả hai phần văn bản
        VBox vbox = new VBox(10); // Khoảng cách giữa hai dòng là 10
        vbox.setPadding(new Insets(20)); // Khoảng cách từ nội dung đến viền
        vbox.getChildren().add(textBottom);

        // Thiết lập nội dung và kích thước cho cửa sổ thông báo
        alert.getDialogPane().setContent(vbox);
        alert.getDialogPane().setPrefSize(300, 150); // Thay đổi kích thước theo ý muốn

        alert.showAndWait();
    }

    public static void showWarningMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);  // Thay đổi kiểu cảnh báo
        alert.setTitle("Cảnh báo");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show(); // Hiển thị thông báo
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
