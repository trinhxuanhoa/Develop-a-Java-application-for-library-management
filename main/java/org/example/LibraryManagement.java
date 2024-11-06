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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LibraryManagement {

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();
    private Book book;
    BookDAO bookDAO;

    public LibraryManagement() {
        book = null;
        bookDAO = new BookDAO();
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
        VBox statusVBox = vBox("Trạng thái");
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
        TextField statusField = (TextField)statusVBox.getChildren().get(1);
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
                    imageView.setImage(image); // Hiển thị ảnh lên ImageView
                    imageVBox.getChildren().remove(imgRectangle);
                    imageVBox.setSpacing(0);
                    qrBox.setLayoutX(1150);
                    urlField.setMinWidth(225);
                    if (imageView.getImage() == null)
                        imageVBox.getChildren().addAll(imageView, new Label("Mã số tài liệu: " + isbnTextField.getText()));
                    else {
                        imageVBox.getChildren().remove(imageView);
                        imageVBox.getChildren().addAll(imageView, new Label("Mã số tài liệu: " + isbnTextField.getText()));
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

        Button backButton = new Button("Thoát");
        backButton.setLayoutX(1400);
        backButton.setLayoutY(630);
        backButton.setMinSize(70,50);
        backButton.setOnAction(e -> main.showInterfaceScene());
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        backButton.setOnMouseEntered(e-> backButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        backButton.setOnMouseExited(e-> backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        Button addButton = new Button("Lưu");
        addButton.setLayoutX(1400);
        addButton.setLayoutY(690);
        addButton.setMinSize(70,50);
        addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        addButton.setOnMouseEntered(e-> addButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        addButton.setOnMouseExited(e-> addButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));
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
                    String status = statusField.getText();
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
        Pane layout = new Pane(summaryLabel, textArea, addFeild, imageVBox, hBox, qrBox, backButton, addButton);
        StackPane root = new StackPane();
        root.getChildren().addAll(whiteRectangle, layout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }

    //phương thức chỉnh sửa sách
    public void updateDocument(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        /*TableView<Book> tableView = new TableView<>();
        ObservableList<Book> data = FXCollections.observableArrayList();
        tableView = Document.table(true,screenWidth-80);
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

        Button searchButton = new Button("Tìm kiếm");
        searchButton.setOnAction(e->{
            document = new Document(idField.getText(), titleField.getText(),authorField.getText(),publisherField.getText(),
                    yearField.getText(),genreField.getText(),
                    quantityField.getText());
            List<Document> documents = document.findDocument();
            data.clear();
            for(Document d : documents) {
                data.add(new Book(
                        d.getId(), d.getTitle(), d.getAuthor(),d.getPublisher(),
                        d.getYear(),d.getGenre(),d.getQuantity(),""
                ));
            }
        });
        Button deleteButton = new Button("xóa");
        deleteButton.setOnAction(e-> {
            idField.clear();
            titleField.clear();
            quantityField.clear();
            publisherField.clear();
            yearField.clear();
            genreField.clear();
            quantityField.clear();
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(data);

        Button cancelButton = new Button("Thoát");
        cancelButton.setOnAction(e->main.showMainScene());
        HBox inputLayout = new HBox(10, idField, titleField, authorField, publisherField,
                yearField, genreField, quantityField);
        HBox buttonLayout = new HBox(10, searchButton, deleteButton, cancelButton);
        VBox layout = new VBox(10, inputLayout, buttonLayout, tableView);

        // Căn chỉnh các thành phần
        layout.setPadding(new Insets(40));
        inputLayout.setAlignment(Pos.CENTER);
        buttonLayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,screenWidth,screenHeight);
        primaryStage.setScene(scene);

/*
        Label idLabelf = new Label("nhập ID sách cần sửa");
        TextField idFieldf = new TextField();

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Hủy");

        cancelButton.setOnAction(e -> main.showMainScene());

        okButton.setOnAction(e -> {
            Button updateButton = new Button("sửa");
            String id = idFieldf.getText();
            document.output1Value(id);
            Label idLabel0 = new Label("ID sách: " + document.getId());
            Label titleLabel0 = new Label("Tên sách: " + document.getTitle());
            Label authorLabel0 = new Label("Tác giả: " + document.getAuthor());
            Label publisherLabel0 = new Label("Nhà xuất bản: " + document.getPublisher());
            Label yearLabel0 = new Label("Năm xuất bản: " + document.getYear());
            Label genreLabel0 = new Label("Thể loại: " + document.getGenre());
            Label quantityLabel0 = new Label("số lượng: " + document.getQuantity());

            Label idLabel = new Label("Không thể thay đổi ID ");
            Label titleLabel = new Label("Tên sách mới:");
            TextField titleField = new TextField();
            Label authorLabel = new Label("Tác giả mới:");
            TextField authorField = new TextField();
            Label publisherLabel = new Label("Nhà xuất bản mới:");
            TextField publisherField = new TextField();
            Label yearLabel = new Label("Năm xuất bản mới:");
            TextField yearField = new TextField();
            Label genreLabel = new Label("Thể loại mới:");
            TextField genreField = new TextField();
            Label quantityLabel = new Label("số lượng mới:");
            TextField quantityField = new TextField();
            updateButton.setOnAction(e1 -> {
                boolean check = false;
                if (!titleField.getText().isEmpty()) {
                    check = true;
                    document.setTitle(titleField.getText());
                }
                if (!authorField.getText().isEmpty()) {
                    check = true;
                    document.setAuthor(authorField.getText());
                }
                if (!publisherField.getText().isEmpty()) {
                    check = true;
                    document.setPublisher(publisherField.getText());
                }
                if (!genreField.getText().isEmpty()) {
                    check = true;
                    document.setGenre(genreField.getText());
                }
                if (!yearField.getText().isEmpty())
                    try {
                        document.setYear(Integer.parseInt(yearField.getText()));
                        if (!quantityField.getText().isEmpty())
                            try {
                                document.setQuantity(Integer.parseInt(quantityField.getText()));
                            } catch (NumberFormatException ex) {
                                Noti.showErrorMessage("Vui lòng nhập một số lượng hợp lệ", primaryStage);
                            }
                    } catch (NumberFormatException ex) {
                        Noti.showErrorMessage("Vui lòng nhập một năm hợp lệ", primaryStage);
                    }

                if(check) {
                    document.updateBookToDatabase();
                    if (document.isAvailable()) {
                        Noti.showSuccessMessage("Sửa sách thành công!");
                        main.showMainScene();
                    } else
                        Noti.showFailureMessage("Sửa sách thất bại!");
                }

            });
            VBox layout1 = new VBox(10);
            layout1.getChildren().addAll(
                    idLabel0, titleLabel0,
                    authorLabel0, publisherLabel0,
                    yearLabel0, genreLabel0,
                    quantityLabel0,

                    idLabel,
                    titleLabel, titleField,
                    authorLabel, authorField,
                    publisherLabel, publisherField,
                    yearLabel, yearField,
                    genreLabel, genreField,
                    quantityLabel, quantityField,
                    updateButton, cancelButton
            );
            Scene scene1 = new Scene(layout1, 1500, 750);
            primaryStage.setScene(scene1);
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                idLabelf, idFieldf,
                okButton, cancelButton
        );

        Scene scene = new Scene(layout, 1500, 750);
        primaryStage.setScene(scene);
*/
    }

    //phương thức tìm sách và show toàn bộ sách
    public Pane managerDocument(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        TableView<Book> tableView;
        ObservableList<Book> data = FXCollections.observableArrayList();
        List<String> a = new ArrayList<>();
        tableView = Table.table(primaryStage, main, true, 1000, a, this);

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


        Button searchButton = button("Tìm kiếm");
        searchButton.setOnAction(e->{
            data.clear();
            try {
                int year = Integer.parseInt(yearField.getText());
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    book = new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(),
                            year, genreField.getText(), quantity);
                } catch (NumberFormatException ex){
                    book = new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(),
                            year, genreField.getText());
                }
            } catch (NumberFormatException ex1) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    book = new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(),
                            genreField.getText(), quantity);
                } catch (NumberFormatException ex2){
                    book = new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(),
                            genreField.getText());
                }
            }
            ResultSet resultSet = bookDAO.findBooks(book);
            try  {
                while (resultSet.next()) {
                    data.add(new Book(
                            false,
                            resultSet.getString("id"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("publisher"),
                            getInt(resultSet,"year"),
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
            updateDocument(primaryStage,main);
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

        HBox buttonLayout = new HBox(10, searchButton, deleteSerchButton);
        buttonLayout.setAlignment(Pos.CENTER);
        VBox buttonLayout2 = new VBox( buttonLayout, buttonLayout1);
        Pane layout = new Pane(new VBox(20, inputLayout, buttonLayout2, tableView));
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
        VBox vBox = new VBox(0, label, textField);
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

