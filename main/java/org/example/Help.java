package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Help {
    private LibraryManagement library;

    public Help(LibraryManagement library) {
        this.library = library;
    }

    // Method to show help options (help topics and contact form)
    public Node showHelpOptions(double width, double height) {
        VBox helpOptionsVBox = new VBox(20);
        helpOptionsVBox.setPadding(new Insets(20));
        helpOptionsVBox.setAlignment(Pos.CENTER);

        Label helpLabel = new Label("Trung Tâm Trợ Giúp");
        helpLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button helpTopicsButton = new Button("Các Vấn Đề Thường Gặp");
        helpTopicsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        helpTopicsButton.setMaxWidth(width / 3);
        helpTopicsButton.setMinHeight(40);
        helpTopicsButton.setOnAction(e -> {
            helpOptionsVBox.getChildren().clear();
            helpOptionsVBox.getChildren().add(displayHelpTopics(width, height));
        });

        Button contactAdminButton = new Button("Liên hệ Admin");
        contactAdminButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        contactAdminButton.setMaxWidth(width / 3);
        contactAdminButton.setMinHeight(40);
        contactAdminButton.setOnAction(e -> {
            helpOptionsVBox.getChildren().clear();
            helpOptionsVBox.getChildren().add(showHelpForm(width, height));
        });

        helpOptionsVBox.getChildren().addAll(helpLabel, helpTopicsButton, contactAdminButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(helpOptionsVBox);
        borderPane.setPadding(new Insets(20));

        return borderPane;
    }

    // Method to display predefined help topics with expandable content categorized for admin and user
    private Node displayHelpTopics(double width, double height) {
        VBox topicsVBox = new VBox(20);
        topicsVBox.setPadding(new Insets(20));

        Label adminLabel = new Label("Vấn Đề Quản Lý Cho Admin");
        adminLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        topicsVBox.getChildren().add(adminLabel);

        // Admin help topics
        addHelpTopic(topicsVBox, "Làm thế nào để thêm sách mới vào danh mục thư viện?", 
                "Để thêm sách mới vào thư viện, hãy vào mục 'Quản Lý Tài Liệu', sau đó chọn 'Thêm Sách'. Điền đầy đủ thông tin về sách như tiêu đề, tác giả, năm xuất bản, và mã ISBN.", width);
        
        addHelpTopic(topicsVBox, "Làm thế nào để quản lý tài khoản thành viên?", 
                "Để quản lý tài khoản thành viên, vào mục 'Quản Lý Người Dùng'. Bạn có thể tạo tài khoản mới, cập nhật thông tin thành viên hoặc xóa tài khoản không còn sử dụng.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để xử lý sách quá hạn và phí phạt?", 
                "Để xử lý sách quá hạn, vào mục 'Thống Kê', chọn sách quá hạn và nhấn 'Xử Lý'. Bạn có thể áp dụng phí phạt hoặc gửi thông báo nhắc nhở cho thành viên.", width);

        addHelpTopic(topicsVBox, "Cách thêm và xóa nhân viên?", 
                "Vào mục 'Quản Lý Người Dùng', chọn 'Thêm Nhân Viên' hoặc 'Xóa Nhân Viên'. Điền đầy đủ thông tin yêu cầu, sau đó nhấn 'Lưu' để hoàn tất thao tác.", width);

        addHelpTopic(topicsVBox, "Cách thiết lập và quản lý phí phạt quá hạn?", 
                "Vào mục 'Cài Đặt', chọn 'Thiết Lập Phí Phạt'. Bạn có thể đặt mức phí phạt cho từng loại tài liệu và quy định thời hạn tối đa để mượn sách.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để xuất báo cáo thống kê?", 
                "Vào mục 'Thống Kê' và chọn loại báo cáo muốn xuất như 'Sách Đã Mượn', 'Sách Quá Hạn', 'Người Dùng Hoạt Động'. Sau đó nhấn 'Xuất Báo Cáo' để tải về.", width);

        Label userLabel = new Label("Vấn Đề Cho Người Dùng");
        userLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        userLabel.setPadding(new Insets(20, 0, 0, 0));
        topicsVBox.getChildren().add(userLabel);

        // User help topics
        addHelpTopic(topicsVBox, "Làm thế nào để cấp sách cho thành viên?", 
                "Để cấp sách cho một thành viên, hãy vào mục 'Mượn Tài Liệu', tìm tên thành viên và mã sách. Xác nhận thông tin và nhấn 'Cấp Sách'.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để tìm sách trong danh mục?", 
                "Sử dụng chức năng 'Tìm Kiếm' trong mục 'Quản Lý Tài Liệu' để tìm sách. Bạn có thể tìm theo tiêu đề, tác giả, hoặc mã ISBN.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để đăng ký và gia hạn thẻ thư viện?", 
                "Để đăng ký hoặc gia hạn thẻ thư viện, hãy đến mục 'Đăng Ký Thẻ' hoặc 'Gia Hạn Thẻ' và làm theo hướng dẫn. Điền thông tin yêu cầu và gửi yêu cầu.", width);

        addHelpTopic(topicsVBox, "Phí phạt khi trả sách quá hạn là bao nhiêu?", 
                "Phí phạt sẽ phụ thuộc vào số ngày trả muộn và loại sách mượn. Bạn có thể kiểm tra chi tiết phí phạt trong phần 'Quy Định Phí Phạt' hoặc liên hệ thủ thư để biết thêm chi tiết.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để kiểm tra lịch sử mượn sách?", 
                "Vào mục 'Tài Khoản Của Tôi' và chọn 'Lịch Sử Mượn Sách'. Bạn sẽ thấy danh sách tất cả các sách đã mượn, cùng với ngày mượn và ngày phải trả.", width);

        addHelpTopic(topicsVBox, "Làm thế nào để yêu cầu gia hạn thời gian mượn sách?", 
                "Để gia hạn thời gian mượn sách, vào mục 'Mượn Tài Liệu' và chọn sách bạn muốn gia hạn. Sau đó nhấn 'Yêu Cầu Gia Hạn'. Lưu ý rằng không phải tất cả các sách đều có thể gia hạn.", width);

        topicsVBox.setAlignment(Pos.TOP_LEFT);

        // Wrap the VBox in a ScrollPane to allow scrolling
        ScrollPane scrollPane = new ScrollPane(topicsVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(height);
        scrollPane.setPrefViewportWidth(width);

        return scrollPane;
    }
    
    private void addHelpTopic(VBox parentVBox, String question, String solution, double width) {
        Button topicButton = new Button(question);
        topicButton.setMaxWidth(width - 40);

        Pane solutionPane = new Pane();
        solutionPane.setMinHeight(0);
        solutionPane.setPrefHeight(0);
        solutionPane.setVisible(false);

        Label solutionLabel = new Label(solution);
        solutionLabel.setWrapText(true);
        solutionLabel.setStyle("-fx-font-size: 14px;");
        solutionLabel.setMaxWidth(width - 40);
        solutionLabel.setPadding(new Insets(10));
        solutionPane.getChildren().add(solutionLabel);

        topicButton.setOnAction(e -> {
            if (solutionPane.isVisible()) {
                solutionPane.setVisible(false);
                solutionPane.setPrefHeight(0);
            } else {
                solutionPane.setVisible(true);
                solutionPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            }
        });

        parentVBox.getChildren().addAll(topicButton, solutionPane);
    }

    public Node showHelpForm(double width, double height) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        double maxWidth = width - 40;

        Label titleLabel = new Label("Tiêu đề:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField titleField = new TextField();
        titleField.setPromptText("Nhập tiêu đề");
        titleField.setMaxWidth(maxWidth);

        Label emailLabel = new Label("Email nhận phản hồi:");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Nhập email của bạn");
        emailField.setMaxWidth(maxWidth);

        Label contentLabel = new Label("Nội dung:");
        contentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Nhập nội dung");
        contentArea.setPrefHeight(200);
        contentArea.setMaxWidth(maxWidth);

        Button sendButton = new Button("Gửi");
        sendButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        sendButton.setOnAction(e -> {
            String title = titleField.getText();
            String email = emailField.getText();
            String content = contentArea.getText();

            if (title.isEmpty() || email.isEmpty() || content.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ tiêu đề, email và nội dung.");
                alert.showAndWait();
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập một địa chỉ email hợp lệ.");
                alert.showAndWait();
            } else {
                boolean success = library.sendHelpRequest(interfaces.userId(), title, content);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText(null);
                    alert.setContentText("Yêu cầu trợ giúp đã được gửi thành công. Vui lòng kiểm tra email để nhận phản hồi.");
                    alert.showAndWait();

                    titleField.clear();
                    emailField.clear();
                    contentArea.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại.");
                    alert.showAndWait();
                }
            }
        });

        vbox.getChildren().addAll(titleLabel, titleField, emailLabel, emailField, contentLabel, contentArea, sendButton);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportWidth(width);
        scrollPane.setPrefViewportHeight(height);

        return scrollPane;
    }
}
