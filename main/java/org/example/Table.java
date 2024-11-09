package org.example;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.List;
import java.time.LocalDate;
public class Table {
    public static TableView<Book> tableDocument(Stage primaryStage, interfaces main, boolean open, double tableWidth, List<String> a, LibraryManagement library) {
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
                label.setStyle("-fx-text-fill: blue;"); // Màu chữ
                // Thêm sự kiện di chuột để gạch chân
                label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: blue; -fx-underline: true;"));
                label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: blue; -fx-underline: false;"));

                // Thiết lập sự kiện nhấp chuột
                label.setOnMouseClicked(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    library.showBook(primaryStage, main, book.getId());
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

    public static TableView<User> tableUser(Stage primaryStage, interfaces main, boolean open, double tableWidth, List<String> a, LibraryManagement library) {
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

        TableColumn<User, String> userIdColumn = tableColumn("ID", "userId", 55, 55, true);
        TableColumn<User, String> fullNameColumn = tableColumn("Họ và Tên", "fullName", 200, 200, true);
        TableColumn<User, String> phoneNumberColumn = tableColumn("Số Điện Thoại", "phoneNumber", 150, 150, true);
        TableColumn<User, String> emailColumn = tableColumn("Email", "email", 200, 200, true);
        TableColumn<User, String> dateOfBirthColumn = tableColumnDate("Ngày Sinh", "dateOfBirth", 80, 80);
        TableColumn<User, Integer> totalBooksBorrowedColumn = tableColumn("Số Sách Đã Mượn", "totalBooksBorrowed", 175, 175, false);
        TableColumn<User, String> statusColumn = tableColumn("Trạng Thái Thẻ", "membershipStatus", tableWidth-930, 5000, true);

        TableColumn<User, String> detailCol = new TableColumn<>("Xem Thêm");
        detailCol.setMinWidth(70);
        detailCol.setMaxWidth(70);
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setCellFactory(col -> new TableCell<User, String>() {
            private final Label label = new Label("Chi tiết");
            private final HBox hBox = new HBox(label);
            {
                hBox.setAlignment(Pos.CENTER);
                label.setStyle("-fx-text-fill: blue;"); // Màu chữ
                // Thêm sự kiện di chuột để gạch chân
                label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: blue; -fx-underline: true;"));
                label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: blue; -fx-underline: false;"));

                // Thiết lập sự kiện nhấp chuột
                label.setOnMouseClicked(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    library.showUser(primaryStage, main, user.getUserId());
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
