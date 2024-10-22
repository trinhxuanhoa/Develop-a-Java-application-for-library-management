package org.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryManagement {

    private Document document = null;

    // Phương thức thêm sách
    public void addDocument(Stage primaryStage,Main main) {
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
            if(id.length()==0) {
                Noti.showErrorMessage("ID sách không được để trống!", primaryStage);
            } else if(title.length()==0) {
                Noti.showErrorMessage("Tên sách không được để trống!", primaryStage);
            } else if(author.length()==0) {
                Noti.showErrorMessage("Tên tác giả không được để trống!", primaryStage);
            } else {
                try {
                    year = Integer.parseInt(yearField.getText());
                    try {
                        quantity = Integer.parseInt(quantityField.getText());
                        document = new Document(id, title, author, publisher, year, genre,quantity);
                        document.addBookToDatabase();
                        if(document.isAvailable()) {
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
                saveButton,cancelButton
        );

        Scene scene = new Scene(layout, 1500, 750);
        primaryStage.setScene(scene);
        //primaryStage.show();
    }

    // Phương thức xóa sách
    public void removeDocument(Stage primaryStage,Main main) {
        Label idLabel = new Label("ID sách:");
        TextField idField = new TextField();

        Button removeButton = new Button("xóa");

        removeButton.setOnAction(e -> {
            String id = idField.getText();
            if(id.length()==0) {
                Noti.showErrorMessage("ID sách không được để trống!", primaryStage);
            } else {
                document = new Document();
                document.removeBookToDatabase(id);
                if(document.isAvailable()) {
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
               removeButton,cancelButton
        );

        Scene scene = new Scene(layout, 1500, 750);
        primaryStage.setScene(scene);
    }

    //phương thức chỉnh sửa sách
    public void updateDocument(Stage primaryStage,Main main) {
        document = new Document();

        Label idLabelf = new Label("nhập ID sách cần sửa");
        TextField idFieldf = new TextField();

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Hủy");

        cancelButton.setOnAction(e->main.showMainScene());

        okButton.setOnAction(e-> {
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
            updateButton.setOnAction(e1->{
            if (!titleField.getText().isEmpty()) {
                document.setTitle(titleField.getText());
                System.out.println(document.getTitle());
            }
            if (!authorField.getText().isEmpty())
                document.setAuthor(authorField.getText());
            if (!publisherField.getText().isEmpty())
                document.setPublisher(publisherField.getText());

            if (!yearField.getText().isEmpty())
            try {
                    document.setYear(Integer.parseInt(yearField.getText()));
                    if(!quantityField.getText().isEmpty())
                try {
                    document.setQuantity(Integer.parseInt(quantityField.getText()));
                } catch (NumberFormatException ex) {
                    Noti.showErrorMessage("Vui lòng nhập một số lượng hợp lệ",primaryStage);
                }
            } catch (NumberFormatException ex) {
                Noti.showErrorMessage("Vui lòng nhập một năm hợp lệ",primaryStage);
            }


            document.updateBookToDatabase();
            if (document.isAvailable()) {
                Noti.showSuccessMessage("Sửa sách thành công!");
                main.showMainScene();
            }
            else
                Noti.showFailureMessage("Sửa sách thất bại!");

        });
            VBox layout1 = new VBox(10);
            layout1.getChildren().addAll(
                    idLabel0, titleLabel0,
                    authorLabel0, publisherLabel0,
                    yearLabel0, genreLabel0,
                    quantityLabel0,

                    idLabel,
                    titleLabel,titleField ,
                    authorLabel,authorField,
                    publisherLabel,publisherField,
                    yearLabel,yearField,
                    genreLabel,genreField,
                    quantityLabel,quantityField,
                    updateButton, cancelButton
            );
            Scene scene1 = new Scene(layout1, 1500, 750);
            primaryStage.setScene(scene1);
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                idLabelf, idFieldf,
                okButton,cancelButton
        );

        Scene scene = new Scene(layout, 1500, 750);
        primaryStage.setScene(scene);

    }
}

