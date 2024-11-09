package org.example;

import com.google.zxing.WriterException;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.time.LocalDate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.concurrent.atomic.AtomicBoolean;
public class LibraryManagement {

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();
    private Book book;
    BookDAO bookDAO;
    private User user;
    UserDAO userDAO;

    public LibraryManagement() {
        book = null;
        bookDAO = new BookDAO();
        user = null;
        userDAO = new UserDAO();
    }
    // Phương thức thêm sách
    public void addDocument(Stage primaryStage, interfaces main) {

        VBox isbnVBox = vBox("ISBN");
        VBox titleVBox = vBox("Tiêu đề");
        VBox authorVBox = vBox("Tác giả");
        VBox yearVBox = vBox("Năm xuất bản");
        VBox genreVBox = vBox("Thể loại");
        VBox publisherVBox = vBox("Nhà xuất bản");
        VBox quantityVBox = vBox("Số lượng");
        VBox editionVBox = vBox("Phiên bản");
        VBox reprintVBox = vBox("Số lần tái bản");
        VBox priceVBox = vBox("Giá");
        VBox languageVBox = vBox("Ngôn ngữ");
        VBox statusVBox = comboBox("Trạng thái",  new String[] {"available", "not available"});
        VBox chapterVBox = vBox("Số chương");
        genreVBox.setMinWidth(400);
        chapterVBox.setMinWidth(90);
        HBox chapAndGenreHBox = new HBox(10,genreVBox,chapterVBox);
        chapAndGenreHBox.setMaxWidth(500);

        TextField isbnTextField = (TextField)isbnVBox.getChildren().get(1);
        TextField titleField = (TextField)titleVBox.getChildren().get(1);
        TextField authorField = (TextField)authorVBox.getChildren().get(1);
        TextField publisherField = (TextField)publisherVBox.getChildren().get(1);
        TextField yearField = (TextField)yearVBox.getChildren().get(1);
        TextField genreField = (TextField)genreVBox.getChildren().get(1);
        TextField quantityField = (TextField)quantityVBox.getChildren().get(1);
        TextField editionField = (TextField)editionVBox.getChildren().get(1);
        TextField reprintField = (TextField)reprintVBox.getChildren().get(1);
        TextField priceField = (TextField)priceVBox.getChildren().get(1);
        TextField languageField = (TextField)languageVBox.getChildren().get(1);
        ComboBox statusField = (ComboBox)statusVBox.getChildren().get(1);
        TextField chapterField = (TextField)chapterVBox.getChildren().get(1);
        //tạo mã QR
        TextField urlField = new TextField();
        urlField.setPromptText("Nhập đường link...");
        Button generateButton = new Button("Tạo mã QR");
        ImageView qrImageView = new ImageView();
        qrImageView.setFitWidth(200);
        qrImageView.setFitHeight(200);
        generateButton.setOnAction(e -> {
            String url = urlField.getText();
            if (!url.isEmpty()) {
                try {
                    Image qrImage = generateQRCodeImage(url, 200, 200);
                    qrImageView.setImage(qrImage);
                } catch (WriterException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        HBox linkHBox = new HBox(urlField, generateButton);
        VBox qrBox = new VBox(linkHBox, qrImageView);
        qrBox.setLayoutX(1187.5);
        qrBox.setLayoutY(539);
        qrBox.setAlignment(Pos.CENTER);
        //tạo tiêu đề thêm sách
        Label addFeild = new Label("Thêm tài liệu");
        addFeild.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
        addFeild.setLayoutX(70);
        addFeild.setLayoutY(60);
//ô bên noài
        Rectangle whiteRectangle = rectangle(screenWidth-100, screenHeight-100,Color.WHITE,
                Color.BLACK,2,10,10,1,60,50);
//dòng tiêu đề tóm tắt
        Label summaryLabel = new Label("Tóm tắt nội dung:");
        summaryLabel.setLayoutX(70);
        summaryLabel.setLayoutY(480);
        summaryLabel.setStyle("-fx-font-size: 15px;-fx-font-style: italic;-fx-font-family: 'Arial';");
//nội dung
        TextArea textArea = textArea();

        // Tạo một nút để mở hộp thoại chọn file
        Button uploadButton = new Button("Chọn Ảnh");
        uploadButton.setMinWidth(300);
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        ImageView imageView = new ImageView(); // Tạo một ImageView để hiển thị ảnh
        imageView.setFitWidth(300);
        imageView.setFitHeight(400);
        Rectangle imgRectangle = rectangle(225,300,Color.WHITE,Color.BLACK,
                1,0,0,1,0,0);
        VBox imageVBox = new VBox(50, uploadButton, imgRectangle);
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.setLayoutX(1150);
        imageVBox.setLayoutY(95);
        Label codeID = new Label();
        //hoạt động nút tạo qr
        uploadButton.setOnAction(e -> {
            if(isbnTextField.getText().isEmpty())
                Noti.showErrorMessage("Xin hãy nhập mã ISBN trước", primaryStage);
            else {
                FileChooser fileChooser = new FileChooser(); // Khởi tạo FileChooser
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );   // Thiết lập các loại file cho phép chọn
                File selectedFile = fileChooser.showOpenDialog(primaryStage);  // Hiển thị hộp thoại chọn file và lưu file được chọn
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString()); // Tạo đối tượng Image từ file đã chọn
                    if (imageView.getImage() == null) {
                        imageView.setImage(image);
                    imageVBox.getChildren().remove(imgRectangle);
                    imageVBox.setSpacing(0);
                    qrBox.setLayoutX(1150);
                    urlField.setMinWidth(225);
                        codeID.setText("Mã số tài liệu: " + isbnTextField.getText());
                        imageVBox.getChildren().addAll(imageView, codeID);
                    }
                    else {
                        imageView.setImage(image);
                        codeID.setText("Mã số tài liệu: " + isbnTextField.getText());
                        System.out.println(imageVBox.getChildren().size());
                    }
                }
            }
        });

        VBox vBoxLeft = new VBox(15);
        vBoxLeft.getChildren().addAll(titleVBox, authorVBox, publisherVBox,
                yearVBox,chapAndGenreHBox, quantityVBox);
        VBox vBoxRight = new VBox(15);
        vBoxRight.getChildren().addAll(isbnVBox,editionVBox,
              reprintVBox,languageVBox,priceVBox,statusVBox);

        Button deleteButton = new Button("Xóa");
        deleteButton.setLayoutX(1400);
        deleteButton.setLayoutY(580);
        deleteButton.setMinSize(70,40);
        deleteButton.setOnAction(e -> main.showInterfaceScene());
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

       deleteButton.setOnAction(e->{
           isbnTextField.clear();
           titleField.clear();
           authorField.clear();
           publisherField.clear();
           yearField.clear();
           genreField.clear();
           quantityField.clear();
           editionField.clear();
           reprintField.clear();
           priceField.clear();
           languageField.clear();
           chapterField.clear();
           textArea.clear();
           imageView.setImage(null);
           urlField.clear();

           if(imageVBox.getChildren().size()==3)
           imageVBox.getChildren().remove(2);

           if(imageVBox.getChildren().get(1)==imageView)
           imageVBox.getChildren().remove(imageView);

           if(imageVBox.getChildren().size()==1)
           imageVBox.getChildren().add(imgRectangle);
           imageVBox.setSpacing(50);

           qrImageView.setImage(null);
           qrBox.setLayoutX(1187.5);
           urlField.setMinWidth(150);
       });

        addButton.setOnAction(e-> {
            if (isbnTextField.getText().isEmpty())
                Noti.showErrorMessage("ISBN không được để trống", primaryStage);
            else if (titleField.getText().isEmpty())
                Noti.showErrorMessage("Tên sách không được để trống", primaryStage);
            else if (authorField.getText().isEmpty())
                Noti.showErrorMessage("Tên tác giả không được để trống", primaryStage);
            else
                try {
                    // Lấy giá trị từ TextField
                    String isbn = isbnTextField.getText();
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String publisher = publisherField.getText();
                    Integer year = parseInteger(yearField.getText(), "năm không hợp lệ");
                    String genre = genreField.getText();
                    Integer quantity = parseInteger(quantityField.getText(), "số lượng không hợp lệ");
                    String edition = editionField.getText();
                    Integer reprint = parseInteger(reprintField.getText(), "số lần tái bản không hợp lệ");
                    Double price = parseDouble(priceField.getText(), "giá không hợp lệ");
                    String language = languageField.getText();
                    String status = (String) statusField.getValue();
                    //String status = statusField.getText();
                    Double chapter = parseDouble(chapterField.getText(), "số chương không hợp lệ");
                    String description = textArea.getText();

                    // Tạo đối tượng Book
                    Book book = new Book(isbn, title, author, publisher, year, genre, quantity, edition, reprint,
                            price, language, status, chapter, description,
                            ImageConverter.imageToByteArray(qrImageView),
                            ImageConverter.imageToByteArray(imageView));

                    // Thêm vào cơ sở dữ liệu
                    bookDAO.addBook(book);

                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    Noti.showFailureMessage("Lỗi " + ex.getMessage());
                }
        });

        HBox hBox = new HBox(40, vBoxLeft, vBoxRight);
        hBox.setLayoutX(70);
        hBox.setLayoutY(100);
        Pane layout = new Pane(summaryLabel, textArea, addFeild, imageVBox,
                hBox, qrBox, deleteButton, backButton, addButton);
        StackPane root = new StackPane();
        root.getChildren().addAll(whiteRectangle, layout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }
    //phương thức chỉnh sửa sách
    public void updateDocument(Stage primaryStage, interfaces main, Book book) {

        //tạo mã QR
        TextField urlField;
        if(book.getQrCodeImage()!=null)
            urlField = new TextField(ImageConverter.decodeQRCodeFromImageView(book.getQrCodeImage()));
        else
            urlField = new TextField();
        urlField.setPromptText("Nhập đường link...");
        Button generateButton = new Button("Tạo mã QR");
        ImageView qrImageView;
        if(book.getQrCodeImage()!=null)
            qrImageView = new ImageView(book.getQrCodeImage());
        else
            qrImageView = new ImageView(new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/qr1.png"));
        qrImageView.setFitWidth(200);
        qrImageView.setFitHeight(200);
        generateButton.setOnAction(e -> {
            String url = urlField.getText();
            if (!url.isEmpty()) {
                try {
                    Image qrImage = generateQRCodeImage(url, 200, 200);
                    qrImageView.setImage(qrImage);
                } catch (WriterException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        HBox linkHBox = new HBox(urlField, generateButton);
        VBox qrBox = new VBox(linkHBox, qrImageView);
        qrBox.setLayoutX(1150);
        qrBox.setLayoutY(539);
        urlField.setMinWidth(225);
        qrBox.setAlignment(Pos.CENTER);
        //tạo tiêu đề thêm sách
        Label addFeild = new Label("Sửa tài liệu");
        addFeild.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
        addFeild.setLayoutX(70);
        addFeild.setLayoutY(60);

        //ô bên noài
        Rectangle whiteRectangle = rectangle(screenWidth-100, screenHeight-100,Color.WHITE,
                Color.BLACK,2,10,10,1,60,50);
//dòng tiêu đề tóm tắt
        Label summaryLabel = new Label("Tóm tắt nội dung:");
        summaryLabel.setLayoutX(70);
        summaryLabel.setLayoutY(480);
        summaryLabel.setStyle("-fx-font-size: 15px;-fx-font-style: italic;-fx-font-family: 'Arial';");
//nội dung
        TextArea textArea = textArea();

        VBox isbnVBox = vBox("ISBN");
        VBox titleVBox = vBox("Tiêu đề");
        VBox authorVBox = vBox("Tác giả");
        VBox yearVBox = vBox("Năm xuất bản");
        VBox genreVBox = vBox("Thể loại");
        VBox publisherVBox = vBox("Nhà xuất bản");
        VBox quantityVBox = vBox("Số lượng");
        VBox editionVBox = vBox("Phiên bản");
        VBox reprintVBox = vBox("Số lần tái bản");
        VBox priceVBox = vBox("Giá");
        VBox languageVBox = vBox("Ngôn ngữ");
        VBox statusVBox = comboBox("Trạng thái",  new String[] {"available", "not available"});
        VBox chapterVBox = vBox("Số chương");
        genreVBox.setMinWidth(400);
        chapterVBox.setMinWidth(90);
        HBox chapAndGenreHBox = new HBox(10,genreVBox,chapterVBox);
        chapAndGenreHBox.setMaxWidth(500);

        TextField isbnField = (TextField)isbnVBox.getChildren().get(1);
        TextField titleField = (TextField)titleVBox.getChildren().get(1);
        TextField authorField = (TextField)authorVBox.getChildren().get(1);
        TextField publisherField = (TextField)publisherVBox.getChildren().get(1);
        TextField yearField = (TextField)yearVBox.getChildren().get(1);
        TextField genreField = (TextField)genreVBox.getChildren().get(1);
        TextField quantityField = (TextField)quantityVBox.getChildren().get(1);
        TextField editionField = (TextField)editionVBox.getChildren().get(1);
        TextField reprintField = (TextField)reprintVBox.getChildren().get(1);
        TextField priceField = (TextField)priceVBox.getChildren().get(1);
        TextField languageField = (TextField)languageVBox.getChildren().get(1);
        ComboBox statusField = (ComboBox)statusVBox.getChildren().get(1);
        TextField chapterField = (TextField)chapterVBox.getChildren().get(1);

        isbnField.setText(book.getId());
         titleField.setText(book.getTitle());
         authorField.setText(book.getAuthor());
         publisherField.setText(book.getPublisher());
         genreField.setText(book.getGenre());
         yearField.setText(book.getYear()+"");
         quantityField.setText(book.getQuantity()+"");
         editionField.setText(book.getEdition());
         reprintField.setText(book.getReprint()+"");
         priceField.setText(book.getPrice()+"");
         languageField.setText(book.getLanguage());
         statusField.setValue(book.getStatus());
         chapterField.setText(book.getChapter()+"");


        // Tạo một nút để mở hộp thoại chọn file
        Button uploadButton = new Button("Chọn Ảnh");
        uploadButton.setMinWidth(300);
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        ImageView imageView;
        if(book.getCoverImage()==null) {
            Image imageCover = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
            imageView = new ImageView(imageCover); // Tạo một ImageView để hiển thị ảnh
        } else {
            imageView = new ImageView(book.getCoverImage()); // Tạo một ImageView để hiển thị ảnh
        }
        imageView.setFitWidth(300);
        imageView.setFitHeight(400);

        VBox imageVBox = new VBox(0, uploadButton, imageView);
        imageVBox.getChildren().addAll(new Label("Mã số tài liệu ban đầu: " + isbnField.getText()));
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.setLayoutX(1150);
        imageVBox.setLayoutY(95);
        //hoạt động nút tạo qr
        uploadButton.setOnAction(e -> {
            if(isbnField.getText().isEmpty())
                Noti.showErrorMessage("Xin hãy nhập mã ISBN trước", primaryStage);
            else {
                FileChooser fileChooser = new FileChooser(); // Khởi tạo FileChooser
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );   // Thiết lập các loại file cho phép chọn
                File selectedFile = fileChooser.showOpenDialog(primaryStage);  // Hiển thị hộp thoại chọn file và lưu file được chọn
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString()); // Tạo đối tượng Image từ file đã chọn
                    imageView.setImage(image); // Hiển thị ảnh lên ImageView
                }
            }
        });

        VBox vBoxLeft = new VBox(15);
        vBoxLeft.getChildren().addAll(titleVBox, authorVBox, publisherVBox,
                yearVBox,chapAndGenreHBox, quantityVBox);
        VBox vBoxRight = new VBox(15);
        vBoxRight.getChildren().addAll(isbnVBox,editionVBox,
                reprintVBox,languageVBox,priceVBox,statusVBox);

        Button deleteButton = new Button("Xóa");
        deleteButton.setLayoutX(1400);
        deleteButton.setLayoutY(580);
        deleteButton.setMinSize(70,40);
        deleteButton.setOnAction(e -> main.showInterfaceScene());
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        deleteButton.setOnAction(e->{
            isbnField.clear();
            titleField.clear();
            authorField.clear();
            publisherField.clear();
            yearField.clear();
            genreField.clear();
            quantityField.clear();
            editionField.clear();
            reprintField.clear();
            priceField.clear();
            languageField.clear();
            chapterField.clear();
            textArea.clear();
            imageView.setImage(null);
            urlField.clear();

            if(imageVBox.getChildren().size()==3)
                imageVBox.getChildren().remove(2);

            if(imageVBox.getChildren().get(1)==imageView)
                imageVBox.getChildren().remove(imageView);

            if(imageVBox.getChildren().size()==1)
                imageVBox.getChildren().add(imageView);
            imageVBox.setSpacing(50);

            qrImageView.setImage(null);
            qrBox.setLayoutX(1187.5);
            urlField.setMinWidth(150);
        });

        addButton.setOnAction(e-> {
            if (isbnField.getText().isEmpty())
                Noti.showErrorMessage("ISBN không được để trống", primaryStage);
            else if (titleField.getText().isEmpty())
                Noti.showErrorMessage("Tên sách không được để trống", primaryStage);
            else if (authorField.getText().isEmpty())
                Noti.showErrorMessage("Tên tác giả không được để trống", primaryStage);
            else
                try {
                    // Lấy giá trị từ TextField
                    book.setId(isbnField.getText());
                    book.setTitle(titleField.getText());
                    book.setAuthor(authorField.getText());
                    book.setPublisher(publisherField.getText());
                    book.setYear(parseInteger(yearField.getText(), "năm không hợp lệ"));
                    book.setGenre(genreField.getText());
                    book.setQuantity(parseInteger(quantityField.getText(), "số lượng không hợp lệ"));
                    book.setEdition(editionField.getText());
                    book.setReprint(parseInteger(reprintField.getText(), "số lần tái bản không hợp lệ"));
                    book.setPrice(parseDouble(priceField.getText(), "giá không hợp lệ"));
                    book.setLanguage(languageField.getText());
                    book.setStatus((String) statusField.getValue());
                    book.setChapter(parseDouble(chapterField.getText(), "số chương không hợp lệ"));
                    book.setSummary(textArea.getText());
                    book.setCoverImages(ImageConverter.imageToByteArray(qrImageView));
                    book.setQrCode( ImageConverter.imageToByteArray(imageView));

                    // Thêm vào cơ sở dữ liệu
                    bookDAO.updateBook(book);

                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    Noti.showFailureMessage("Lỗi " + ex.getMessage());
                }
        });

        HBox hBox = new HBox(40, vBoxLeft, vBoxRight);
        hBox.setLayoutX(70);
        hBox.setLayoutY(100);
        Pane layout = new Pane(summaryLabel, textArea, addFeild, imageVBox,
                hBox, qrBox, deleteButton, backButton, addButton);
        StackPane root = new StackPane();
        root.getChildren().addAll(whiteRectangle, layout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }
    //phương thức quản lý sách
    public Pane managerDocument(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        TableView<Book> tableView;
        ObservableList<Book> data = FXCollections.observableArrayList();
        List<String> a = new ArrayList<>();
        tableView = Table.tableDocument(primaryStage, main, true, 1000, a, this);
        Rectangle rectangle = rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, -20 );

        // Các trường nhập liệu để thêm dữ liệu mới
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField publisherField = new TextField();
        publisherField.setPromptText("Publisher");
        TextField yearField = new TextField();
        yearField.setPromptText("Year");
        TextField genreField = new TextField();
        genreField.setPromptText("Genre");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        AtomicBoolean check1 = new AtomicBoolean(false);
        AtomicBoolean check2 = new AtomicBoolean(false);
        Button searchButton = button("Tìm kiếm");
        searchButton.setOnAction(e->{
            data.clear();
            try {
                    Integer year = parseIntegerForFind(yearField.getText(),check1);
                    Integer quantity = parseIntegerForFind(quantityField.getText(),check2);

                    book = new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(),
                            year, genreField.getText(), quantity);

                } catch (NumberFormatException ex){

                }
            if(check1.get()||check2.get())
                bookDAO.setCheck(new AtomicBoolean(true));
            else
                bookDAO.setCheck(new AtomicBoolean(false));
            ResultSet resultSet = bookDAO.findBooks(book);
            if(resultSet!=null) {
                try {
                    while (resultSet.next()) {
                        data.add(new Book(
                                false,
                                resultSet.getString("id"),
                                resultSet.getString("title"),
                                resultSet.getString("author"),
                                resultSet.getString("publisher"),
                                getInt(resultSet, "year"),
                                resultSet.getString("genre"),
                                resultSet.getInt("quantity"),
                                "chi tiết"
                        ));
                    }
                } catch (SQLException ex) {
                    System.out.println("Lỗi khi đọc ResultSet: " + ex.getMessage());
                } finally {
                    try {
                        if (resultSet != null) resultSet.close();
                    } catch (SQLException ex) {
                        System.out.println("Lỗi khi đóng ResultSet: " + ex.getMessage());
                    }
                }
                bookDAO.closeDatabase();
            }
            // Thêm lắng nghe cho từng hàng
            tableView.getItems().forEach(book -> {
                book.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        a.add(book.getId()); // Thêm ID vào danh sách khi chọn
                    } else {
                        a.remove(book.getId()); // Xóa ID khỏi danh sách khi bỏ chọn
                    }
                });
            });
        });

        Button deleteSerchButton = button("Xóa Tìm kiếm");
        deleteSerchButton.setOnAction(e-> {
            data.clear();
            idField.clear();
            titleField.clear();
            quantityField.clear();
            publisherField.clear();
            yearField.clear();
            genreField.clear();
            quantityField.clear();
        });

        Button addButton = button("Thêm");
        addButton.setOnAction(e->{
            addDocument(primaryStage, main);
        });
        addButton.setMinWidth(30);

        Button updateButton = button("Sửa");
        updateButton.setOnAction(e->{
            if(a.size()>0)
            updateDocument(primaryStage,main,bookDAO.output1Value(a.get(0)));
            else
                Noti.showFailureMessage("Xin hãy chọn trước!");
            System.out.println(a.size());
        });
        updateButton.setMinWidth(15);

        Button deleteButton = button("Xóa");
        deleteButton.setOnAction(e->{
            for(String id0 : a) {
                bookDAO.removeBook(id0);
            }
            data.removeIf(Book::isSelected);
        });
        deleteButton.setMinWidth(15);

        HBox buttonLayout1 = new HBox(10, addButton, updateButton, deleteButton);
        buttonLayout1.setAlignment(Pos.TOP_RIGHT);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(data);

        HBox inputLayout = new HBox(10, idField, titleField, authorField, publisherField,
                yearField, genreField, quantityField);
        inputLayout.setAlignment(Pos.CENTER);
        HBox buttonLayout = new HBox(10, searchButton, deleteSerchButton);
        buttonLayout.setAlignment(Pos.CENTER);
        VBox buttonLayout2 = new VBox( buttonLayout, buttonLayout1);
        Pane layout = new Pane(rectangle,new VBox(20, inputLayout, buttonLayout2, tableView));
        layout.setLayoutY(120);
        return layout;
    }
    //phương thức xem chi tiết ảnh
    public void showBook(Stage primaryStage, interfaces main, String id) {

        book = bookDAO.output1Value(id);
        Label isbnLabel = new Label("ISBN: " + book.getId());
        Label titleLabel =  new Label("Tiêu đề: " + book.getTitle());
        Label authorLabel =  new Label("Tác giả: " + book.getAuthor());
        Label yearLabel =  new Label("Năm xuất bản: " + ((book.getYear()!=null)?book.getYear():""));
        Label genreLabel =  new Label("Thể loại: " + book.getGenre());
        Label publisherLabel =  new Label("Nhà xuất bản: " + book.getPublisher());
        Label quantityLabel =  new Label("Số lượng: " + book.getQuantity());
        Label editionLabel =  new Label("Phiên bản: " + book.getEdition());
        Label reprintLabel =  new Label("Số lần tái bản: " + ((book.getReprint()!=null)?book.getReprint():""));
        Label priceLabel =  new Label("Giá: " + ((book.getPrice()!=null)?book.getPrice():""));
        Label languageLabel =  new Label("Ngôn ngữ: " + book.getLanguage());
        Label statusLabel =  new Label("Trạng thái: " + book.getStatus());
        Label chapterLabel = new Label("Số chương: " + ((book.getChapter()!=null)?book.getChapter():""));
        Label summaryLabel = new Label(book.getSummary());
        ImageView qrCode;
        if(book.getQrCodeImage()!=null) {
            qrCode = new ImageView(book.getQrCodeImage());
            qrCode.setFitHeight(200);
            qrCode.setFitWidth(200);
        } else {
            Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/qr1.png");
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            qrCode = new ImageView(image);
            qrCode.setFitHeight(200);
            qrCode.setFitWidth(200);
        }
        ImageView coverImage;
        if(book.getCoverImage()!=null) {
            coverImage = new ImageView(book.getCoverImage());
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        } else {
            Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImage = new ImageView(image);
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        }

        Button backButton = new Button("Thoát");
        backButton.setOnAction(e-> main.showInterfaceScene());

        VBox imageHBox = new VBox(10, coverImage, qrCode, backButton);
        VBox attributeVBox1 = new VBox(10, titleLabel,authorLabel, yearLabel, genreLabel,publisherLabel, quantityLabel);
        VBox attributeVBox2 = new VBox(10, isbnLabel, editionLabel, reprintLabel, priceLabel, languageLabel, statusLabel,chapterLabel);
        HBox hBox = new HBox(10,imageHBox,attributeVBox1,attributeVBox2);
        VBox vBox = new VBox(10,hBox,summaryLabel);
        Scene scene = new Scene(vBox, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }

    public void addUser(Stage primaryStage, interfaces main) {

        VBox userIdVBox = vBox("ID người dùng");
        VBox fullNameVBox = vBox("Họ và tên");
        VBox dateOfBirthVBox = vBox("Ngày sinh");
        VBox addressVBox = vBox("Địa Chỉ");
        VBox phoneNumberVBox = vBox("Số Điện Thoại");
        VBox emailVBox = vBox("Email");
        VBox usernameVBox = vBox("Tên đăng nhập");
        VBox passwordHashVBox = vBox("Mật khẩu");
        VBox membershipIdVBox = vBox("Mã thẻ thư viện");
        VBox joinDateVBox = vBox("Ngày tạo tài khoản");
        VBox membershipStatusVBox = comboBox("Trạng thái thẻ",  new String[] {"active","expired","'locked'"});
        VBox roleVBox = comboBox("Chức vụ",  new String[] {"member","librarian","admin"});
        VBox expiryDateVBox = vBox("Ngày hết hạn thẻ");
        VBox cardRegistrationDateVBox = vBox("Ngày đăng kí thẻ");
        VBox accountStatusVBox = comboBox("Trạng thái tài khoản",  new String[] {"active","inactive"});
        VBox genderVBox = comboBox("Giới tính",  new String[] {"other","male","female"});
        VBox departmentVBox = vBox("Khoa");
        VBox classVBox = vBox("Lớp"); // 18 cái

        TextField userIdField = (TextField)userIdVBox.getChildren().get(1);
        TextField fullNameField = (TextField)fullNameVBox.getChildren().get(1);
        TextField dateOfBirthField = (TextField)dateOfBirthVBox.getChildren().get(1);
        TextField addressField = (TextField)addressVBox.getChildren().get(1);
        TextField phoneNumberField = (TextField)phoneNumberVBox.getChildren().get(1);
        TextField emailField = (TextField)emailVBox.getChildren().get(1);
        TextField usernameField = (TextField)usernameVBox.getChildren().get(1);
        TextField passwordHashField = (TextField)passwordHashVBox.getChildren().get(1);
        TextField membershipjoinDateId = (TextField)membershipIdVBox.getChildren().get(1);
        TextField joinDateField = (TextField)joinDateVBox.getChildren().get(1);
        ComboBox membershipStatusField = (ComboBox)membershipStatusVBox.getChildren().get(1);
        ComboBox roleField = (ComboBox)roleVBox.getChildren().get(1);
        TextField expiryDateField = (TextField)expiryDateVBox.getChildren().get(1);
        TextField cardRegistrationDateField = (TextField)cardRegistrationDateVBox.getChildren().get(1);
        ComboBox accountStatusField = (ComboBox)accountStatusVBox.getChildren().get(1);
        ComboBox genderField = (ComboBox)genderVBox.getChildren().get(1);
        TextField departmentField = (TextField)departmentVBox.getChildren().get(1);
        TextField classField = (TextField)classVBox.getChildren().get(1);

        //tạo tiêu đề
        Label addFeild = new Label("Thêm người dùng");
        addFeild.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
        addFeild.setLayoutX(70);
        addFeild.setLayoutY(60);
//ô bên noài
        Rectangle whiteRectangle = rectangle(screenWidth-100, screenHeight-100,Color.WHITE,
                Color.BLACK,2,10,10,1,60,50);

        // Tạo một nút để mở hộp thoại chọn file
        Button uploadButton = new Button("Chọn Ảnh");
        uploadButton.setMinWidth(300);
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        ImageView imageView = new ImageView(); // Tạo một ImageView để hiển thị ảnh
        imageView.setFitWidth(300);
        imageView.setFitHeight(400);
        Rectangle imgRectangle = rectangle(225,300,Color.WHITE,Color.BLACK,
                1,0,0,1,0,0);
        VBox imageVBox = new VBox(50, uploadButton, imgRectangle);
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.setLayoutX(1150);
        imageVBox.setLayoutY(95);
        //hoạt động nút tạo qr
        uploadButton.setOnAction(e -> {
            if(userIdField.getText().isEmpty())
                Noti.showErrorMessage("Xin hãy nhập ID người dùng trước", primaryStage);
            else {
                FileChooser fileChooser = new FileChooser(); // Khởi tạo FileChooser
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );   // Thiết lập các loại file cho phép chọn
                File selectedFile = fileChooser.showOpenDialog(primaryStage);  // Hiển thị hộp thoại chọn file và lưu file được chọn
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString()); // Tạo đối tượng Image từ file đã chọn
                    imageView.setImage(image); // Hiển thị ảnh lên ImageView
                    imageVBox.getChildren().remove(imgRectangle);
                    imageVBox.setSpacing(0);

                    if (imageView.getImage() == null)
                        imageVBox.getChildren().addAll(imageView, new Label("ID người dùng: " + userIdField.getText()));
                    else {
                        imageVBox.getChildren().remove(imageView);
                        imageVBox.getChildren().addAll(imageView, new Label("ID người dùng: " + userIdField.getText()));
                    }
                }
            }
        });

        VBox vBoxLeft = new VBox(15);
        vBoxLeft.getChildren().addAll(fullNameVBox, genderVBox, dateOfBirthVBox, departmentVBox,
                classVBox,addressVBox, phoneNumberVBox, emailVBox,accountStatusVBox);
        VBox vBoxRight = new VBox(15);
        vBoxRight.getChildren().addAll(userIdVBox,usernameVBox, passwordHashVBox,roleVBox,joinDateVBox,
                membershipIdVBox, cardRegistrationDateVBox, expiryDateVBox, membershipStatusVBox);

        Button deleteButton = new Button("Xóa");
        deleteButton.setLayoutX(1400);
        deleteButton.setLayoutY(580);
        deleteButton.setMinSize(70,40);
        deleteButton.setOnAction(e -> main.showInterfaceScene());
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

       deleteButton.setOnAction(e->{
           userIdField.clear();
           fullNameField.clear();
           dateOfBirthField.clear();
           addressField.clear();
           phoneNumberField.clear();
           emailField.clear();
           usernameField.clear();
           passwordHashField.clear();
           membershipjoinDateId.clear();
           joinDateField.clear();
           expiryDateField.clear();
           cardRegistrationDateField.clear();
           departmentField.clear();
           classField.clear();
           imageView.setImage(null);

            if(imageVBox.getChildren().size()==3)
                imageVBox.getChildren().remove(2);

            if(imageVBox.getChildren().get(1)==imageView)
                imageVBox.getChildren().remove(imageView);

            if(imageVBox.getChildren().size()==1)
                imageVBox.getChildren().add(imgRectangle);
            imageVBox.setSpacing(50);

        });

        addButton.setOnAction(e-> {
            if (userIdField.getText().isEmpty())
                Noti.showErrorMessage("ID không được để trống", primaryStage);
            else if (fullNameField.getText().isEmpty())
                Noti.showErrorMessage("Tên Người dùng không được để trống", primaryStage);
            else if (usernameField.getText().isEmpty())
                Noti.showErrorMessage("Tên đăng nhập không được để trống", primaryStage);
            else if (passwordHashField.getText().isEmpty())
                Noti.showErrorMessage("Mật khẩu không được để trống", primaryStage);
            else
                try {
                    // Lấy giá trị từ TextField
                    String userId = userIdField.getText();
                    String fullName = fullNameField.getText();
                    LocalDate dateOfBirth = parseDate(dateOfBirthField.getText(),"lỗi ngày");
                    String address = addressField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    String email = emailField.getText();
                    String username = usernameField.getText();
                    String passwordHash = passwordHashField.getText();
                    String membershipId = membershipjoinDateId.getText();
                    LocalDate joinDate =  parseDate(joinDateField.getText(), "Lỗi ngày");
                    String membershipStatus = (String) membershipStatusField.getValue();
                    String role = (String) roleField.getValue();
                    LocalDate expiryDate = parseDate(expiryDateField.getText(), "Lỗi ngày");
                    LocalDate cardRegistrationDate = parseDate(cardRegistrationDateField.getText(), "Lỗi ngày");
                    String accountStatus = (String) accountStatusField.getValue();
                    String gender = (String) genderField.getValue();
                    String department = departmentField.getText();
                    String className = classField.getText();


                    // Tạo đối tượng User
                 User user = new User( userId,  fullName,  dateOfBirth,  address,  phoneNumber,
                             email,  username,  passwordHash,  membershipId,
                             joinDate, membershipStatus, role,  cardRegistrationDate,
                             expiryDate,  accountStatus,  gender,  department,
                            className, ImageConverter.imageToByteArray(imageView));

                    userDAO.addUser(user);

                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    Noti.showFailureMessage("Lỗi: " + ex.getMessage());
                }
        });

        HBox hBox = new HBox(40, vBoxLeft, vBoxRight);
        hBox.setLayoutX(80);
        hBox.setLayoutY(100);
        Pane layout = new Pane( addFeild, imageVBox,
                hBox, deleteButton, backButton, addButton);
        StackPane root = new StackPane();
        root.getChildren().addAll(whiteRectangle, layout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }
    //phương thức quản lý người dùng
    public Pane managerUser(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        TableView<User> tableView;
        ObservableList<User> data = FXCollections.observableArrayList();
        List<String> arr = new ArrayList<>();
        tableView = Table.tableUser(primaryStage, main, true, 1000, arr, this);
        Rectangle rectangle = rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, -20 );
        // Các trường nhập liệu để thêm dữ liệu mới
        TextField userIdField = new TextField();
        userIdField.setPromptText("ID");
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Họ Và Tên");
        TextField joinDateField = new TextField();
        joinDateField.setPromptText("Ngày Tham Gia");
        TextField membershipStatusField = new TextField();
        membershipStatusField.setPromptText("Trạng Thái");
        TextField roleField = new TextField();
        roleField.setPromptText("Chức Vụ");
        TextField totalBooksBorrowedField = new TextField();
        totalBooksBorrowedField.setPromptText("Số Sách Đã Mượn");
        TextField overdueCountField = new TextField();
        overdueCountField.setPromptText("Số Lần Trả Trễ Hạn");

        AtomicBoolean check1 = new AtomicBoolean(false);
        AtomicBoolean check2 = new AtomicBoolean(false);
        AtomicBoolean check3 = new AtomicBoolean(false);
        Button searchButton = button("Tìm kiếm");
        searchButton.setOnAction(e->{
            data.clear();
            try {
                String userId = userIdField.getText();
                String fullName = fullNameField.getText();
                LocalDate joinDate = parseDateForFind(joinDateField.getText(), check3);
                String membershipStatus = membershipStatusField.getText();
                String role = roleField.getText();
                Integer totalBooksBorrowed = parseIntegerForFind(totalBooksBorrowedField.getText(),check1);
                Integer overdueCount = parseIntegerForFind(overdueCountField.getText(),check2);
                user = new User(userId, fullName, joinDate, membershipStatus,
                        role,totalBooksBorrowed,overdueCount);
            }catch (NumberFormatException ex){
                }
            if(check1.get()||check2.get()||check3.get())
                userDAO.setCheck(new AtomicBoolean(true));
            else
                userDAO.setCheck(new AtomicBoolean(false));
            ResultSet resultSet = userDAO.findUsers(user);

            if(resultSet!=null) {
                try {
                    while (resultSet.next()) {
                        data.add(new User(
                                false,
                                resultSet.getString("user_id"),
                                resultSet.getString("full_name"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("email"),
                                getDate(resultSet, "date_of_birth"),
                                resultSet.getInt("total_books_borrowed"),
                                resultSet.getString("membership_status"),
                                "chi tiết"
                        ));
                    }
                } catch (SQLException ex) {
                    System.out.println("Lỗi khi đọc ResultSet: " + ex.getMessage());
                } finally {
                    try {
                        if (resultSet != null) resultSet.close();
                    } catch (SQLException ex) {
                        System.out.println("Lỗi khi đóng ResultSet: " + ex.getMessage());
                    }
                }
                userDAO.closeDatabase();
            }
            // Thêm lắng nghe cho từng hàng
            tableView.getItems().forEach(user -> {
                user.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        arr.add(user.getUserId()); // Thêm ID vào danh sách khi chọn
                    } else {
                        arr.remove(user.getUserId()); // Xóa ID khỏi danh sách khi bỏ chọn
                    }
                });
            });
        });

        Button deleteSerchButton = button("Xóa Tìm kiếm");
        deleteSerchButton.setOnAction(e-> {
            data.clear();
            userIdField.clear();
            fullNameField.clear();
            joinDateField.clear();
            membershipStatusField.clear();
            roleField.clear();
            totalBooksBorrowedField.clear();
            overdueCountField.clear();
        });

        Button addButton = button("Thêm");
        addButton.setOnAction(e->{
            addUser(primaryStage, main);
        });
        addButton.setMinWidth(30);

        Button updateButton = button("Sửa");
        updateButton.setOnAction(e->{

        });
        updateButton.setMinWidth(15);

        Button deleteButton = button("Xóa");
        deleteButton.setOnAction(e->{
            for(String id0 : arr) {
                userDAO.deleteUser(id0);
            }
            data.removeIf(User::isSelected);
        });
        deleteButton.setMinWidth(15);

        HBox buttonLayout1 = new HBox(10, addButton, updateButton, deleteButton);
        buttonLayout1.setAlignment(Pos.TOP_RIGHT);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(data);

        HBox inputLayout = new HBox(10, userIdField, fullNameField, membershipStatusField,joinDateField,
                 roleField, totalBooksBorrowedField, overdueCountField);
        inputLayout.setAlignment(Pos.CENTER);
        HBox buttonLayout = new HBox(10, searchButton, deleteSerchButton);
        buttonLayout.setAlignment(Pos.CENTER);
        VBox buttonLayout2 = new VBox( buttonLayout, buttonLayout1);
        Pane layout = new Pane(rectangle, new VBox(20, inputLayout, buttonLayout2, tableView));
        layout.setLayoutY(120);
        return layout;
    }
    public void showUser(Stage primaryStage, interfaces main, String id) {

        user = userDAO.output1Value(id);
        Label userIdLabel = new Label("User ID: " + user.getUserId());
        Label fullNameLabel = new Label("Họ và tên: " + user.getFullName());
        Label dateOfBirthLabel = new Label("Ngày sinh: " + user.getDateOfBirth());
        Label addressLabel = new Label("Địa chỉ: " + user.getAddress());
        Label phoneNumberLabel = new Label("Số điện thoại: " + user.getPhoneNumber());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label usernameLabel = new Label("Tên đăng nhập: " + user.getUsername());
        Label passwordHashLabel = new Label("Mã hóa mật khẩu: " + user.getPasswordHash());
        Label membershipIdLabel = new Label("Mã thành viên: " + user.getMembershipId());
        Label joinDateLabel = new Label("Ngày tham gia: " + user.getJoinDate());
        Label membershipStatusLabel = new Label("Trạng thái thẻ: " + user.getMembershipStatus());
        Label roleLabel = new Label("Chức vụ: " + user.getRole());
        Label expiryDateLabel = new Label("Ngày hết hạn thẻ: " + user.getExpiryDate());
        Label cardRegistrationDateLabel = new Label("Ngày đăng ký thẻ: " + user.getCardRegistrationDate());
        Label accountStatusLabel = new Label("Trạng thái tài khoản: " + user.getAccountStatus());
        Label genderLabel = new Label("Giới tính: " + user.getGender());
        Label departmentLabel = new Label("Khoa: " + user.getDepartment());
        Label classLabel = new Label("Lớp: " + user.getClass());

        ImageView coverImage;
        if(user.getAvatar()!=null) {
            coverImage = new ImageView(book.getCoverImage());
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        } else {
            Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImage = new ImageView(image);
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        }

        Button backButton = new Button("Thoát");
        backButton.setOnAction(e->main.showInterfaceScene());

        VBox imageHBox = new VBox(10, coverImage, backButton);
        VBox attributeVBox1 = new VBox(10,fullNameLabel, genderLabel, dateOfBirthLabel, departmentLabel,
                classLabel,addressLabel, phoneNumberLabel, emailLabel,accountStatusLabel);
        VBox attributeVBox2 = new VBox(10,userIdLabel,usernameLabel, passwordHashLabel,roleLabel,joinDateLabel,
                membershipIdLabel, cardRegistrationDateLabel, expiryDateLabel, membershipStatusLabel);
        HBox hBox = new HBox(10,imageHBox,attributeVBox1,attributeVBox2);
        VBox vBox = new VBox(10,hBox);
        Scene scene = new Scene(vBox, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }


    Button button(String s) {
        Button button0 = new Button(s);
        button0.setStyle("-fx-background-color: #0066CC; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;");
        button0.setMinHeight(15);
        button0.setMinWidth(200);
        button0.setOnMouseEntered(e-> button0.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        button0.setOnMouseExited(e-> button0.setStyle("-fx-background-color:  #0066CC; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        return button0;
    }

    VBox vBox(String s) {
        Label label = new Label(s);
        label.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
        TextField textField = new TextField();
        textField.setPromptText(s);
        textField.setMinHeight(20);
        textField.setStyle("-fx-background-color: white; -fx-border-color: #ccc;-fx-border-radius: 2px;");
        VBox vBox = new VBox(0, label, textField);
        vBox.setMinWidth(500);
        return vBox;
    }
    public VBox comboBoxAndTextField(String s) {
        Label label = new Label(s);
        label.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");

        // Tạo TextField
        TextField textField = new TextField();
        textField.setPromptText(s);
        textField.setMinHeight(20);

        // Tạo ComboBox
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("available", "not available");
        statusComboBox.setValue("available"); // giá trị mặc định
        statusComboBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color:transparent #ccc transparent transparent; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-padding: 0px; " +
                        "-fx-border-width: 1px;"
        );

        statusComboBox.setPrefSize(20,20);
        statusComboBox.setMinSize(20,20);
        statusComboBox.setMaxSize(20,20);

        // Thiết lập giá trị mặc định cho TextField
        textField.setText(statusComboBox.getValue());
        textField.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-padding: 4px 25px 4px 5px;-fx-border-radius: 2px;");
        // Thêm Listener để cập nhật TextField khi giá trị của ComboBox thay đổi
        statusComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(newValue);
        });

        // Bố cục HBox và VBox
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(textField, statusComboBox);
        stackPane.setMinWidth(500);
        StackPane.setAlignment(statusComboBox, Pos.CENTER_RIGHT);
        VBox vBox = new VBox(0, label, stackPane);
        vBox.setMinWidth(500);

        return vBox;
    }
    public VBox comboBox(String s, String[] arr) {
        Label label = new Label(s);
        label.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");

        // Tạo ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(arr);
        comboBox.setValue(arr[0]); // giá trị mặc định
        comboBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color:#ccc #ccc #ccc #ccc; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-padding: 0px; " +
                        "-fx-border-width: 1px;"
        );

        comboBox.setPrefSize(500,28);
        comboBox.setMinSize(500,28);
        comboBox.setMaxSize(500,28);


        // Bố cục HBox và VBox

        VBox vBox = new VBox(0, label, comboBox);
        vBox.setMinWidth(500);

        return vBox;
    }

    VBox vBoxs(String s1, String s2) {
        Label label1 = new Label(s1);
        label1.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
        Label label2 = new Label(s2);
        label2.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
        VBox vBox = new VBox(0, label1, label2);
        vBox.setMinWidth(500);
        return vBox;
    }

    TextArea textArea() {
        TextArea textArea = new TextArea();
        textArea.setLayoutX(70);
        textArea.setLayoutY(500);
        textArea.setMaxSize(1040,240);  // Kích thước bên trong ô vuông
        textArea.setMinSize(1040,240);
        textArea.setWrapText(true);  // Tự xuống dòng khi vượt quá chiều rộng
        //textArea.setPadding(new Insets(20, 10, 10, 10));  // Cách viền 10px
        textArea.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;"+
                        "-fx-font-style: italic;" +
                        "-fx-text-fill: black;" +
                        "-fx-border-color: black;" + // Đặt màu viền là đen
                        "-fx-border-width: 1;" + // Đặt độ dày viền
                        "-fx-focus-color: transparent;" + // Xóa màu focus
                        "-fx-faint-focus-color: transparent;" + // Xóa viền mờ khi focus
                        "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );
        return textArea;
    }

    // Phương thức chuyển đổi kiểu Integer
    private Integer parseInteger(String input, String s) {
        if (input.isEmpty()) {
            return null; // Nếu trường rỗng, trả về null
        } else {
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(s);
            }
        }
    }
    private Integer parseIntegerForFind(String input,AtomicBoolean  check) {
        if (input.isEmpty()) {
            check.set(false);
            return null; // Nếu trường rỗng, trả về null
        } else {
            try {
                check.set(false);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                check.set(true);
               return null;
            }
        }
    }
    // Phương thức chuyển đổi kiểu Double
    private Double parseDouble(String input, String s) {
        if (input.isEmpty()) {
            return null; // Nếu trường rỗng, trả về null
        } else {
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(s);
            }
        }
    }
    public Integer getInt(ResultSet resultSet, String type) throws SQLException {
        int type0 = resultSet.getInt(type);
        return resultSet.wasNull() ? null : type0;
    }

    private LocalDate parseDate(String input, String s) {
        if (input.isEmpty()) {
            return null; // Nếu trường rỗng, trả về null
        } else {
            try {
                return java.time.LocalDate.parse(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(s);
            }
        }
    }
    private LocalDate parseDateForFind(String dateString, AtomicBoolean check) {
        if (dateString == null || dateString.isEmpty()) {
            check.set(false);
            return null;  // Trả về null nếu chuỗi là rỗng hoặc null
        }
        try {
            check.set(false);
            return java.time.LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            check.set(true);
            // Xử lý ngoại lệ nếu chuỗi không thể phân tích thành LocalDate
            System.out.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }
    public LocalDate getDate(ResultSet resultSet, String type) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(type);
        return resultSet.wasNull() ? null : sqlDate.toLocalDate();
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

    private Image generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());

        return new Image(inputStream);
    }
}

