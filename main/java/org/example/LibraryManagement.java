package org.example;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class LibraryManagement {
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = visualBounds.getWidth();
    double screenHeight = visualBounds.getHeight();

    private Document document = null;

    // Phương thức thêm sách
    public void addDocument(Stage primaryStage, interfaces main) {
        //Stage addDocumentStage = new Stage(); // Tạo cửa sổ mới
        //addDocumentStage.setTitle("Thêm Tài Liệu");

        // Đặt chế độ Modality để cửa sổ này phải được đóng trước khi quay lại cửa sổ chính
        //addDocumentStage.initModality(Modality.APPLICATION_MODAL);

        Label idLabel = new Label("ID sách:");
        TextField idField = new TextField();

        // Các Label và TextField để nhập thông tin
        Label titleLabel = new Label("Tên sách:");
        TextField titleField = new TextField();

        Label authorLabel = new Label("Tác giả:");
        TextField authorField = new TextField();

        Label publisherLabel = new Label("Nhà xuất bản:");
        TextField publisherField = new TextField();

        Label yearLabel = new Label("Năm xuất bản:");
        TextField yearField = new TextField();

        Label genreLabel = new Label("Thể loại:");
        TextField genreField = new TextField();

        Label quantityLabel = new Label("số lượng:");
        TextField quantityField = new TextField();

        // Nút Lưu để thêm sách vào database
        Button saveButton = new Button("Lưu");

        saveButton.setOnAction(e -> {

            String id = idField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            int year;
            int quantity;
            String genre = genreField.getText();
            if (id.length() == 0) {
                Noti.showErrorMessage("ID sách không được để trống!", primaryStage);
            } else if (title.length() == 0) {
                Noti.showErrorMessage("Tên sách không được để trống!", primaryStage);
            } else if (author.length() == 0) {
                Noti.showErrorMessage("Tên tác giả không được để trống!", primaryStage);
            } else {
                try {
                    year = Integer.parseInt(yearField.getText());
                    try {
                        quantity = Integer.parseInt(quantityField.getText());
                        document = new Document(id, title, author, publisher, year, genre, quantity);
                        document.addBookToDatabase();
                        if (document.isAvailable()) {
                            Noti.showSuccessMessage("Sách đã được thêm thành công!");
                            main.showMainScene();
                        } else {
                            Noti.showFailureMessage("Lỗi khi thêm sách!");
                        }
                    } catch (NumberFormatException ex) {
                        Noti.showErrorMessage("Vui lòng nhập một số lượng hợp lệ!", primaryStage);
                    }
                } catch (NumberFormatException ex) {
                    Noti.showErrorMessage("Vui lòng nhập một năm hợp lệ!", primaryStage);

                }
            }
        });
        Button cancelButton = new Button("Hủy");
        cancelButton.setOnAction(e -> main.showMainScene());
        // Bố cục cho cửa sổ thêm tài liệu
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                idLabel, idField,
                titleLabel, titleField,
                authorLabel, authorField,
                publisherLabel, publisherField,
                yearLabel, yearField,
                genreLabel, genreField,
                quantityLabel, quantityField,
                saveButton, cancelButton
        );

        Scene scene = new Scene(layout, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        //primaryStage.show();
    }

    // Phương thức xóa sách
    public void removeDocument(Stage primaryStage, interfaces main) {
        Label idLabel = new Label("ID sách:");
        TextField idField = new TextField();

        Button removeButton = new Button("xóa");

        removeButton.setOnAction(e -> {
            String id = idField.getText();
            if (id.length() == 0) {
                Noti.showErrorMessage("ID sách không được để trống!", primaryStage);
            } else {
                document = new Document();
                document.removeBookToDatabase(id);
                if (document.isAvailable()) {
                    Noti.showSuccessMessage("Sách đã được xóa thàng công!");
                    main.showMainScene();
                } else {
                    Noti.showFailureMessage("Lỗi khi xóa sách!");
                }
            }
        });
        Button cancelButton = new Button("Hủy");
        cancelButton.setOnAction(e -> main.showMainScene());
        // Bố cục cho cửa sổ thêm tài liệu
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                idLabel, idField,
                removeButton, cancelButton
        );

        Scene scene = new Scene(layout, screenWidth, screenHeight);
        primaryStage.setScene(scene);
    }

    //phương thức chỉnh sửa sách
    public void updateDocument(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        TableView<Book> tableView = new TableView<>();
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
                        d.getYear(),d.getGenre(),d.getQuantity()
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
    public Pane findDocument(Stage primaryStage, interfaces main) {
        // Tạo bảng TableView
        TableView<Book> tableView = new TableView<>();
        ObservableList<Book> data = FXCollections.observableArrayList();
        tableView = Document.table(false,1000);

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
            document = new Document(idField.getText(), titleField.getText(),authorField.getText(),publisherField.getText(),
                yearField.getText(),genreField.getText(),
                    quantityField.getText());
            List<Document> documents = document.findDocument();
            data.clear();
           for(Document d : documents) {
               data.add(new Book(
                       d.getId(), d.getTitle(), d.getAuthor(),d.getPublisher(),
                       d.getYear(),d.getGenre(),d.getQuantity()
               ));
           }
        });
        Button deleteButton = button("Xóa Tìm kiếm");
        deleteButton.setOnAction(e-> {
            data.clear();
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

        HBox inputLayout = new HBox(10, idField, titleField, authorField, publisherField,
                yearField, genreField, quantityField);

        HBox buttonLayout = new HBox(10, searchButton, deleteButton);
        buttonLayout.setAlignment(Pos.CENTER);
        Pane layout = new Pane(new VBox(20, inputLayout, buttonLayout, tableView));
        layout.setLayoutY(70);
        return layout;
    }

    Button button(String s) {
        Button button0 = new Button(s);
        button0.setStyle("-fx-background-color: #0066CC; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;");
        button0.setMinHeight(15);
        button0.setMinWidth(385);
        button0.setOnMouseEntered(e-> button0.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        button0.setOnMouseExited(e-> button0.setStyle("-fx-background-color:  #0066CC; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        return button0;
    }

}

