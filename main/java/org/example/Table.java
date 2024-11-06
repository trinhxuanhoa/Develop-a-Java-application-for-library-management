package org.example;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.List;

public class Table {
    public static TableView<Book> table(Stage primaryStage, interfaces main, boolean open, double tableWidth, List<String> a, LibraryManagement library) {
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
            if(!isSelected)
                a.clear();
            for (Book book : tableView.getItems()) {
                if(isSelected)
                    a.add(book.getId());
                book.setSelected(isSelected);
            }
            tableView.refresh(); // Cập nhật bảng
        });

        TableColumn<Book, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        idColumn.setMinWidth(55);
        idColumn.setMaxWidth(55);

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

    public static TableColumn tableColumn(String nameColum, String setValue, double width, double hight, boolean isString) {
        if(isString) {
            TableColumn<Book, String> typeColumn = new TableColumn<>(nameColum);
            typeColumn.setCellValueFactory(new PropertyValueFactory<>(setValue));
            typeColumn.setStyle("-fx-alignment: CENTER;");
            typeColumn.setMinWidth(width);
            typeColumn.setMaxWidth(hight);
            return typeColumn;
        } else {
            TableColumn<Book, Integer> typeColumn = new TableColumn<>(nameColum);
            typeColumn.setCellValueFactory(new PropertyValueFactory<>(setValue));
            typeColumn.setStyle("-fx-alignment: CENTER;");
            typeColumn.setMinWidth(width);
            typeColumn.setMaxWidth(hight);
            return typeColumn;
        }
    }
}
