package org.example;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter {

    public static byte[] imageToByteArray(ImageView imageView) {
        try {
            if (imageView == null) {return null;}
            // Lấy ảnh từ ImageView
            Image image = imageView.getImage();
            if (image == null) {return null;}
            WritableImage writableImage = new WritableImage(
                    (int) image.getWidth(),
                    (int) image.getHeight()
            );

            // Chuyển đổi sang BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Chuyển đổi BufferedImage thành mảng byte
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
