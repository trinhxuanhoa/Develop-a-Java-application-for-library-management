package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;

import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Polygon;

public class Table {
    public static TableView<Book> tableDocument(boolean open, double tableWidth, List<String> a, LibraryManagement library) {
        TableView<Book> tableView = new TableView<>();
        tableView.setEditable(open); // Bật chế độ chỉnh sửa

        // Tạo các cột
        TableColumn<Book, Boolean> selectColumn = new TableColumn<>();
        CheckBox headerCheckBox = new CheckBox();

        // Thiết lập tiêu đề cột là CheckBox
        selectColumn.setGraphic(headerCheckBox);
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setStyle("-fx-alignment: CENTER;");
        selectColumn.setMinWidth(20);
        selectColumn.setMaxWidth(20);
        headerCheckBox.setOnAction(event -> {
            boolean isSelected = headerCheckBox.isSelected();

                a.clear();
            for (Book book : tableView.getItems()) {
                book.setSelected(isSelected);
            }
            tableView.refresh(); // Cập nhật bảng
        });

        TableColumn<Book, String> idColumn = tableColumn("ID", "id", 55, 55, true);
        TableColumn<Book, String> titleColumn = tableColumn("Tên Sách", "title", tableWidth - 670, 5000, true);
        TableColumn<Book, String> authorColumn = tableColumn("Tác Giả", "author", 150, 150, true);
        TableColumn<Book, String> publisherColumn = tableColumn("Nhà Xuất Bản", "publisher", 150, 150,  true);
        TableColumn<Book, Integer> yearColumn = tableColumn("Năm", "year", 50, 50, false);
        TableColumn<Book, String> genreColumn = tableColumn("Thể Loại", "genre", 100, 100, true);
        TableColumn<Book, Integer> quantityColumn = tableColumn("Số Lượng", "quantity", 75, 75, false);
        TableColumn<Book, String> detailCol = new TableColumn<>("Xem Thêm");
        detailCol.setMinWidth(70);
        detailCol.setMaxWidth(70);
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setCellFactory(col -> new TableCell<Book, String>() {
            private final Label label = new Label("Chi tiết");
            private final HBox hBox = new HBox(label);
            {
                hBox.setAlignment(Pos.CENTER);
                label.setStyle("-fx-text-fill: #0000FF;"); // Màu chữ
                // Thêm sự kiện di chuột để gạch chân
                label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: true;"));
                label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: false;"));

                // Thiết lập sự kiện nhấp chuột
                label.setOnMouseClicked(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    library.showBook(book.getId());
                    System.out.println("Chi tiết cho: " + book.getTitle());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); // Không hiển thị label khi ô trống
                } else {
                    label.setText(item);
                    setGraphic(hBox); // Hiển thị label
                    setText(null); // Đặt text thành null để không hiển thị văn bản mặc định của ô
                }
            }
        });

