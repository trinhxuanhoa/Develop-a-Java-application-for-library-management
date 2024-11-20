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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class LibraryManagement {

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();
    private Book book;
    BookDAO bookDAO;
    private User user;
    UserDAO userDAO;
    private Stage primaryStage;
    private interfaces main;
    public LibraryManagement(Stage primaryStage, interfaces main) {
        book = null;
        bookDAO = new BookDAO();
        user = null;
        userDAO = new UserDAO();
        this.primaryStage = primaryStage;
        this.main = main;
    }
    // Phương thức thêm sách
    public void addDocument() {

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
        VBox pagesVBox = vBox("Số trang");
        genreVBox.setMinWidth(400);
        chapterVBox.setMinWidth(90);
        HBox chapAndGenreHBox = new HBox(10,genreVBox,chapterVBox);
        chapAndGenreHBox.setMaxWidth(500);
        quantityVBox.setMinWidth(400);
        pagesVBox.setMinWidth(90);
        pagesVBox.setMaxWidth(90);
        HBox quantityAndPagesHBox = new HBox(10,quantityVBox,pagesVBox);
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
        TextField pagesField = (TextField)pagesVBox.getChildren().get(1);
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
                yearVBox,chapAndGenreHBox, quantityAndPagesHBox);
        VBox vBoxRight = new VBox(15);
        vBoxRight.getChildren().addAll(isbnVBox,editionVBox,
              reprintVBox,languageVBox,priceVBox,statusVBox);

        Button deleteButton = new Button("Xóa");
        deleteButton.setLayoutX(1400);
        deleteButton.setLayoutY(580);
        deleteButton.setMinSize(70,40);
        deleteButton.setOnAction(e -> main.showInterfaceScene());
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

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
           pagesField.clear();
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
                    Double chapter = parseDouble(chapterField.getText(), "số chương không hợp lệ");
                    Integer pages = parseInteger(pagesField.getText(), "số trang không hợp lệ");
                    String description = textArea.getText();

                    // Tạo đối tượng Book
                    Book book = new Book(isbn, title, author, publisher, year, genre, quantity,
                            edition, reprint, price, language, status, chapter, pages, description,
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
    public void updateDocument(Book book) {

        //tạo mã QR
        TextField urlField;
        if(book.getQrCodeImage()!=null)
            urlField = new TextField(ImageConverter.decodeQRCodeFromImageView(book.getQrCodeImage()));
        else
            urlField = new TextField();
        urlField.setPromptText("Nhập đường link...");
        Button generateButton = new Button("Tạo mã QR");
        ImageView qrImageView;
        AtomicBoolean checkQR = new AtomicBoolean(false);
        if(book.getQrCodeImage()!=null) {
            checkQR.set(true);
            qrImageView = new ImageView(book.getQrCodeImage());
        }
        else {
            checkQR.set(false);
            qrImageView = new ImageView(new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/qr.png"));
        }
        qrImageView.setFitWidth(160);
        qrImageView.setFitHeight(160);

        generateButton.setOnAction(e -> {
            String url = urlField.getText();
            if (!url.isEmpty()) {
                try {
                    checkQR.set(true);
                    Image qrImage = generateQRCodeImage(url, 200, 200);
                    qrImageView.setImage(qrImage);
                } catch (WriterException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox linkHBox = new HBox(urlField, generateButton);
        VBox qrBox = new VBox(20, linkHBox, qrImageView);
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
        VBox pagesVBox = vBox("Số trang");
        VBox downloadsVBox = vBox("Số lượt tải");
        genreVBox.setMinWidth(400);
        chapterVBox.setMinWidth(90);
        HBox chapAndGenreHBox = new HBox(10,genreVBox,chapterVBox);
        chapAndGenreHBox.setMaxWidth(500);
        quantityVBox.setMinWidth(300);
        pagesVBox.setMinWidth(90);
        pagesVBox.setMaxWidth(90);
        downloadsVBox.setMinWidth(100);
        downloadsVBox.setMaxWidth(100);
        HBox quantityAndPagesHBox = new HBox(10,quantityVBox, downloadsVBox, pagesVBox);
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
        TextField pagesField = (TextField)pagesVBox.getChildren().get(1);
        TextField downloadsField = (TextField)downloadsVBox.getChildren().get(1);
        downloadsField.setEditable(false);

        isbnField.setText(book.getId());
         titleField.setText(book.getTitle());
         authorField.setText(book.getAuthor());
         publisherField.setText(book.getPublisher());
         genreField.setText(book.getGenre());
         yearField.setText((book.getYear()!=null)?book.getYear()+"":"");
         quantityField.setText(book.getQuantity()+"");
         editionField.setText(book.getEdition());
         reprintField.setText((book.getReprint()!=null)?book.getReprint()+"":"");
         priceField.setText((book.getPrice()!=null)?book.getPrice()+"":"");
         languageField.setText(book.getLanguage());
         statusField.setValue(book.getStatus());
         chapterField.setText((book.getChapter()!=null)?book.getChapter()+"":"");
         pagesField.setText((book.getPages()!=null)?book.getPages()+"":"");
         downloadsField.setText(book.getDownloads()+"");


        // Tạo một nút để mở hộp thoại chọn file
        Button uploadButton = new Button("Chọn Ảnh");
        uploadButton.setMinWidth(300);
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 12px; -fx-font-weight: bold;"));
        ImageView imageView;
        AtomicBoolean checkCoverIamge = new AtomicBoolean(false);
        if(book.getCoverImage()==null) {
            checkCoverIamge.set(false);
            Image imageCover = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
            imageView = new ImageView(imageCover); // Tạo một ImageView để hiển thị ảnh
        } else {
            checkCoverIamge.set(true);
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
                    checkCoverIamge.set(true);
                    Image image = new Image(selectedFile.toURI().toString()); // Tạo đối tượng Image từ file đã chọn
                    imageView.setImage(image); // Hiển thị ảnh lên ImageView
                }
            }
        });

        VBox vBoxLeft = new VBox(15);
        vBoxLeft.getChildren().addAll(titleVBox, authorVBox, publisherVBox,
                yearVBox,chapAndGenreHBox, quantityAndPagesHBox);
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
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

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
                  if(checkCoverIamge.get())
                    book.setCoverImages(ImageConverter.imageToByteArray(imageView));
                  if(checkQR.get())
                    book.setQrCode(ImageConverter.imageToByteArray(qrImageView));

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
    public Pane managerDocument() {
        // Tạo bảng TableView
        TableView<Book> tableView;
        ObservableList<Book> data = FXCollections.observableArrayList();
        List<String> a = new ArrayList<>();
        tableView = Table.tableDocument(true, 1000, a, this);
        Rectangle rectangle = rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, -20 );

        // Các trường nhập liệu để thêm dữ liệu mới
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField titleField = new TextField();
        titleField.setPromptText("Tiêu đề");
        TextField authorField = new TextField();
        authorField.setPromptText("Tác giả");
        TextField publisherField = new TextField();
        publisherField.setPromptText("Nhà Xuất Bản");
        TextField yearField = new TextField();
        yearField.setPromptText("Năm xuất bản");
        TextField genreField = new TextField();
        genreField.setPromptText("Thể loại");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Số lượng");

        AtomicBoolean check1 = new AtomicBoolean(false);
        AtomicBoolean check2 = new AtomicBoolean(false);
        Button searchButton = button("Tìm kiếm");
        searchButton.setOnAction(e->{
            a.clear();
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
            a.clear();
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
            addDocument();
        });
        addButton.setMinWidth(30);

        Button updateButton = button("Sửa");
        updateButton.setOnAction(e->{
            if(a.size()>0) {
                for(String s : a)
                System.out.println(s);
                updateDocument(bookDAO.output1Value(a.get(0)));
            }
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
            a.clear();
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
    public void showBook(String id) {

        book = bookDAO.output1Value(id);
        VBox isbnBox = styledVBox("ISBN:", book.getId());
        Label titleLabel =  new Label(book.getTitle());
        Label authorLabel =  new Label(book.getAuthor());
        VBox titleBox = styledVBox("Tiêu đề:", book.getTitle());
        VBox authorBox = styledVBox("Tác giả:", book.getAuthor());
        VBox yearBox = styledVBox("Năm xuất bản:", (book.getYear() != null) ? book.getYear().toString() : "");
        VBox genreBox = styledVBox("Thể loại:", book.getGenre());
        VBox publisherBox = styledVBox("Nhà xuất bản:", book.getPublisher());
        VBox quantityBox = styledVBox("Số lượng:", Integer.toString(book.getQuantity()));
        VBox editionBox = styledVBox("Phiên bản:", book.getEdition());
        VBox reprintBox = styledVBox("Số lần tái bản:", (book.getReprint() != null) ? book.getReprint().toString() : "");
        VBox priceBox = styledVBox("Giá:", (book.getPrice() != null) ? book.getPrice().toString() : "");
        VBox languageBox = styledVBox("Ngôn ngữ:", book.getLanguage());
        VBox statusBox = styledVBox("Trạng thái:", book.getStatus());
        VBox chapterBox = styledVBox("Số chương:", (book.getChapter() != null) ? book.getChapter().toString() : "");

        Label pages = new Label((book.getPages() != null) ? book.getPages().toString() : "");
        Label downloads = new Label(Integer.toString(book.getDownloads()));
        Label ratingLabel = new Label(BookReviewsDAO.getAverageRating(id));
        HBox pagesAndDownloads = pagesAndDownloads(pages, downloads, ratingLabel);


        Label summaryLabel = new Label("Tóm tắt nội dung:");
        summaryLabel.setLayoutX(70);
        summaryLabel.setLayoutY(480);
        summaryLabel.setStyle("-fx-font-size: 15px;-fx-font-style: italic;-fx-font-family: 'Arial';");
        TextArea summaryArea = textArea(book.getSummary());

        summaryArea.setEditable(false);
        VBox summaryVBox = new VBox(summaryLabel, summaryArea);


        ImageView qrCode;
        Label qr = new Label("Quét mã QR để tải sách");
        Rectangle rectangle = rectangle(screenWidth-60, screenHeight-80, Color.WHITE,
                Color.BLACK, 2, 10,10, 0.8, 30, 30);
        if(book.getQrCodeImage()!=null) {
            qrCode = new ImageView(book.getQrCodeImage());
            qrCode.setFitHeight(200);
            qrCode.setFitWidth(200);
        } else {
            Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/qr.png");
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

        Button backButton = buttonForShowBook(70, 30, "Thoát", "#FF0000", "#8B0000");
        backButton.setOnAction(e-> main.showInterfaceScene());
        Button borrowButton = buttonForShowBook(70, 30, "Mượn", "#0000FF", "#00008B");
        borrowButton.setOnAction(e->{
            Borrow borrow = new Borrow();
            borrow.setBookId(id);
            borrow.setUserId(interfaces.userId());

            LocalDate borrowDate = LocalDate.now();
            int loanPeriodDays = 30; // Số ngày được mượn

            borrow.setBorrowDate(borrowDate);
            borrow.setStatus("đã mượn");
            borrow.setDueDate(borrow.calculateDueDate(borrowDate, loanPeriodDays));

            if(!BorrowDAO.isBorrowed(id, interfaces.userId())) {
                    BorrowDAO.addBorrow(borrow);
                    if(BorrowDAO.updateQuantity(id, BorrowDAO.remainingQuantity(id) - 1))
                    bookDAO.updateDownload(id, BorrowDAO.downloads(id));
                }
             else {
                Noti.showFailureMessage("Bạn đã mượn sách này rồi");
            }
        });
        Button ratingButton = buttonForShowBook(90, 30, "Đánh giá", "#008000", "#006400");
        AtomicInteger rating = new AtomicInteger(0);
        ratingButton.setOnAction(e->{
            if(rating.get()-1>=1) {
                BookReviews bookReviews = new BookReviews();
                bookReviews.setBookId(id);
                bookReviews.setUserId(interfaces.userId());
                bookReviews.setRating(rating.get()-1);
                bookReviews.setReviewDate(LocalDate.now());
                BookReviewsDAO.add(bookReviews);
            } else {
                Noti.showFailureMessage("Xin vui lòng chọn đánh giá của bạn trước");
            }

        });

        HBox buttonHBox = new HBox(10, ratingButton, borrowButton, backButton);
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);
        buttonHBox.setMinWidth(250);
        buttonHBox.setMaxWidth(250);
        buttonHBox.setMinHeight(35);
        buttonHBox.setMaxHeight(35);

        VBox qrVBox =  new VBox(qrCode, qr);
        HBox qrHBox = new HBox(20, qrVBox);
        VBox coverImageVBox = new VBox(coverImage, titleLabel, authorLabel);
        VBox imageVBox = new VBox(20, coverImageVBox, qrHBox);
        imageVBox.setLayoutX(50);
        imageVBox.setLayoutY(50);
        qrCode.setFitHeight(170);
        qrCode.setFitWidth(170);
        titleLabel.setMaxWidth(280);
        titleLabel.setWrapText(true); // Cho phép xuống dòng khi vượt quá chiều rộng
        titleLabel.setStyle("-fx-text-fill: #000000; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        authorLabel.setMaxHeight(40);
        authorLabel.setWrapText(true);
        authorLabel.setStyle("-fx-text-fill: #808080; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        authorLabel.setMaxWidth(280);
        qrVBox.setAlignment(Pos.CENTER);
        qrHBox.setAlignment(Pos.CENTER);
        coverImageVBox.setAlignment(Pos.CENTER);
        HBox genreAndChapter = new HBox(10, genreBox, chapterBox);
        genreBox.setMinWidth(400);
        chapterBox.setMaxWidth(138);
        chapterBox.setMinWidth(138);
        genreAndChapter.setMinWidth(500);
        genreAndChapter.setMaxWidth(500);
        VBox leftVBox = new VBox(5, new VBox(10,titleBox, authorBox, publisherBox,
                yearBox, genreAndChapter, quantityBox), pagesAndDownloads);
        VBox rightVBox = new VBox(30,new VBox(10, isbnBox, editionBox, reprintBox,
                languageBox, priceBox, statusBox), new HBox(25,Shape.startHBox(rating), buttonHBox));
        leftVBox.setMaxWidth(548);
        leftVBox.setMinWidth(548);
        rightVBox.setMaxWidth(548);
        rightVBox.setMinWidth(548);
        HBox LRhBox = new HBox(20, leftVBox, rightVBox);
        List<VBox> list = CommentsDAO.getCommentDisplay(id);
        CommentPage commentPage = new CommentPage(list);
        VBox myCommentVbox = createCommentTextArea(id,0, list, commentPage);
        myCommentVbox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(30, LRhBox, summaryVBox, myCommentVbox);


        vBox.getChildren().add(commentPage.commentPage());

        HBox hBox = new HBox(20, imageVBox, vBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMinHeight(screenHeight*2+100);
        hBox.setMaxHeight(screenHeight*2+100);

        summaryVBox.setMinWidth(1116);
        summaryVBox.setMaxWidth(1116);
        summaryArea.setMinWidth(1116);
        summaryArea.setMaxWidth(1116);



        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setFocusTraversable(false);
        scrollPane.setLayoutX(33);
        scrollPane.setLayoutY(50);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-background-color: #FFFFFF;");
        scrollPane.setPrefWidth(screenWidth-67);
        scrollPane.setPrefHeight(screenHeight-110);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        commentPage.setScrollPane(scrollPane);

        Pane pane = new Pane(rectangle,scrollPane);

        Scene scene = new Scene(pane, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }
    public Pane showBooks() {
        // Tạo rectangle làm nền
        Rectangle rectangle = rectangle(screenWidth - 350, screenHeight - 170, Color.WHITE,
                Color.BLACK, 1, 10, 10, 0.8, -25, 100);

        // Tạo TextField và Button tìm kiếm
        TextField searchField = new TextField();
        searchField.setMinWidth(750);
        searchField.setMaxWidth(750);
        searchField.setPromptText("Tên sách, Tên tác giả");
        Button searchButton = new Button("Tìm kiếm");
        searchButton.setMinWidth(100);
        searchButton.setMaxWidth(100);

        HBox searchHBox = new HBox(searchField, searchButton);
        searchHBox.setMinWidth(850);
        searchHBox.setMaxWidth(850);
        searchHBox.setAlignment(Pos.CENTER);
        searchHBox.setLayoutX(143);
        searchHBox.setLayoutY(120);

        Pane pane = new Pane(rectangle, searchHBox);

        // Tạo ScrollPane chứa StackPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ẩn thanh cuộn ngang
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ẩn thanh cuộn dọc
        scrollPane.setPrefSize(screenWidth - 480, screenHeight - 320);
        scrollPane.setLayoutX(40);
        scrollPane.setLayoutY(240);
        VBox vBox = new VBox(10);
        scrollPane.setContent(vBox); // Đặt StackPane vào ScrollPane
        pane.getChildren().add(scrollPane);
        scrollPane.setStyle("-fx-background-color: #FFFFFF; -fx-background: #FFFFFF;");

        // Xử lý sự kiện khi nhấn nút Tìm kiếm
        searchButton.setOnAction(e -> {
            HBox hBox = new HBox(30);
            vBox.getChildren().clear();

            final String keyword = searchField.getText();
            ResultSet resultSet = bookDAO.findBooksUtimate(keyword); // Gọi phương thức tìm kiếm

            int i = 0;
            try {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    Image coverImage;

                    // Kiểm tra ảnh bìa
                    if (resultSet.getBytes("cover_image") != null) {
                        coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                    } else {
                        coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                    }

                    hBox.getChildren().add(bookVBox(id, title, author, coverImage, primaryStage, main));
                    i++;

                    // Nếu có đủ 5 phần tử trong hBox, thêm hBox vào newVBox và tạo hBox mới
                    if (i == 6) {
                        vBox.getChildren().add(hBox);  // Thêm HBox vào VBox
                        hBox = new HBox(30);  // Tạo một HBox mới để chứa 6 phần tử tiếp theo
                        i = 0;  // Đặt lại biến đếm
                    }
                }

                // Nếu vẫn còn phần tử trong hBox sau khi duyệt hết resultSet
                if (i > 0) {
                    vBox.getChildren().add(hBox);
                }

            } catch (SQLException ex) {
                Noti.showErrorMessage("Lỗi khi tìm sách: " + ex.getMessage(), primaryStage);
            } finally {
                bookDAO.closeDatabase();
            }

        });

        return pane;
    }

    public void addUser() {

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
        TextField membershipIdField = (TextField)membershipIdVBox.getChildren().get(1);
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
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;"));
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
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

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
           membershipIdField.clear();
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
                    String membershipId = membershipIdField.getText();
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
    public void updateUser(User user) {

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
        TextField membershipIdField = (TextField)membershipIdVBox.getChildren().get(1);
        TextField joinDateField = (TextField)joinDateVBox.getChildren().get(1);
        ComboBox membershipStatusField = (ComboBox)membershipStatusVBox.getChildren().get(1);
        ComboBox roleField = (ComboBox)roleVBox.getChildren().get(1);
        TextField expiryDateField = (TextField)expiryDateVBox.getChildren().get(1);
        TextField cardRegistrationDateField = (TextField)cardRegistrationDateVBox.getChildren().get(1);
        ComboBox accountStatusField = (ComboBox)accountStatusVBox.getChildren().get(1);
        ComboBox genderField = (ComboBox)genderVBox.getChildren().get(1);
        TextField departmentField = (TextField)departmentVBox.getChildren().get(1);
        TextField classField = (TextField)classVBox.getChildren().get(1);

        // Gán giá trị ban đầu từ đối tượng User
        userIdField.setText(user.getUserId());
        fullNameField.setText(user.getFullName());
        dateOfBirthField.setText((user.getDateOfBirth()!=null)?user.getDateOfBirth().toString():"");
        addressField.setText(user.getAddress());
        phoneNumberField.setText(user.getPhoneNumber());
        emailField.setText(user.getEmail());
        usernameField.setText(user.getUsername());
        passwordHashField.setText(user.getPasswordHash());
        membershipIdField.setText(user.getMembershipId());
        joinDateField.setText((user.getJoinDate()!=null)?user.getJoinDate().toString():"");
        membershipStatusField.setValue(user.getMembershipStatus());
        roleField.setValue(user.getRole());
        expiryDateField.setText((user.getExpiryDate()!=null)?user.getExpiryDate().toString():"");
        cardRegistrationDateField.setText((user.getCardRegistrationDate()!=null)?user.getCardRegistrationDate().toString():"");
        accountStatusField.setValue(user.getAccountStatus());
        genderField.setValue(user.getGender());
        departmentField.setText(user.getDepartment());
        classField.setText(user.getClassName());
        Image avatar = user.getAvatar();

        //tạo tiêu đề
        Label addFeild = new Label("Cập nhật thông tin người dùng");
        addFeild.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
        addFeild.setLayoutX(70);
        addFeild.setLayoutY(60);
//ô bên noài
        Rectangle whiteRectangle = rectangle(screenWidth-100, screenHeight-100,Color.WHITE,
                Color.BLACK,2,10,10,1,60,50);

        // Tạo một nút để mở hộp thoại chọn file
        Button uploadButton = new Button("Chọn Ảnh");
        uploadButton.setMinWidth(300);
        uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;");
        uploadButton.setOnMouseEntered(e-> uploadButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;"));
        uploadButton.setOnMouseExited(e-> uploadButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 12px; -fx-font-weight: bold;"));
        ImageView imageView = new ImageView(); // Tạo một ImageView để hiển thị ảnh
        imageView.setFitWidth(300);
        imageView.setFitHeight(400);
        Rectangle imgRectangle = rectangle(225,300,Color.WHITE,Color.BLACK,
                1,0,0,1,0,0);
        if(avatar!=null) {
            imageView.setImage(avatar);
        } else {
            Random random = new Random();
            int i = random.nextInt(2);
            String av = UserDAO.gender(userIdField.getText());
            if(av.compareTo("other")==0) {
                av = "male" + i;
            } else {
                av +=i;
            }
            imageView.setImage(new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/" + av + ".jpg"));
        }

        VBox imageVBox = new VBox(uploadButton, imageView);
        imageVBox.getChildren().add(new Label("ID người dùng: " + userIdField.getText()));
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
                    if (imageVBox.getChildren().contains(imgRectangle))
                        imageVBox.getChildren().set(1, imageView);
                    imageVBox.setSpacing(0);
                    imageVBox.getChildren().set(2, new Label("ID người dùng: " + userIdField.getText()));
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
        deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        deleteButton.setOnMouseEntered(e-> deleteButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        deleteButton.setOnMouseExited(e-> deleteButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(640);
        backButton.setMinSize(70,40);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(700);
        addButton.setMinSize(70,40);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        deleteButton.setOnAction(e->{
           Noti.showConfirmationDialog("Xác nhận xóa",
                   "Bạn có chắc muốn toàn bộ?",
                   "Hành động này không thể hoàn tác!");
            userIdField.clear();
            fullNameField.clear();
            dateOfBirthField.clear();
            addressField.clear();
            phoneNumberField.clear();
            emailField.clear();
            usernameField.clear();
            passwordHashField.clear();
            membershipIdField.clear();
            joinDateField.clear();
            expiryDateField.clear();
            cardRegistrationDateField.clear();
            departmentField.clear();
            classField.clear();

imageVBox.getChildren().set(1, imgRectangle);
imageVBox.setSpacing(50);
imageVBox.getChildren().set(2,new Label(""));

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
                    String membershipId = membershipIdField.getText();
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
    public Pane managerUser() {
        // Tạo bảng TableView
        TableView<User> tableView;
        ObservableList<User> data = FXCollections.observableArrayList();
        List<String> arr = new ArrayList<>();
        tableView = Table.tableUser(true, 1000, arr, this, null);
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
        TextField totalBooksReturnedField = new TextField();
        totalBooksReturnedField.setPromptText("Số Sách đã trả");

        AtomicBoolean check1 = new AtomicBoolean(false);
        AtomicBoolean check2 = new AtomicBoolean(false);
        AtomicBoolean check3 = new AtomicBoolean(false);
        Button searchButton = button("Tìm kiếm");
        searchButton.setOnAction(e->{
            arr.clear();
            data.clear();
            try {
                String userId = userIdField.getText();
                String fullName = fullNameField.getText();
                LocalDate joinDate = parseDateForFind(joinDateField.getText(), check3);
                String membershipStatus = membershipStatusField.getText();
                String role = roleField.getText();
                Integer totalBooksBorrowed = parseIntegerForFind(totalBooksBorrowedField.getText(),check1);
                Integer totalBooksReturned = parseIntegerForFind(totalBooksReturnedField.getText(),check2);
                user = new User(userId, fullName, joinDate, membershipStatus,
                        role,totalBooksBorrowed,totalBooksReturned);
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
            arr.clear();
            data.clear();
            userIdField.clear();
            fullNameField.clear();
            joinDateField.clear();
            membershipStatusField.clear();
            roleField.clear();
            totalBooksBorrowedField.clear();
            totalBooksReturnedField.clear();
        });

        Button addButton = button("Thêm");
        addButton.setOnAction(e->{
            addUser();
        });
        addButton.setMinWidth(30);

        Button updateButton = button("Sửa");
        updateButton.setOnAction(e->{
            if(arr.size()>0) {
                for(String s : arr)
                    System.out.println(s);
                updateUser(userDAO.output1Value(arr.get(0)));
            }
            else
                Noti.showFailureMessage("Xin hãy chọn trước!");
            System.out.println(arr.size());
        });
        updateButton.setMinWidth(15);

        Button deleteButton = button("Xóa");
        deleteButton.setOnAction(e->{
            for(String id0 : arr) {
                bookDAO.removeBook(id0);
            }
            arr.clear();
            data.removeIf(User::isSelected);
        });
        deleteButton.setMinWidth(15);

        HBox buttonLayout1 = new HBox(10, addButton, updateButton, deleteButton);
        buttonLayout1.setAlignment(Pos.TOP_RIGHT);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(data);

        HBox inputLayout = new HBox(10, userIdField, fullNameField, membershipStatusField,joinDateField,
                 roleField, totalBooksBorrowedField, totalBooksReturnedField);
        inputLayout.setAlignment(Pos.CENTER);
        HBox buttonLayout = new HBox(10, searchButton, deleteSerchButton);
        buttonLayout.setAlignment(Pos.CENTER);
        VBox buttonLayout2 = new VBox( buttonLayout, buttonLayout1);
        Pane layout = new Pane(rectangle, new VBox(20, inputLayout, buttonLayout2, tableView));
        layout.setLayoutY(120);
        return layout;
    }
    public void showUser(String id, Login login) {

        user = userDAO.output1Value(id);
        HBox userIdHBox = hBoxForShowUser("ID người dùng:", user.getUserId());
        HBox fullNameHBox = hBoxForShowUser("Họ và tên:", user.getFullName());
        HBox dateOfBirthHBox = hBoxForShowUser("Ngày sinh:", user.getDateOfBirth() + "");
        HBox addressHBox = hBoxForShowUser("Địa chỉ:", user.getAddress());
        HBox phoneNumberHBox = hBoxForShowUser("Số điện thoại:", user.getPhoneNumber());
        HBox emailHBox = hBoxForShowUser("Email:", user.getEmail());
        HBox usernameHBox = hBoxForShowUser("Tên đăng nhập:", user.getUsername());
        HBox passwordHashHBox = hBoxForShowUser("Mật khẩu:", user.getPasswordHash());
        HBox membershipIdHBox = hBoxForShowUser("Mã thẻ:", user.getMembershipId());
        HBox joinDateHBox = hBoxForShowUser("Ngày tạo tài khoản:", user.getJoinDate()+ "");
        HBox membershipStatusHBox = hBoxForShowUser("Trạng thái thẻ:", user.getMembershipStatus());
        HBox roleHBox = hBoxForShowUser("Chức vụ:", user.getRole());
        HBox expiryDateHBox = hBoxForShowUser("Ngày hết hạn thẻ:", user.getExpiryDate()+ "");
        HBox cardRegistrationDateHBox = hBoxForShowUser("Ngày đăng ký thẻ:", user.getCardRegistrationDate()+ "");
        HBox accountStatusHBox = hBoxForShowUser("Trạng thái tài khoản:", user.getAccountStatus());
        HBox genderHBox = hBoxForShowUser("Giới tính:", user.getGender());
        HBox departmentHBox = hBoxForShowUser("Khoa:", user.getDepartment());
        HBox classHBox = hBoxForShowUser("Lớp:", user.getClassName());
        HBox totalBorrowAndDue = totalBorrowAndDue(new Label(user.getTotalBooksBorrowed().toString()), new Label(user.gettotalBooksReturned().toString()));

        Rectangle rectangle = rectangle(screenWidth-60, screenHeight-80, Color.WHITE,
                Color.BLACK, 2, 10,10, 0.8, 30, 30);

        ImageView coverImage;
        if(user.getAvatar()!=null) {
            coverImage = new ImageView(book.getCoverImage());
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        } else {
            Random random = new Random();
            int i = random.nextInt(2);
            String av = UserDAO.gender(id);
            if(av.compareTo("other")==0) {
                av = "male" + i;
            } else {
                av +=i;
            }

            Image image = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/" + av + ".jpg");
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImage = new ImageView(image);
            coverImage.setFitWidth(300);
            coverImage.setFitHeight(400);
        }

        Button backButton = buttonForShowUser(90, 30, "Thoát", "#FF0000", "#8B0000");
        backButton.setOnAction(e->main.showInterfaceScene());
        Button updateButton = buttonForShowUser(90, 30, "Sửa", "#0000FF", "#00008B");

        HBox buttonHBox = new HBox(10, updateButton, backButton);
        buttonHBox.setAlignment(Pos.CENTER_LEFT);
        if(login!=null) {
            Button deleteButton = buttonForShowUser(90, 30, "Xóa TK", "#FF0000", "#8B0000");
            deleteButton.setOnAction(e -> {
                boolean confirmed = Noti.showConfirmationDialog(
                        "Xác nhận xóa",
                        "Bạn có chắc muốn xóa tài khoản?",
                        "Hành động này không thể hoàn tác!"
                );
                if (confirmed) {
                    userDAO.deleteUser(interfaces.userId());
                    login.showLoginScene();
                }
            });

            buttonHBox.getChildren().add(0, deleteButton);
        }

        VBox imageHBox = new VBox(10, coverImage, totalBorrowAndDue, buttonHBox);
        VBox attributeVBox1 = new VBox(10,
                fullNameHBox, genderHBox, dateOfBirthHBox, departmentHBox,
                classHBox, addressHBox, phoneNumberHBox, emailHBox, accountStatusHBox
        );

        VBox attributeVBox2 = new VBox(10,
                userIdHBox, usernameHBox, passwordHashHBox, roleHBox, joinDateHBox,
                membershipIdHBox, cardRegistrationDateHBox, expiryDateHBox, membershipStatusHBox
        );

        Line line1 = line(0,0, 0, 400, Color.BLACK, 1);
        Line line2 = line(0,0, 0, 400, Color.BLACK, 1);

        HBox hBox = new HBox(30,imageHBox, new HBox(5,line1, attributeVBox1, line2 ,attributeVBox2));
        //VBox vBox = new VBox(10,hBox);

        hBox.setLayoutX(50);
        hBox.setLayoutY(50);

        Pane pane = new Pane(rectangle,hBox);

        Scene scene = new Scene(pane, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }

    public Pane statistical(String userId) {
            // Tạo bảng TableView
            TableView<BookAndBorrow> tableView;
            ObservableList<BookAndBorrow> data = FXCollections.observableArrayList();
            List<String> a = new ArrayList<>();
            tableView = Table.tableStatistical(true, 1020, a, this);
            Rectangle rectangle = rectangle(screenWidth-350, screenHeight - 170, Color.WHITE,
                    Color.BLACK, 1, 10, 10, 0.8, -25, -20 );

            // Các trường nhập liệu để thêm dữ liệu mới

            TextField titleField = new TextField();
            titleField.setPromptText("Tiêu đề");
            titleField.setMinWidth(150);
            TextField authorField = new TextField();
            authorField.setPromptText("Tác giả");
            authorField.setMinWidth(150);
            TextField genreField = new TextField();
            genreField.setPromptText("Thể Loại");
            genreField.setMinWidth(150);

        TextField userIdField = new TextField();
        genreField.setPromptText("ID người dùng");
        genreField.setMinWidth(150);
        TextField fullNameField = new TextField();
        genreField.setPromptText("Tên người dùng");
        genreField.setMinWidth(150);


        Label titleTable = new Label("Sách Đã Mượn");
        titleTable.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        VBox titleTableVBox = new VBox(titleTable);
        titleTableVBox.setAlignment(Pos.CENTER);

        AtomicInteger localTable = new AtomicInteger(0);

            TextField serchField = new TextField();
            serchField.setPromptText("Tìm kiếm");
            serchField.setMinWidth(500);

            Button borrowedButton = bookButton("Sách đang mượn");
            Button returneddButton = bookButton("Sách trả đúng hạn");
            Button notBorrowedButton = bookButton("Sách trả quá hạn");
            Button returndButton = button("Trả sách");
            returndButton.setOnAction(e->{
                if(a.size()>0) {
                    boolean check = false;
                    for (String id : a) {
                        check = BorrowDAO.returnBook(userId, id);
                        BorrowDAO.updateQuantity(id, BorrowDAO.remainingQuantity(id)+1);
                    }
                    if (check)
                        Noti.showSuccessMessage("Trả sách thành công");
                    else {
                        Noti.showFailureMessage("Lỗi khi trả sách");
                    }
                } else {
                    Noti.showFailureMessage("Bạn chưa chọn sách để trả");
                }
            });

            HBox bookButton = new HBox(10, borrowedButton, returneddButton, notBorrowedButton);
            bookButton.setAlignment(Pos.CENTER);

            Button searchButton = button("Tìm kiếm");
            searchButton.setOnAction(e->{
                a.clear();
                data.clear();

                book = new Book();
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setGenre(genreField.getText());
                    String keyWord = serchField.getText();
                    ResultSet resultSet = bookDAO.findBooksBorrowed(userId, book, keyWord, localTable.get());
                    if (resultSet != null) {
                        try {
                            while (resultSet.next()) {
                                data.add(new BookAndBorrow(
                                        resultSet.getString("id"),
                                        resultSet.getString("title"),
                                        resultSet.getString("author"),
                                        resultSet.getString("genre"),
                                        getDate(resultSet,"borrow_date"),
                                        getDate(resultSet,"due_date"),
                                        getDate(resultSet,"return_date"),
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
                a.clear();
                data.clear();
                serchField.clear();
                titleField.clear();
                authorField.clear();
                genreField.clear();
                userIdField.clear();
                fullNameField.clear();
            });

            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setItems(data);

            HBox inputLayout = new HBox(10, titleField, authorField, genreField);
            if(UserDAO.getRole(userId).compareTo("admin")==0)
                inputLayout.getChildren().addAll(userIdField, fullNameField);
            inputLayout.setAlignment(Pos.CENTER);
            HBox buttonLayout = new HBox(10, serchField, searchButton, deleteSerchButton, returndButton);
            buttonLayout.setAlignment(Pos.CENTER_LEFT);

            borrowedButton.setOnAction(e->{
                a.clear();
                data.clear();
                localTable.set(0);
                titleTable.setText("Sách Đã Mượn");
                if (!buttonLayout.getChildren().contains(returndButton)) {
                    buttonLayout.getChildren().add(returndButton);
                }
            });
            returneddButton.setOnAction(e->{
                a.clear();
                data.clear();
                localTable.set(1);
                titleTable.setText("Sách Đã Trả");
                if (buttonLayout.getChildren().contains(returndButton)) {
                    buttonLayout.getChildren().remove(returndButton);
                }

            });
            notBorrowedButton.setOnAction(e->{
                a.clear();
                data.clear();
                localTable.set(2);
                titleTable.setText("Sách Quá Hạn");
            if (buttonLayout.getChildren().contains(returndButton)) {
                buttonLayout.getChildren().remove(returndButton);
            }
        });

            VBox buttonLayout2 = new VBox(buttonLayout);
            Pane layout = new Pane(rectangle,new VBox(20, bookButton, inputLayout,
                    buttonLayout2, new VBox(titleTable, tableView)));
            layout.setLayoutY(120);
            return layout;
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

    Button bookButton(String s) {
        Button button0 = new Button(s);
        button0.setStyle("-fx-background-color: #0066CC; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;");
        button0.setMinHeight(15);
        button0.setMinWidth(150);
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
        textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #ccc;-fx-border-radius: 2px;");
        VBox vBox = new VBox(0, label, textField);
        vBox.setMinWidth(500);
        return vBox;
    }
    HBox hBoxForShowUser(String s1, String s2) {
        HBox hBox = new HBox(2);

        // Đặt kích thước tối thiểu và tối đa cho HBox
        hBox.setMinWidth(545);
        hBox.setMaxWidth(545);

        Label label = new Label(s1);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        label.setMinHeight(35);

        TextField textField = new TextField(s2);
        textField.setEditable(false);
        textField.setMinWidth(345);
        textField.setMaxWidth(345);

        // Loại bỏ viền và hiệu ứng của TextField bằng CSS
        textField.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent; " +
                        "-fx-font-size: 15px;"
        );

        hBox.getChildren().addAll(label, textField);
        return hBox;
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
                "-fx-background-color: #FFFFFF; " +
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
                "-fx-background-color: #FFFFFF; " +
                        "-fx-border-color:#ccc #ccc #ccc #ccc; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-padding: 0px; " +
                        "-fx-border-width: 1px;"
        );

        comboBox.setPrefSize(500,28);
        comboBox.setMinSize(500,28);
        comboBox.setMaxSize(500,28);

        VBox vBox = new VBox(0, label, comboBox);
        vBox.setMinWidth(500);

        return vBox;
    }
    public HBox pagesAndDownloads(Label pages, Label downloads, Label rating) {
        Label pagesText = new Label("Trang");
        Label downloadsText = new Label("Lượt mượn");
        Label ratingtext = new Label("Đánh giá");

        // Thiết lập cỡ chữ và màu chữ cho các Label
        pages.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        downloads.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        rating.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        pagesText.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080; -fx-font-weight: bold;");
        downloadsText.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080; -fx-font-weight: bold;");
        ratingtext.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080; -fx-font-weight: bold;");

        VBox lineAndDotVbox1 = lineAndDotVbox(0,0,0,30,Color.GRAY,1);
        VBox lineAndDotVbox2 = lineAndDotVbox(0,0,0,30,Color.GRAY,1);

        VBox pagesVBox = new VBox(pages, pagesText);
        pagesVBox.setAlignment(Pos.CENTER);
        pagesVBox.setMinWidth(180);

        VBox downloadsVBox = new VBox(downloads, downloadsText);
        downloadsVBox.setAlignment(Pos.CENTER);
        downloadsVBox.setMinWidth(180);

        VBox ratingVBox = new VBox(rating, ratingtext);
        ratingVBox.setAlignment(Pos.CENTER);
        ratingVBox.setMinWidth(180);

        // Chỉ hiển thị viền bên phải của các VBox
        pagesVBox.setStyle("-fx-border-color: transparent transparent #808080 transparent;");
        downloadsVBox.setStyle("-fx-border-color: transparent transparent #808080 transparent;");
        ratingVBox.setStyle("-fx-border-color: transparent transparent #808080 transparent;");

        // Tạo Region để bao quanh HBox và tạo viền cho toàn bộ

        // Thiết lập HBox
        HBox hBox = new HBox(pagesVBox, lineAndDotVbox1, downloadsVBox, lineAndDotVbox2, ratingVBox);
        hBox.setMaxWidth(548);
        hBox.setMinWidth(548);
        hBox.setMaxHeight(50);
        hBox.setMinHeight(50);
        hBox.setAlignment(Pos.CENTER);

        return hBox; // Hoặc stackPane nếu cần thiết
    }
    public HBox totalBorrowAndDue(Label totalBorrow, Label totalReturn) {
        Label borrowText = new Label("Số sách đang mượn");
        Label returnText = new Label("Số sách đã trả");

        // Thiết lập cỡ chữ và màu chữ cho các Label
        totalBorrow.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        totalReturn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        borrowText.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080; -fx-font-weight: bold;");
        returnText.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080; -fx-font-weight: bold;");

        VBox lineAndDotVbox1 = lineAndDotVbox(0,0,0,30,Color.GRAY,1);

        VBox borrowVBox = new VBox(totalBorrow, borrowText);
        borrowVBox.setAlignment(Pos.CENTER);
        borrowVBox.setMinWidth(150);

        VBox dueVBox = new VBox(totalReturn, returnText);
        dueVBox.setAlignment(Pos.CENTER);
        dueVBox.setMinWidth(150);

        // Chỉ hiển thị viền bên phải của các VBox
        borrowVBox.setStyle("-fx-border-color: transparent transparent #808080 transparent;");
        dueVBox.setStyle("-fx-border-color: transparent transparent #808080 transparent;");

        // Tạo Region để bao quanh HBox và tạo viền cho toàn bộ

        // Thiết lập HBox
        HBox hBox = new HBox(borrowVBox, lineAndDotVbox1, dueVBox);
        hBox.setMaxWidth(300);
        hBox.setMinWidth(300);
        hBox.setMaxHeight(50);
        hBox.setMinHeight(50);
        hBox.setAlignment(Pos.CENTER);

        return hBox; // Hoặc stackPane nếu cần thiết
    }

    public VBox bookVBox(String id, String title, String author, Image image,Stage primaryStage, interfaces main) {
        Label labelTitle = new Label(title);
        Label labelAuthor = new Label(author);
        ImageView imageView = new ImageView(image);
        // Bo tròn ảnh
        Circle clip = new Circle(10, 10, 10); // Tạo hình tròn có bán kính 75 (nửa chiều rộng/chiều cao của ImageView)
        //imageView.setClip(clip);

        imageView.setFitWidth(150);
        imageView.setFitHeight(200);
        labelTitle.setMaxHeight(40); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelTitle.setMaxWidth(150); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelTitle.setWrapText(true); // Cho phép xuống dòng khi vượt quá chiều rộng
        labelTitle.setEllipsisString("..."); // Thêm dấu "..." khi chữ bị cắt
        labelTitle.setStyle("-fx-text-fill: #000000; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");

        labelAuthor.setMaxHeight(40);
        labelAuthor.setWrapText(true);
        labelAuthor.setStyle("-fx-text-fill: #808080; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        labelAuthor.setMaxHeight(40); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelAuthor.setMaxWidth(150); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng

        labelTitle.setOnMouseEntered(event -> {
            labelTitle.setStyle("-fx-text-fill: #0000FF; -fx-font-family: Arial; -fx-font-size: 15px; -fx-underline: true;");
        });

        labelTitle.setOnMouseExited(event -> {
            labelTitle.setStyle("-fx-text-fill: #000000; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        });

        VBox vBox = new VBox(0, imageView, labelTitle, labelAuthor);
        vBox.setMinWidth(150);
        vBox.setOnMouseClicked(event -> {
            showBook(id);
        });
        return vBox;
    }

    public VBox styledVBox(String s1, String s2) {
        // Tạo Label cho s1 với chữ in đậm
        Label label1 = new Label(s1);
        label1.setStyle("-fx-font-weight: bold;");

        // Tạo TextField cho s2 để hiển thị và chỉ cho phép sao chép
        TextField textField2 = new TextField(s2);
        textField2.setEditable(false);
        textField2.setStyle(
                "-fx-font-size: 15;" +                              // Đặt cỡ chữ là 15
                        "-fx-border-color: #808080;" +                         // Màu viền là gray
                        "-fx-border-width: 1;" +                            // Độ dày viền
                        "-fx-border-radius: 2;" +                           // Bo góc viền
                        "-fx-padding: 4;" +                                 // Đệm trong
                        "-fx-focus-color: transparent;"                     // Ẩn viền khi focus
        );


        // Tạo VBox và thêm Label và TextField vào
        VBox vbox = new VBox(label1, textField2);

        return vbox;
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
                        "-fx-text-fill: #000000;" +
                        "-fx-border-color: #000000;" + // Đặt màu viền là đen
                        "-fx-border-width: 1;" + // Đặt độ dày viền
                        "-fx-focus-color: transparent;" + // Xóa màu focus
                        "-fx-faint-focus-color: transparent;" + // Xóa viền mờ khi focus
                        "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );
        return textArea;
    }

    TextArea textArea(String s) {
        TextArea textArea = new TextArea(s);
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
                        "-fx-text-fill: #000000;" +
                        "-fx-border-color: #000000;" + // Đặt màu viền là đen
                        "-fx-border-width: 1;" + // Đặt độ dày viền
                        "-fx-focus-color: transparent;" + // Xóa màu focus
                        "-fx-faint-focus-color: transparent;" + // Xóa viền mờ khi focus
                        "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );
        return textArea;
    }

    public VBox createCommentTextArea(String bookId,int totalComments, List<VBox> list, CommentPage commentPage) {
        // Tạo Label phía trên TextArea
        Label titleLabel = new Label(totalComments +" bình luận");
        titleLabel.setStyle("-fx-font-size: 16px;");

        // Tạo TextField cho thông báo, thiết lập không chỉnh sửa và cho phép sao chép nội dung
        TextField promptField = new TextField("Mời bạn thảo luận, vui lòng không spam, share link kiếm tiền, thiếu lành mạnh,... để tránh bị khóa tài khoản");
        promptField.setEditable(false);  // Chế độ không chỉnh sửa
        promptField.setMaxWidth(999);
        promptField.setStyle("-fx-text-fill: #808080;" + // Đặt màu chữ là xám
                "-fx-font-style: italic;" +  // Đặt kiểu chữ nghiêng
                "-fx-font-size: 16px;" + // Đặt kích thước chữ là 16px
                "-fx-focus-color: transparent;" + // Xóa màu focus
                "-fx-faint-focus-color: transparent;" + // Xóa viền mờ khi focus
                "-fx-background-color: #FFFFFF;" + // Đặt nền màu trắng
                "-fx-background-insets: 0;" + // Loại bỏ hiệu ứng insets
                "-fx-border-color:#000000 transparent transparent #000000;" // Ẩn viền TextField
        );

        // Tạo TextArea và thiết lập kích thước, cho phép xuống dòng

        TextArea commentText = new TextArea();
        commentText.setMinHeight(75);
        commentText.setMaxHeight(75);
        commentText.setWrapText(true);
        commentText.setStyle("-fx-font-family: 'Arial';" +
                "-fx-font-size: 16px;" +
                "-fx-font-style: italic;" +
                "-fx-text-fill: #000000;" +
                "-fx-border-color: #000000;" + // Đặt màu viền là đen
                "-fx-border-width: 1;" + // Đặt độ dày viền
                "-fx-focus-color: transparent;" + // Xóa màu focus
                "-fx-faint-focus-color: transparent;" + // Xóa viền mờ khi focus
                "-fx-vbar-policy: never;" + /* Ẩn thanh cuộn dọc */
                "-fx-hbar-policy: never;" +
                "-fx-background-insets: 0;" // Đảm bảo không có hiệu ứng insets
        );

        // Đặt promptField vào StackPane với TextArea để hiển thị khi TextArea trống
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(commentText, promptField);

        // Ẩn hiện promptField dựa vào nội dung TextArea
        commentText.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            promptField.setVisible(false);
        });

        // Khi TextArea mất focus mà vẫn trống, ẩn promptField
        commentText.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            promptField.setVisible(!isNowFocused && commentText.getText().isEmpty());
        });

        promptField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            promptField.setVisible(false);
            commentText.requestFocus();
        });

        Button sendButton = new Button("Gửi");

        sendButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #0077FF; -fx-font-size: 16px;");
        sendButton.setOnMouseEntered(event -> {
            sendButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #0055BB; -fx-font-size: 16px;"); // Đổi nền thành xanh đậm hơn khi di chuột vào
        });
        sendButton.setOnMouseExited(event -> {
            sendButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #0077FF; -fx-font-size: 16px;"); // Quay lại nền xanh ban đầu khi chuột ra
        });
        HBox sendButtonHBox =  new HBox(sendButton);
        sendButtonHBox.setAlignment(Pos.CENTER_LEFT);

        HBox titleHBox =  new HBox(titleLabel);
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        VBox titleVbox = new VBox(titleHBox, stackPane);
        stackPane.setAlignment(Pos.TOP_LEFT);
        VBox vbox = new VBox(10, titleVbox, sendButtonHBox);
        sendButton.setOnAction(e-> {
            Comments comments = new Comments();
            comments.setCommentId(CommentsDAO.getMaxCommentId() + 1);
            comments.setUserId(interfaces.userId());
            comments.setBookId(bookId);
            comments.setComment(commentText.getText());
            comments.setTimestamp(LocalDateTime.now()); // Thiết lập thời gian hiện tại
            CommentsDAO.addComments(comments);

            /*vbox.getChildren().add(2,CommentsDAO.createCommentBox(interfaces.userId(),
                    new java.sql.Timestamp(System.currentTimeMillis()), commentText.getText()));*/
            VBox addVBox = CommentsDAO.createCommentBox(UserDAO.accountName(interfaces.userId()),
                    new java.sql.Timestamp(System.currentTimeMillis()), commentText.getText());
            list.add(0, addVBox);
            commentPage.updatePage();
        });

        return vbox;
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
        Integer type0 = resultSet.getInt(type);
        return resultSet.wasNull() ? null : type0;
    }
    public Double getDouble(ResultSet resultSet, String type) throws SQLException {
        Double type0 = resultSet.getDouble(type);
        return resultSet.wasNull() ? null : type0;
    }

    public Button buttonForShowBook(double width, double height, String nameButton, String color1, String color2) {
        // Tạo button mới
        Button button = new Button(nameButton);

        // Thiết lập kích thước của button
        button.setMinWidth(width);
        button.setMinHeight(height);

        // Đặt màu nền ban đầu cho button
        button.setStyle("-fx-background-color: " + color1 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Hiệu ứng khi di chuột vào
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + color2 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            button.setMinWidth(width+2);
            button.setMinHeight(height+2);
        });

        // Hiệu ứng khi di chuột ra
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color1 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            button.setMinWidth(width);
            button.setMinHeight(height);
        });

        return button;
    }
    public Button buttonForShowUser(double width, double height, String nameButton, String color1, String color2) {
        // Tạo button mới
        Button button = new Button(nameButton);

        // Thiết lập kích thước của button
        button.setMinWidth(width);
        button.setMinHeight(height);

        // Đặt màu nền ban đầu cho button
        button.setStyle("-fx-background-color: " + color1 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Hiệu ứng khi di chuột vào
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + color2 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            button.setMinWidth(width);
            button.setMinHeight(height);
        });

        // Hiệu ứng khi di chuột ra
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color1 + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            button.setMinWidth(width);
            button.setMinHeight(height);
        });

        return button;
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

public Line line(double startX, double startY, double endX, double endY, Color color, double strokeWidth) {
    // Tạo đoạn thẳng với các tọa độ đã cho
    Line line = new Line(startX, startY, endX, endY);
    // Đặt màu và độ dày cho đoạn thẳng
    line.setStroke(color);
    line.setStrokeWidth(strokeWidth);
    return line;
}

public Circle dot(double centerX, double centerY, Color color, double radius) {
        // Tạo một vòng tròn (dấu chấm) tại tọa độ đã cho với bán kính và màu sắc
        Circle dot = new Circle(centerX, centerY, radius);
        dot.setFill(color);  // Đặt màu sắc cho dấu chấm
        return dot;
    }
    public VBox lineAndDotVbox(double startX, double startY, double endX, double endY,
                           Color color, double strokeWidth){
        Line line =  line(startX, startY, endX, endY, color, strokeWidth);
        Line dot =  line(0, 0, 2, 0, color, strokeWidth);
        VBox vBox = new VBox(10, line, dot);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        return vBox;
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

