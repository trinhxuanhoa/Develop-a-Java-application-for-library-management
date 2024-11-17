package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Shape {

    public static HBox startHBox( AtomicInteger rating) {
        // Tạo HBox chứa cả Label và các ngôi sao
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.BOTTOM_LEFT);
        // Tạo Label "Đánh giá" với màu đỏ và font đậm
        Label ratingLabel = new Label("Đánh giá");
        ratingLabel.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Thêm Label vào HBox
        hbox.getChildren().add(ratingLabel);

        // Số lượng ngôi sao cần hiển thị
        int numberOfStars = 5;

        // Thêm các ngôi sao vào HBox
        for (int i = 0; i < numberOfStars; i++) {
            Polygon star = createStar(0, 0);  // Tạo ngôi sao với kích thước 20x20

            // Đặt màu mặc định là xám
            star.setFill(Color.GRAY);

            // Sự kiện di chuột vào
            int index = i;  // Lưu chỉ số của ngôi sao để sử dụng trong sự kiện

            /*star.setOnMouseEntered(e -> {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                highlightStars(hbox, index + 2);  // Tô vàng tất cả ngôi sao đến ngôi sao thứ index
                }));
                timeline.play(); // Chạy Timeline
                });

            // Sự kiện khi di chuột ra

            star.setOnMouseExited(e -> {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                highlightStars(hbox, rating.get());  // Khôi phục màu sắc ngôi sao theo đánh giá hiện tại
                }));
                timeline.play(); // Chạy Timeline
            });*/

            // Sự kiện khi nhấn chuột
            star.setOnMouseClicked(e -> {
                rating.set(index + 2);  // Cập nhật đánh giá khi người dùng nhấn vào ngôi sao
                highlightStars(hbox, rating.get());  // Tô vàng tất cả các ngôi sao đã đánh giá
            });

            hbox.getChildren().add(star);
        }

        return hbox;
    }

    //tạo ngôi sao
    private static Polygon createStar(double centerX, double centerY) {
        Polygon star = new Polygon();
        double R = 15;  // Bán kính ngôi sao
        double r = R/Math.sin(7*Math.PI/10)*Math.sin(Math.PI / 10);
        double x;
        double y;
        // Tính toán các điểm cho ngôi sao
        for (int i = 0; i < 5; i++) {

                double angle = Math.PI / 2 + i * 2 * Math.PI / 5;  // Góc quay cho các điểm
                 x = centerX + R * Math.cos(angle);  // Toạ độ X
                 y = centerY - R * Math.sin(angle);  // Toạ độ Y
                star.getPoints().addAll(x, y);

                angle = Math.PI / 2 + i * 2 * Math.PI / 5 +  Math.PI / 5;  // Góc quay cho các điểm
                 x = centerX + r * Math.cos(angle);  // Toạ độ X
                 y = centerY - r * Math.sin(angle);  // Toạ độ Y
                star.getPoints().addAll(x, y);
        }
        return star;
    }

    // Hàm để tô màu các ngôi sao từ 1 đến index
    private static void highlightStars(HBox hbox, int upToIndex) {
        for (int i = 1; i < hbox.getChildren().size(); i++) {  // Bắt đầu từ 1 để bỏ qua label
            Polygon star = (Polygon) hbox.getChildren().get(i);
            if (i < upToIndex) {
                star.setFill(Color.YELLOW);  // Tô vàng
            } else {
                star.setFill(Color.GRAY);  // Tô xám
            }
        }
    }
}
