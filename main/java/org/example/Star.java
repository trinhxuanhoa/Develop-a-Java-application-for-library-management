package org.example;

import com.sun.javafx.geom.Shape;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
import javafx.geometry.Bounds;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Point2D;

public class Star{



    public static HBox createRatingHBox(AtomicInteger rating) {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.BOTTOM_LEFT);

        Label ratingLabel = new Label("Đánh giá");
        ratingLabel.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-size: 20px; -fx-font-weight: bold;");
        hbox.getChildren().add(ratingLabel);

        final int numberOfStars = 5;

        for (int i = 0; i < numberOfStars; i++) {
            Polygon star = createStar(15); // Tạo ngôi sao kích thước cố định
            star.setFill(Color.GRAY);

            final int index = i;
            star.setOnMouseClicked(event -> {
                rating.set(index + 1);
                updateStarColors(hbox, rating.get());
            });

            hbox.getChildren().add(star);
        }

        return hbox;
    }

    private static Polygon createStar(double radius) {
        Polygon star = new Polygon();
        final double innerRadius = radius / Math.sin(7 * Math.PI / 10) * Math.sin(Math.PI / 10);

        for (int i = 0; i < 5; i++) {
            // Điểm ngoài
            double outerAngle = Math.PI / 2 + i * 2 * Math.PI / 5;
            star.getPoints().addAll(
                    radius * Math.cos(outerAngle),
                    -radius * Math.sin(outerAngle)
            );

            // Điểm trong
            double innerAngle = outerAngle + Math.PI / 5;
            star.getPoints().addAll(
                    innerRadius * Math.cos(innerAngle),
                    -innerRadius * Math.sin(innerAngle)
            );
        }
        return star;
    }

    public static Polygon star(double radius, Color color) {
        Polygon star = new Polygon();
        final double innerRadius = radius / Math.sin(7 * Math.PI / 10) * Math.sin(Math.PI / 10);

        for (int i = 0; i < 5; i++) {
            // Điểm ngoài
            double outerAngle = Math.PI / 2 + i * 2 * Math.PI / 5;
            star.getPoints().addAll(
                    radius * Math.cos(outerAngle),
                    -radius * Math.sin(outerAngle)
            );

            // Điểm trong
            double innerAngle = outerAngle + Math.PI / 5;
            star.getPoints().addAll(
                    innerRadius * Math.cos(innerAngle),
                    -innerRadius * Math.sin(innerAngle)
            );
        }
        star.setFill(color);
        return star;
    }

    private static void updateStarColors(HBox hbox, int upToIndex) {
        for (int i = 1; i < hbox.getChildren().size(); i++) { // Bỏ qua Label đầu tiên
            Polygon star = (Polygon) hbox.getChildren().get(i);
            star.setFill(i <= upToIndex ? Color.YELLOW : Color.GRAY);
        }
    }

}
