package org.example;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;

public class Help {
    private LibraryManagement library;

    // Main container to switch between views
    private VBox mainContainer;

    public Help(LibraryManagement library) {
        this.library = library;
    }

    // Method to initialize and display the Help section
    public Node showHelpOptions() {
        double width = 1150;  
        double height = 600;

        // Initialize the main container
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Help Center Label
        Label helpLabel = new Label("Trung Tâm Trợ Giúp");
        helpLabel.setFont(new Font("Arial", 22));
        helpLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Display help topics by default
        Node helpTopicsNode = displayHelpTopics();

        // "Contact Us" Button
        Button contactAdminButton = createContactButton();

        // Add all components to the main container
        mainContainer.getChildren().addAll(helpLabel, helpTopicsNode, contactAdminButton);

        // Wrap the main container in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefSize(width, height);

        return scrollPane;
    }

    // Method to display help topics fetched from the database, corresponding to the user's role
    private Node displayHelpTopics() {
        String userId = interfaces.userId();
        String role = UserDAO.getRole(userId);
        if ("member".equalsIgnoreCase(role)) {
            role = "user";
        }

        List<HelpTopic> helpTopics = HelpDAO.getHelpTopics(role);

        VBox topicsVBox = new VBox(10);
        topicsVBox.setPadding(new Insets(10));
        topicsVBox.setAlignment(Pos.TOP_LEFT);

        Label topicsLabel = new Label("Các Vấn Đề Thường Gặp");
        topicsLabel.setFont(new Font("Arial", 18));
        topicsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        topicsVBox.getChildren().add(topicsLabel);

        for (HelpTopic topic : helpTopics) {
            addHelpTopic(topicsVBox, topic.getQuestion(), topic.getAnswer());
        }

        // Create ScrollPane for help topics with a maximum height
        ScrollPane topicsScrollPane = new ScrollPane(topicsVBox);
        topicsScrollPane.setFitToWidth(true);
        topicsScrollPane.setPannable(true);
        topicsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        topicsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        topicsScrollPane.setMaxHeight(400); // Adjust as needed

        return topicsScrollPane;
    }

    // Method to add individual help topics to the VBox
    private void addHelpTopic(VBox parentVBox, String question, String answer) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(question);

        Label answerLabel = new Label(answer);
        answerLabel.setWrapText(true);
        answerLabel.setStyle("-fx-font-size: 14px;");
        answerLabel.setMaxWidth(750); // Adjust based on your layout

        // Use a VBox to align the label to the left
        VBox contentVBox = new VBox(answerLabel);
        contentVBox.setAlignment(Pos.TOP_LEFT);
        contentVBox.setPadding(new Insets(10, 10, 10, 10));

        titledPane.setContent(contentVBox);
        titledPane.setExpanded(false);

        // Add animation when the TitledPane is expanded
        titledPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), titledPane.getContent());
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), titledPane.getContent());
                scaleUp.setFromX(0.95);
                scaleUp.setFromY(0.95);
                scaleUp.setToX(1.0);
                scaleUp.setToY(1.0);
                scaleUp.play();
            }
        });

        parentVBox.getChildren().add(titledPane);
    }

    // Method to display the contact form
    private Node showContactForm() {
        double width = 800;  // Desired width

        VBox contactVBox = new VBox(15);
        contactVBox.setPadding(new Insets(20));
        contactVBox.setAlignment(Pos.TOP_CENTER);

        double maxWidth = width - 40;

        // Title Label and TextField
        Label titleLabel = new Label("Tiêu đề:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField titleField = new TextField();
        titleField.setPromptText("Nhập tiêu đề");
        titleField.setMaxWidth(maxWidth);

        // Email Label and TextField (pre-filled and read-only)
        Label emailLabel = new Label("Email nhận phản hồi:");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Nhập email của bạn");
        emailField.setMaxWidth(maxWidth);

        // Fetch email from database
        String userId = interfaces.userId();
        String email = HelpDAO.getEmail(userId);
        emailField.setText(email);
        emailField.setEditable(false);  // Make email field read-only

        // Content Label and TextArea
        Label contentLabel = new Label("Nội dung:");
        contentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Nhập nội dung");
        contentArea.setPrefHeight(200);
        contentArea.setMaxWidth(maxWidth);
        contentArea.setWrapText(true); // Enable text wrapping

        // Send Button
        Button sendButton = new Button("Gửi");
        sendButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;");
        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color: #005BB5; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: bold;"));
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color: #0080FF; -fx-text-fill: white;-fx-font-size: 15px; -fx-font-weight: normal;"));

        // Send Button Action
        sendButton.setOnAction(e -> {
            String title = titleField.getText();
            String content = contentArea.getText();

            if (title.isEmpty() || content.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ tiêu đề và nội dung.");
                alert.showAndWait();
            } else {
                boolean success = HelpDAO.addHelpRequest(userId, title, content);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText(null);
                    alert.setContentText("Yêu cầu trợ giúp đã được gửi thành công. Vui lòng kiểm tra email để nhận phản hồi.");
                    alert.showAndWait();

                    // Reset to help topics view after successful submission
                    mainContainer.getChildren().clear();
                    mainContainer.getChildren().addAll(
                        new Label("Trung Tâm Trợ Giúp") {{
                            setFont(new Font("Arial", 22));
                            setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
                        }},
                        displayHelpTopics(),
                        createContactButton()
                    );
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại.");
                    alert.showAndWait();
                }
            }
        });

        // Arrange all components in the contact VBox
        contactVBox.getChildren().addAll(titleLabel, titleField, emailLabel, emailField, contentLabel, contentArea, sendButton);

        // Wrap the contact form in a ScrollPane
        ScrollPane contactScrollPane = new ScrollPane(contactVBox);
        contactScrollPane.setFitToWidth(true);
        contactScrollPane.setPannable(true);
        contactScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contactScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contactScrollPane.setMaxHeight(500); // Adjust as needed

        return contactScrollPane;
    }

    // Helper method to create the "Contact Us" button (used after sending the form)
    private Button createContactButton() {
        Button contactAdminButton = new Button("Liên hệ với chúng tôi");
        contactAdminButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        contactAdminButton.setMaxWidth(800 / 3);
        contactAdminButton.setMinHeight(40);
        contactAdminButton.setOnMouseEntered(e -> {
            contactAdminButton.setStyle("-fx-background-color: #0D47A1; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        contactAdminButton.setOnMouseExited(e -> {
            contactAdminButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        contactAdminButton.setOnAction(e -> {
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(showContactForm());
        });

        return contactAdminButton;
    }
}
