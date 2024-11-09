package org.example;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import java.io.ByteArrayInputStream;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
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

    public static String decodeQRCodeFromImageView(Image image) {

        try {
            // Chuyển Image sang BufferedImage
            BufferedImage bufferedImage = ImageConverter.convertImageToBufferedImage(image);

            // Giải mã mã QR
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Khởi tạo trình giải mã
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);

            return result.getText();
        } catch (NotFoundException e) {
            return null;  // Trả về null nếu không thể giải mã mã QR
        } catch (IOException e) {
            e.printStackTrace();  // Xử lý lỗi nếu có vấn đề khi chuyển đổi hình ảnh
            return null;  // Trả về null nếu có lỗi khi đọc hình ảnh
        }
    }

    // Phương thức chuyển Image thành BufferedImage
    public static BufferedImage convertImageToBufferedImage(Image image) throws IOException {
        // Chuyển đổi Image thành mảng byte
        ImageView  imageView = new ImageView(image);
        byte[] imageData = ImageConverter.imageToByteArray(imageView);

        // Đọc mảng byte thành BufferedImage
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        return ImageIO.read(byteArrayInputStream);
    }

}
