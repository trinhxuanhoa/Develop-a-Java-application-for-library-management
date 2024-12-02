package org.example;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;

import com.google.gson.JsonArray;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.*;
public class GoogleBooksToSQL {
    public static void main(String[] args) {
        String apiKey = "AIzaSyAnpU3yKVAHE-njl-OWqmqLyq17UKKJaGs";
        String query = "Manga";
        String genre;
        genre = query;
        query = removeAccentsAndSpaces(query);
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + apiKey + "&maxResults=40";

        try {
            // Gọi API Google Books
            JsonObject json = JsonParser.parseReader(new InputStreamReader(new URL(apiUrl).openStream())).getAsJsonObject();
            JsonArray items = json.getAsJsonArray("items");

            // Kết nối cơ sở dữ liệu
            Connection conn = DatabaseConnection.connectToLibrary();

            // SQL chuẩn bị câu lệnh để chèn dữ liệu vào bảng books
            String sql = "INSERT INTO books (id, title, author, publisher, year, genre, quantity, edition, reprint, price, language, status, summary, qr_code, chapter, pages, downloads, cover_image) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Duyệt qua từng cuốn sách
            for (var item : items) {
                JsonObject book = item.getAsJsonObject().getAsJsonObject("volumeInfo");

                // Lấy các thông tin sách từ API
                String id = book.has("id") ? book.get("id").getAsString() : UUID.randomUUID().toString(); // Tạo UUID nếu không có "id"
                String title = book.has("title") ? book.get("title").getAsString() : "Không có tiêu đề";
                String authors = book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Vô danh";
                String publisher = book.has("publisher") ? book.get("publisher").getAsString() : "";
                Integer year = null;
                if (book.has("publishedDate")) {
                    String publishedDate = book.get("publishedDate").getAsString();
                    // Kiểm tra xem có phải là năm hợp lệ không
                    if (publishedDate.matches("^\\d{4}.*")) { // Chuỗi bắt đầu bằng 4 chữ số
                        year = Integer.parseInt(publishedDate.substring(0, 4));
                    }
                }
                Integer quantity = 10; // Mặc định
                String edition = ""; // Mặc định
                Integer reprint = null; // Mặc định
                Double price = null; // Mặc định
                String language = book.has("language") ? book.get("language").getAsString() : "";
                String status = "available"; // Mặc định
                String summary = book.has("description") ? book.get("description").getAsString() : "";
                byte[] qrCode = book.has("infoLink") ? createQR(book.get("infoLink").getAsString()) : null;
                Double chapter = null; // Không có
                Integer pages = book.has("pageCount") ? book.get("pageCount").getAsInt() : null;
                Integer downloads = 0; // Mặc định

                // Lấy ảnh bìa
                String coverImageUrl = null;
                if (book.has("imageLinks")) {
                    JsonObject imageLinks = book.getAsJsonObject("imageLinks");
                    if (imageLinks.has("thumbnail")) {
                        coverImageUrl = imageLinks.get("thumbnail").getAsString();
                    } else if (imageLinks.has("smallThumbnail")) {
                        coverImageUrl = imageLinks.get("smallThumbnail").getAsString();
                    }
                }

                byte[] coverImage = createImage(coverImageUrl);
                // Kiểm tra xem sách đã có trong cơ sở dữ liệu chưa
                String checkQuery = "SELECT COUNT(*) FROM books WHERE id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, id);
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);

                    // Nếu chưa có sách, thì thêm vào cơ sở dữ liệu
                    if (count == 0) {
                        ps.setString(1, id);
                        ps.setString(2, title);
                        ps.setString(3, authors);
                        ps.setString(4, publisher);
                        setIntOrNull(ps, 5, year);
                        ps.setString(6, genre);
                        ps.setInt(7, quantity);
                        ps.setString(8, edition);
                        setIntOrNull(ps,9, reprint);
                        setDoubleOrNull(ps,10, price);
                        ps.setString(11, language);
                        ps.setString(12, status);
                        ps.setString(13, summary);
                        ps.setBytes(14, qrCode);
                        setDoubleOrNull(ps,15, chapter);
                        setIntOrNull(ps,16, pages);
                        ps.setInt(17, downloads);
                        ps.setBytes(18, coverImage);
                        ps.addBatch(); // Thêm vào batch
                    }
                }

            }

            // Thực thi batch để chèn tất cả dữ liệu
            ps.executeBatch();
            System.out.println("Dữ liệu đã được lưu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String removeAccentsAndSpaces(String input) {
        // Loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");

        // Thay thế khoảng trắng bằng dấu cộng (+)
        result = result.replace(" ", "+");

        return result;
    }

    public static byte[] createQR(String url) {

        ImageView qrImageView = new ImageView();
        if (!url.isEmpty()) {
            try {
                Image qrImage = generateQRCodeImage(url, 200, 200);
                qrImageView.setImage(qrImage);
                return ImageConverter.imageToByteArray(qrImageView);
            } catch (WriterException | IOException ex) {
                ex.printStackTrace();
            }
        }
       return null;
    }

    private static Image generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());

        return new Image(inputStream);
    }

    public static byte[] createImage(String url) {

        ImageView imageView;
        if (url!=null&&!url.isEmpty()) {
            try {
                imageView = generateImageFromUrl(url, 300, 400);
                return ImageConverter.imageToByteArray(imageView);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static ImageView generateImageFromUrl(String imageUrl, double width, double height) throws IOException {
        // Tải ảnh từ URL
        try (InputStream inputStream = new java.net.URL(imageUrl).openStream()) {
            Image image = new Image(inputStream);

            // Tạo ImageView và đặt kích thước
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true); // Giữ tỷ lệ gốc của ảnh

            return imageView;
        }
    }

    public static void setIntOrNull(PreparedStatement statement, int parameterIndex, Integer value) throws SQLException {
        if (value != null) {
            statement.setInt(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, java.sql.Types.INTEGER);
        }
    }

    public static void setDoubleOrNull(PreparedStatement statement, int parameterIndex, Double value) throws SQLException {
        if (value != null) {
            statement.setDouble(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
        }
    }

}