        // Thêm tất cả các cột vào bảng
        tableView.getColumns().addAll(selectColumn, idColumn, titleColumn, authorColumn,
                publisherColumn, yearColumn, genreColumn, quantityColumn, detailCol);
        return tableView;
    }

    public static TableView<User> tableUser(boolean open, double tableWidth, List<String> a, LibraryManagement library, Login login) {
        TableView<User> tableView = new TableView<>();
        tableView.setEditable(open); // Bật chế độ chỉnh sửa

        // Tạo các cột
        TableColumn<User, Boolean> selectColumn = new TableColumn<>();
        CheckBox headerCheckBox = new CheckBox();

        // Thiết lập tiêu đề cột là CheckBox
        selectColumn.setGraphic(headerCheckBox);
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setStyle("-fx-alignment: CENTER;");
        selectColumn.setMinWidth(20);
        selectColumn.setMaxWidth(20);
        headerCheckBox.setOnAction(event -> {
            boolean isSelected = headerCheckBox.isSelected();
            a.clear();

            for (User user : tableView.getItems()) {
                user.setSelected(isSelected);
                   // a.add(user.getUserId());
            }

            tableView.refresh(); // Cập nhật bảng
        });

        TableColumn<User, String> userIdColumn = tableColumn("ID Người dùng", "userId", 105, 105, true);
        TableColumn<User, String> fullNameColumn = tableColumn("Họ và Tên", "fullName", tableWidth-830, 5000, true);
        TableColumn<User, String> phoneNumberColumn = tableColumn("Số Điện Thoại", "phoneNumber", 150, 150, true);
        TableColumn<User, String> emailColumn = tableColumn("Email", "email", 225, 225, true);
        TableColumn<User, String> dateOfBirthColumn = tableColumnDate("Ngày Sinh", "dateOfBirth", 80, 80);
        TableColumn<User, Integer> totalBooksBorrowedColumn = tableColumn("Sách Đã Mượn", "totalBooksBorrowed", 100, 100, false);
        TableColumn<User, String> statusColumn = tableColumn("Trạng Thái Thẻ", "membershipStatus", 100, 100, true);

        TableColumn<User, String> detailCol = new TableColumn<>("Xem Thêm");
        detailCol.setMinWidth(70);
        detailCol.setMaxWidth(70);
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setCellFactory(col -> new TableCell<User, String>() {
            private final Label label = new Label("Chi tiết");
            private final HBox hBox = new HBox(label);
            {
                hBox.setAlignment(Pos.CENTER);
                label.setStyle("-fx-text-fill: #0000FF;"); // Màu chữ
                // Thêm sự kiện di chuột để gạch chân
                label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: true;"));
                label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: false;"));

                // Thiết lập sự kiện nhấp chuột
                label.setOnMouseClicked(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    library.showUser(user.getUserId(), login);
                    System.out.println("Chi tiết cho: " + user.getFullName());
                });
            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); // Không hiển thị label khi ô trống
                } else {
                    label.setText(item);
                    setGraphic(hBox); // Hiển thị label
                    setText(null); // Đặt text thành null để không hiển thị văn bản mặc định của ô
                }
            }
        });

        // Thêm tất cả các cột vào bảng
        tableView.getColumns().addAll(selectColumn, userIdColumn, fullNameColumn, phoneNumberColumn,
                emailColumn, dateOfBirthColumn, totalBooksBorrowedColumn, statusColumn, detailCol);
        return tableView;
    }

    public static TableView<BookAndBorrow> tableStatistical(boolean open, double tableWidth, List<String> a, LibraryManagement library, boolean role) {
        TableView<BookAndBorrow> tableView = new TableView<>();
        tableView.setEditable(open); // Bật chế độ chỉnh sửa

        // Tạo các cột
        TableColumn<BookAndBorrow, Boolean> selectColumn = new TableColumn<>();
        CheckBox headerCheckBox = new CheckBox();

        // Thiết lập tiêu đề cột là CheckBox
        selectColumn.setGraphic(headerCheckBox);
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setStyle("-fx-alignment: CENTER;");
        selectColumn.setMinWidth(20);
        selectColumn.setMaxWidth(20);
        headerCheckBox.setOnAction(event -> {
            boolean isSelected = headerCheckBox.isSelected();

            a.clear();
            for (BookAndBorrow book : tableView.getItems()) {
                book.setSelected(isSelected);
            }
            tableView.refresh(); // Cập nhật bảng
        });

        TableColumn<BookAndBorrow, String> idColumn = tableColumn("ID Sách", "id", 55, 55, true);
        TableColumn<BookAndBorrow, String> titleColumn;
        if(role)
            titleColumn = tableColumn("Tên Sách", "title", tableWidth - 800, 5000, true);
        else
            titleColumn = tableColumn("Tên Sách", "title", tableWidth - 675, 5000, true);
        TableColumn<BookAndBorrow, String> authorColumn = tableColumn("Tác Giả", "author", 150, 150, true);
        TableColumn<BookAndBorrow, String> genreColumn = tableColumn("Thể Loại", "genre", 100, 100, true);
        TableColumn<BookAndBorrow, String> borrowDateColumn = tableColumnDate("Ngày Mượn", "borrowDate", 100, 100);
        TableColumn<BookAndBorrow, String> dueDateColumn = tableColumnDate("Hạn Trả", "dueDate", 100, 100);
        TableColumn<BookAndBorrow, String> returnDateColumn = tableColumnDate("Ngày Trả", "returnDate", 100, 100);
        TableColumn<BookAndBorrow, String> userIdColumn = tableColumn("ID", "userId", 55, 55, true);
        TableColumn<BookAndBorrow, String> fullNameColumn = tableColumn("Họ Và Tên", "fullName", 150, 150, true);
        TableColumn<BookAndBorrow, String> detailCol = new TableColumn<>("Xem Thêm");
        detailCol.setMinWidth(70);
        detailCol.setMaxWidth(70);
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setCellFactory(col -> new TableCell<BookAndBorrow, String>() {
            private final Label label = new Label("Chi tiết");
            private final HBox hBox = new HBox(label);
            {
                hBox.setAlignment(Pos.CENTER);
                label.setStyle("-fx-text-fill: #0000FF;"); // Màu chữ
                // Thêm sự kiện di chuột để gạch chân
                label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: true;"));
                label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #0000FF; -fx-underline: false;"));

                // Thiết lập sự kiện nhấp chuột
                label.setOnMouseClicked(e -> {
                    BookAndBorrow book = getTableView().getItems().get(getIndex());
                    library.showBook(book.getId());
                    System.out.println("Chi tiết cho: " + book.getTitle());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); // Không hiển thị label khi ô trống
                } else {
                    label.setText(item);
                    setGraphic(hBox); // Hiển thị label
                    setText(null); // Đặt text thành null để không hiển thị văn bản mặc định của ô
                }
            }
        });

        // Thêm tất cả các cột vào bảng
        if(role)
            tableView.getColumns().addAll(selectColumn, userIdColumn, fullNameColumn, idColumn,
                    titleColumn, authorColumn, borrowDateColumn, returnDateColumn, dueDateColumn, detailCol);
        else
            tableView.getColumns().addAll(selectColumn, idColumn, titleColumn, authorColumn,
                genreColumn, borrowDateColumn, returnDateColumn, dueDateColumn, detailCol);

        return tableView;
    }

    public static TableView<TopUser> tableTopUser(double tableWidth) {
        TableView<TopUser> tableView = new TableView<>();
        tableView.setMinWidth(tableWidth);
        tableView.setMaxWidth(tableWidth);
        tableView.setMinHeight(tableWidth);
        tableView.setMaxHeight(tableWidth);
        // Cột đánh số
        TableColumn<TopUser, Integer> numberColumn = new TableColumn<>("STT");
        numberColumn.setCellValueFactory((TableColumn.CellDataFeatures<TopUser, Integer> param) ->
                new SimpleIntegerProperty(tableView.getItems().indexOf(param.getValue()) + 1).asObject());
        numberColumn.setMinWidth(30);
        numberColumn.setMaxWidth(30);
        numberColumn.setStyle("-fx-alignment: CENTER;");
        // Cột user_id
        TableColumn<TopUser, String> userIdColumn = new TableColumn<>("Mã Người Dùng");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userIdColumn.setMinWidth(150);
        userIdColumn.setMaxWidth(150);
        userIdColumn.setStyle("-fx-alignment: CENTER;");
        // Cột fullName
        TableColumn<TopUser, String> fullNameColumn = new TableColumn<>("Tên Người Dùng");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setMinWidth(tableWidth-230);
        fullNameColumn.setMaxWidth(tableWidth-230);
        fullNameColumn.setStyle("-fx-alignment: CENTER;");
        // Cột tổng số sách đã mượn
        TableColumn<TopUser, Integer> totalBooksColumn = new TableColumn<>("Mượn");
        totalBooksColumn.setCellValueFactory(new PropertyValueFactory<>("totalBooksBorrowed"));
        totalBooksColumn.setMinWidth(50);
        totalBooksColumn.setMaxWidth(50);
        totalBooksColumn.setStyle("-fx-alignment: CENTER;");
        // Thêm tất cả các cột vào bảng
        tableView.getColumns().addAll(numberColumn, userIdColumn, fullNameColumn, totalBooksColumn);

        return tableView;
    }

    public static TableView<TopBook> tableTopBook(double tableWidth) {
        TableView<TopBook> tableView = new TableView<>();
        tableView.setMinWidth(tableWidth);
        tableView.setMaxWidth(tableWidth);
        tableView.setMinHeight(tableWidth);
        tableView.setMaxHeight(tableWidth);
        // Cột đánh số
        TableColumn<TopBook, Integer> numberColumn = new TableColumn<>("STT");
        numberColumn.setCellValueFactory((TableColumn.CellDataFeatures<TopBook, Integer> param) ->
                new SimpleIntegerProperty(tableView.getItems().indexOf(param.getValue()) + 1).asObject());
        numberColumn.setMinWidth(30);
        numberColumn.setMaxWidth(30);
        numberColumn.setStyle("-fx-alignment: CENTER;");
        // Cột book_id
        TableColumn<TopBook, String> bookIdColumn = new TableColumn<>("Mã Sách");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookIdColumn.setMinWidth(100);
        bookIdColumn.setMaxWidth(100);
        bookIdColumn.setStyle("-fx-alignment: CENTER;");
        // Cột title
        TableColumn<TopBook, String> titleColumn = new TableColumn<>("Tên Sách");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(tableWidth-230);
        titleColumn.setMaxWidth(tableWidth-230);
        titleColumn.setStyle("-fx-alignment: CENTER;");
        // cột ratting
        TableColumn<TopBook, Double> ratingColumn = new TableColumn<>("");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setMinWidth(50);
        ratingColumn.setMaxWidth(50);
        ratingColumn.setStyle("-fx-alignment: CENTER;");
        // Thêm ngôi sao vào tiêu đề cột
        Polygon star = Star.star(7, Color.BLACK);  // Tạo ngôi sao với bán kính 10
        Label titleLabel = new Label("", star);  // Đặt ngôi sao vào Label
        titleLabel.setStyle("-fx-alignment: CENTER;");

        ratingColumn.setGraphic(titleLabel);  // Đặt Label chứa ngôi sao làm tiêu đề cột

        // Tùy chỉnh cell để hiển thị ngôi sao bên dưới đánh giá
        ratingColumn.setCellFactory(param -> new TableCell<TopBook, Double>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);

                if (empty || rating == null) {
                    setGraphic(null);
                    return;
                }

                // Tạo ngôi sao từ phương thức star
                Polygon star = Star.star(6, Color.GRAY);  // Tạo ngôi sao với bán kính 10
                HBox hbox = new HBox(star);  // Đặt ngôi sao vào một hộp
                hbox.setAlignment(Pos.BOTTOM_CENTER);
                // Thêm giá trị rating vào HBox
                Label ratingText = new Label(String.format("%.1f", rating));  // Hiển thị giá trị đánh giá
                hbox.getChildren().add(ratingText);

                int numStars = (int) Math.round(rating);  // Làm tròn số đánh giá
                for (int i = 0; i < numStars; i++) {
                    HBox starContainer = new HBox(star);  // Tạo ngôi sao
                    hbox.getChildren().add(starContainer);
                }
                setGraphic(hbox);
            }
        });
        // Cột tổng lượt mượn
        TableColumn<TopBook, Integer> totalBorrowsColumn = new TableColumn<>("Mượn");
        totalBorrowsColumn.setCellValueFactory(new PropertyValueFactory<>("totalBorrows"));
        totalBorrowsColumn.setMinWidth(50);
        totalBorrowsColumn.setMaxWidth(50);
        totalBorrowsColumn.setStyle("-fx-alignment: CENTER;");
        // Thêm tất cả các cột vào bảng
        tableView.getColumns().addAll(numberColumn, bookIdColumn, titleColumn, ratingColumn, totalBorrowsColumn);

        return tableView;
    }

    public static TableColumn tableColumn(String nameColum, String setValue, double width, double hight, boolean isString) {
        if(isString) {
            TableColumn<User, String> typeColumn = new TableColumn<>(nameColum);
            typeColumn.setCellValueFactory(new PropertyValueFactory<>(setValue));
            typeColumn.setStyle("-fx-alignment: CENTER;");
            typeColumn.setMinWidth(width);
            typeColumn.setMaxWidth(hight);
            return typeColumn;
        } else {
            TableColumn<User, Integer> typeColumn = new TableColumn<>(nameColum);
            typeColumn.setCellValueFactory(new PropertyValueFactory<>(setValue));
            typeColumn.setStyle("-fx-alignment: CENTER;");
            typeColumn.setMinWidth(width);
            typeColumn.setMaxWidth(hight);
            return typeColumn;
        }
    }
    public static  TableColumn tableColumnDate(String nameColum, String setValue, double width, double hight) {
        TableColumn<User, LocalDate> typeColumn = new TableColumn<>(nameColum);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>(setValue));
        typeColumn.setStyle("-fx-alignment: CENTER;");
        typeColumn.setMinWidth(width);
        typeColumn.setMaxWidth(hight);
        return typeColumn;
    }
}
