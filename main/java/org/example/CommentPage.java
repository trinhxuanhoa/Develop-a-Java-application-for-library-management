package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;

public class CommentPage {

    private static final int ITEMS_PER_PAGE = 5;  // Số phần tử mỗi trang
    private List<VBox> allComments; // List chứa tất cả các bình luận (VBox)
    private FlowPane flowPane = new FlowPane();  // FlowPane để chứa các VBox
    private HBox paginationBox = new HBox(5);  // HBox để chứa các nút phân trang
    private int currentPage = 0;  // Trang hiện tại
    private ScrollPane scrollPane;
    private double currentScrollPosition;
    private boolean check;
    public CommentPage(List<VBox> allComments) {
        this.allComments = allComments;
        flowPane.setVgap(20);
        currentScrollPosition = 0.70;
        check = false;
    }

    // Hàm khởi tạo giao diện và các phần tử phân trang
    public VBox commentPage() {
        updatePage();
        // Thêm FlowPane và paginationBox vào VBox
        VBox root = new VBox(10);
        root.getChildren().addAll(flowPane, paginationBox);
        return  root;
    }

    // Hàm cập nhật nội dung của trang dựa trên currentPage
    public void updatePage() {
        // Lưu vị trí cuộn hiện tại
       double currentScrollPosition = scrollPane.getVvalue();
        flowPane.getChildren().clear(); // Xóa các phần tử cũ

        // Tính toán các phần tử cần hiển thị cho trang hiện tại
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allComments.size());

        // Thêm các VBox tương ứng vào FlowPane
        for (int i = start; i < end; i++) {
            flowPane.getChildren().add(allComments.get(i));
        }

        // Cập nhật các nút phân trang
        updatePagination();
        // Đảm bảo rằng ScrollPane giữ nguyên vị trí cuộn
        // Nếu bạn muốn cuộn mượt, bạn có thể sử dụng Timeline ở đây
        /*if (check) {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(500), // Thời gian cuộn mượt
                            new KeyValue(scrollPane.vvalueProperty(), currentScrollPosition) // Quay lại vị trí cuộn cũ
                    )
            );
            timeline.play(); // Chạy hiệu ứng cuộn mượt
        } else {
            check = true; // Lần đầu không cuộn mượt
        }*/
    }

    // Hàm cập nhật các nút phân trang
    private void updatePagination() {
        paginationBox.getChildren().clear();  // Xóa các nút phân trang cũ
        paginationBox.setStyle("-fx-alignment: center; -fx-padding: 10px;");
        paginationBox.getChildren().forEach(button -> {
            button.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-radius: 5px;");
        });

        int totalPages = (int) Math.ceil((double) allComments.size() / ITEMS_PER_PAGE); // Tổng số trang
        int maxPage = Math.min(totalPages, 10);  // Chỉ hiển thị tối đa 10 trang trên thanh phân trang

        // Thêm nút "Trang 1"
        Button firstPageButton = new Button("1");
        firstPageButton.setOnAction(e -> {
            
            currentPage = 0;  // Chuyển đến trang 1
            updatePage();
        });
        paginationBox.getChildren().add(firstPageButton);

        // Thêm nút "Trang trước"
        if (currentPage > 0) {
            Button prevButton = new Button("Prev");
            prevButton.setOnAction(e -> {
                currentPage--;  // Quay lại trang trước
                updatePage();
            });
            paginationBox.getChildren().add(prevButton);
        }

        // Tạo các nút phân trang từ 1 đến 10 (hoặc nhiều hơn nếu có)
        for (int i = 0; i < maxPage; i++) {
            int pageIndex = i + 1;
            Button pageButton = new Button(String.valueOf(pageIndex));
            pageButton.setOnAction(e -> {
                currentPage = pageIndex - 1;  // Chuyển đến trang tương ứng
                updatePage();
            });
            paginationBox.getChildren().add(pageButton);
        }

        // Thêm nút "Trang sau"
        if (currentPage + 1 < totalPages) {
            Button nextButton = new Button("Next");
            nextButton.setOnAction(e -> {
                if (currentPage + 1 < totalPages) {
                    currentPage++;
                    updatePage();
                }
            });
            paginationBox.getChildren().add(nextButton);
        }

        // Thêm nút "Trang cuối"
        Button lastPageButton = new Button(String.valueOf(totalPages));
        lastPageButton.setOnAction(e -> {
            
            currentPage = totalPages - 1;  // Chuyển đến trang cuối cùng
            updatePage();
        });
        paginationBox.getChildren().add(lastPageButton);

        if (check) {
            // Tạo hiệu ứng cuộn mượt
            //double currentScrollPosition = scrollPane.getVvalue(); // Lấy vị trí hiện tại
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(500), // Thời gian cuộn
                            new KeyValue(scrollPane.vvalueProperty(), currentScrollPosition) // Cuộn đến cuối (1.0)
                    )
            );
            timeline.play();

        } else {
            check = true; // Lần đầu không cuộn
        }

    }
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    // Hàm trả về FlowPane chứa các bình luận
    public FlowPane getFlowPane() {
        return flowPane;
    }

    public double getCurrentScrollPosition() {
        return currentScrollPosition;
    }

    public void setCurrentScrollPosition(double currentScrollPosition) {
        this.currentScrollPosition = currentScrollPosition;
    }


}
