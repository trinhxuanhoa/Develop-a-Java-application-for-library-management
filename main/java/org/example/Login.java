/*package org.example;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login {

    Stage primaryStage;
    Scene loginScene;

    public void login(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        double screenHeight = visualBounds.getHeight();

        Createinterface inf = new Createinterface(primaryStage);
        Image backgroundImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/kho-tang-tri-thuc-vu-tru.jpg"); // Đường dẫn ảnh
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(screenWidth); // Đặt kích thước nền
        backgroundImageView.setFitHeight(screenHeight);
        //backgroundImageView.setPreserveRatio(true); // Giữ tỷ lệ ảnh

        // Tạo form đăng nhập
        VBox loginBox = new VBox(15); // Khoảng cách giữa các phần tử là 20
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                "-fx-padding: 600px; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Tạo hình chữ nhật màu trắng với viền và bo tròn
        Rectangle whiteRectangle = new Rectangle(400, 200);
        whiteRectangle.setFill(Color.WHITE); // Màu nền
        whiteRectangle.setStroke(Color.BLACK); // Màu viền
        whiteRectangle.setStrokeWidth(1); // Độ dày viền
        whiteRectangle.setArcWidth(30); // Bán kính bo tròn theo chiều ngang
        whiteRectangle.setArcHeight(30); // Bán kính bo tròn theo chiều dọc
        whiteRectangle.setOpacity(0.8); // Độ trong suốt (tùy chọn)

        // Các thành phần trong form đăng nhập
        Label sign = new Label("Đăng nhập để bắt đầu");
        //sign.setFont(new Font("Arial", 20));
        sign.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;"); // Màu chữ đỏ

        TextField usernameField = new TextField();
        usernameField.setPromptText("Tên đăng nhập ");
        usernameField.setMinHeight(30);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mật khẩu");
        passwordField.setMinHeight(30);

        Button loginButton = new Button("Đăng nhập");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Đặt màu cho nút

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (UserDAO.checkAccount(username, password)) {
                if (UserDAO.checkStatusAccount(Createinterface.userId())) {
                        System.out.println("Đăng nhập thành công!");
                        Noti.showFormattedMessage("Đăng nhập thành công!",
                                " nhấn OK để tiếp tục");
                        usernameField.clear();
                        passwordField.clear();
                        if (UserDAO.getRole(Createinterface.userId()).compareTo("admin") == 0)
                            inf.interFaceAdmin(this);
                        else
                            inf.interFaceUser(this);

                } else {
                    Noti.showFailureMessage("Tài khoản của bạn đã bị khóa");
                }
            } else {
                System.out.println("Sai tài khoản hoặc mật khẩu!");
                Noti.showFailureMessage("Sai tài khoản hoặc mật khẩu!");
            }
        });

        // Thêm các thành phần vào VBox
        loginBox.getChildren().addAll(sign, usernameField, passwordField, loginButton);

        // Dùng StackPane để xếp hình nền, hình chữ nhật và form
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, whiteRectangle, loginBox);

        // Đặt vị trí của hình chữ nhật và form đăng nhập
        StackPane.setAlignment(whiteRectangle, Pos.CENTER);
        StackPane.setAlignment(loginBox, Pos.CENTER);

        // Tạo Scene và hiển thị Stage
        loginScene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Library Manager");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
    }
    public void showLoginScene() {
        primaryStage.setScene(loginScene); // Quay lại scene đăng nhập
    }
}*/
package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Random;

public class Login {

    private Stage primaryStage;
    private Scene loginScene, registerScene, forgotPasswordScene, helpScene;
    private String captchaText;
    private Image backgroundImage;

    private Image usernameIconImage;
    private Image passwordIconImage;
    private Image emailIconImage;
    private Image phoneIconImage;

    public void login(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        double screenHeight = visualBounds.getHeight();

        Createinterface inf = new Createinterface(primaryStage);

        loadBackgroundImage(screenWidth, screenHeight);
        loadIcons();

        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setMaxWidth(600); // Tăng độ rộng của VBox để chứa các ô nhập dài hơn

        Label sign = new Label("Đăng nhập để bắt đầu");
        sign.getStyleClass().add("form-title");

        // Trường nhập tên đăng nhập với biểu tượng bên trái
        Label usernameLabel = new Label("Tên đăng nhập:");
        usernameLabel.getStyleClass().add("input-label");

        ImageView usernameIcon = new ImageView(usernameIconImage);
        usernameIcon.setFitWidth(32);
        usernameIcon.setFitHeight(32);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nhập tên đăng nhập");
        usernameField.getStyleClass().add("text-field");
        usernameField.setPrefWidth(500); // Kéo dài ô nhập

        HBox usernameHBox = new HBox(10, usernameIcon, usernameField);
        usernameHBox.setAlignment(Pos.CENTER_LEFT);

        // Trường nhập mật khẩu với biểu tượng bên trái
        Label passwordLabel = new Label("Mật khẩu:");
        passwordLabel.getStyleClass().add("input-label");

        ImageView passwordIcon = new ImageView(passwordIconImage);
        passwordIcon.setFitWidth(32);
        passwordIcon.setFitHeight(32);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nhập mật khẩu");
        passwordField.getStyleClass().add("text-field");
        passwordField.setPrefWidth(500); // Kéo dài ô nhập

        HBox passwordHBox = new HBox(10, passwordIcon, passwordField);
        passwordHBox.setAlignment(Pos.CENTER_LEFT);

        Button loginButton = new Button("Đăng nhập");
        loginButton.getStyleClass().add("primary-button");

        Button registerButton = new Button("Đăng kí tài khoản");
        registerButton.getStyleClass().add("secondary-button");
        registerButton.setOnAction(e -> showRegisterScene());

        Button forgotPasswordButton = new Button("Quên mật khẩu?");
        forgotPasswordButton.getStyleClass().add("link-button");
        forgotPasswordButton.setOnAction(e -> showForgotPasswordScene());

        Button helpButton = new Button("Trợ giúp");
        helpButton.getStyleClass().add("link-button");
        helpButton.setOnAction(e -> showHelpScene());

        loginButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (UserDAO.checkAccount(username, password)) {
                    usernameField.clear();
                    passwordField.clear();
                    if (UserDAO.checkStatusAccount(Createinterface.userId())) {
                        if (UserDAO.getRole(Createinterface.userId()).equals("admin"))
                            inf.interFaceAdmin(this);
                        else
                            inf.interFaceUser(this);
                    } else {
                        Noti.showWarningMessage("Tài khoản của bạn đã bị khóa!");
                    }
                } else {
                    Noti.showFailureMessage("Sai tài khoản hoặc mật khẩu!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Noti.showFailureMessage("Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại.");
            }
        });

        loginBox.getChildren().addAll(
                sign,
                usernameLabel,
                usernameHBox,
                passwordLabel,
                passwordHBox,
                loginButton,
                registerButton,
                forgotPasswordButton,
                helpButton
        );

        loginScene = createSceneWithBackground(loginBox, screenWidth, screenHeight);

        primaryStage.setTitle("Library Manager");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Load background
    private void loadBackgroundImage(double screenWidth, double screenHeight) {
        try {
            String backgroundImagePath = "C:/Users/Dell/IdeaProjects/library/src/main/image/bg.jpg";
            File imageFile = new File(backgroundImagePath);
            if (!imageFile.exists()) {
                throw new Exception("Ảnh nền không tồn tại tại đường dẫn: " + backgroundImagePath);
            }
            backgroundImage = new Image(imageFile.toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Không thể tải ảnh nền. Vui lòng kiểm tra đường dẫn.");
            backgroundImage = null;
        }
    }

    private void loadIcons() {
        try {
            usernameIconImage = new Image(getClass().getResourceAsStream("/icons/username.png"));
            passwordIconImage = new Image(getClass().getResourceAsStream("/icons/password.png"));
            emailIconImage = new Image(getClass().getResourceAsStream("/icons/email.png"));
            phoneIconImage = new Image(getClass().getResourceAsStream("/icons/phone.png"));
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Không thể tải biểu tượng. Vui lòng kiểm tra đường dẫn.");
        }
    }

    private Scene createSceneWithBackground(Pane contentPane, double width, double height) {
        StackPane root = new StackPane();

        ImageView sceneBackgroundImageView;
        if (backgroundImage != null) {
            sceneBackgroundImageView = new ImageView(backgroundImage);
            sceneBackgroundImageView.setPreserveRatio(false);
            sceneBackgroundImageView.setFitWidth(width);
            sceneBackgroundImageView.setFitHeight(height);
        } else {
            sceneBackgroundImageView = new ImageView();
            sceneBackgroundImageView.setFitWidth(width);
            sceneBackgroundImageView.setFitHeight(height);
            sceneBackgroundImageView.setStyle("-fx-background-color: #f0f0f0;");
        }

        Rectangle sceneOverlay = new Rectangle();
        sceneOverlay.setFill(new Color(0, 0, 0, 0.3));
        sceneOverlay.widthProperty().bind(root.widthProperty());
        sceneOverlay.heightProperty().bind(root.heightProperty());

        root.getChildren().addAll(sceneBackgroundImageView, sceneOverlay, contentPane);
        StackPane.setAlignment(contentPane, Pos.CENTER);

        Scene scene;
        try {
            scene = new Scene(root, width, height);
            URL cssResource = getClass().getResource("/styles/styles.css");
            if (cssResource == null) {
                throw new Exception("Không tìm thấy tệp styles.css");
            }
            scene.getStylesheets().add(cssResource.toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Không thể tải tệp CSS. Giao diện có thể hiển thị không đúng.");
            scene = new Scene(root, width, height);
        }
        return scene;
    }

    // Đăng kí tài khoản
    public void showRegisterScene() {
        try {
            VBox registerBox = new VBox(15);
            registerBox.setAlignment(Pos.CENTER);
            registerBox.setPadding(new Insets(20));
            registerBox.setMaxWidth(600);

            Label registerLabel = new Label("Đăng kí tài khoản");
            registerLabel.getStyleClass().add("form-title");

            Label usernameLabel = new Label("Tên đăng nhập:");
            usernameLabel.getStyleClass().add("input-label");

            ImageView usernameIcon = new ImageView(usernameIconImage);
            usernameIcon.setFitWidth(32);
            usernameIcon.setFitHeight(32);

            TextField newUsernameField = new TextField();
            newUsernameField.setPromptText("Nhập tên đăng nhập");
            newUsernameField.getStyleClass().add("text-field");
            newUsernameField.setPrefWidth(500);

            HBox usernameHBox = new HBox(10, usernameIcon, newUsernameField);
            usernameHBox.setAlignment(Pos.CENTER_LEFT);

            Label passwordLabel = new Label("Mật khẩu:");
            passwordLabel.getStyleClass().add("input-label");

            ImageView passwordIcon = new ImageView(passwordIconImage);
            passwordIcon.setFitWidth(32);
            passwordIcon.setFitHeight(32);

            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("Nhập mật khẩu");
            newPasswordField.getStyleClass().add("text-field");
            newPasswordField.setPrefWidth(500);

            HBox passwordHBox = new HBox(10, passwordIcon, newPasswordField);
            passwordHBox.setAlignment(Pos.CENTER_LEFT);

            Label confirmPasswordLabel = new Label("Xác nhận mật khẩu:");
            confirmPasswordLabel.getStyleClass().add("input-label");

            ImageView confirmPasswordIcon = new ImageView(passwordIconImage);
            confirmPasswordIcon.setFitWidth(32);
            confirmPasswordIcon.setFitHeight(32);

            PasswordField confirmPasswordField = new PasswordField();
            confirmPasswordField.setPromptText("Nhập lại mật khẩu");
            confirmPasswordField.getStyleClass().add("text-field");
            confirmPasswordField.setPrefWidth(500);

            HBox confirmPasswordHBox = new HBox(10, confirmPasswordIcon, confirmPasswordField);
            confirmPasswordHBox.setAlignment(Pos.CENTER_LEFT);

            Label emailLabel = new Label("Email:");
            emailLabel.getStyleClass().add("input-label");

            ImageView emailIcon = new ImageView(emailIconImage);
            emailIcon.setFitWidth(32);
            emailIcon.setFitHeight(32);

            TextField emailField = new TextField();
            emailField.setPromptText("Nhập email");
            emailField.getStyleClass().add("text-field");
            emailField.setPrefWidth(500);

            HBox emailHBox = new HBox(10, emailIcon, emailField);
            emailHBox.setAlignment(Pos.CENTER_LEFT);

            Label phoneLabel = new Label("Số điện thoại:");
            phoneLabel.getStyleClass().add("input-label");

            ImageView phoneIcon = new ImageView(phoneIconImage);
            phoneIcon.setFitWidth(32);
            phoneIcon.setFitHeight(32);

            TextField phoneField = new TextField();
            phoneField.setPromptText("Nhập số điện thoại");
            phoneField.getStyleClass().add("text-field");
            phoneField.setPrefWidth(500);

            HBox phoneHBox = new HBox(10, phoneIcon, phoneField);
            phoneHBox.setAlignment(Pos.CENTER_LEFT);

            Canvas captchaCanvas = new Canvas(200, 50);
            GraphicsContext gc = captchaCanvas.getGraphicsContext2D();
            generateCaptcha(gc);

            TextField captchaField = new TextField();
            captchaField.setPromptText("Nhập mã CAPTCHA");
            captchaField.getStyleClass().add("text-field");
            captchaField.setMaxWidth(200);

            Button refreshCaptchaButton = new Button("Đổi mã");
            refreshCaptchaButton.getStyleClass().add("link-button");
            refreshCaptchaButton.setOnAction(e -> {
                generateCaptcha(gc);
                captchaField.clear();
            });

            HBox captchaBox = new HBox(10);
            captchaBox.setAlignment(Pos.CENTER);
            captchaBox.getChildren().addAll(captchaCanvas, refreshCaptchaButton);

            Button registerAccountButton = new Button("Đăng kí");
            registerAccountButton.getStyleClass().add("primary-button");

            Button backButton = new Button("Quay lại");
            backButton.getStyleClass().add("secondary-button");
            backButton.setOnAction(e -> showLoginScene());

            registerAccountButton.setOnAction(e -> {
                try {
                    String username = newUsernameField.getText();
                    String password = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    String captchaInput = captchaField.getText();

                    if (!captchaInput.equals(captchaText)) {
                        Noti.showFailureMessage("Mã CAPTCHA không chính xác!");
                    } else if (!password.equals(confirmPassword)) {
                        Noti.showFailureMessage("Mật khẩu không khớp!");
                    } else if (UserDAO.registerAccount(username, password, email, phone)) {
                        Noti.showSuccessMessage("Đăng kí thành công!");
                        showLoginScene();
                    } else {
                        Noti.showFailureMessage("Đăng kí không thành công, vui lòng thử lại!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Noti.showFailureMessage("Đã xảy ra lỗi trong quá trình đăng kí. Vui lòng thử lại.");
                }
            });

            registerBox.getChildren().addAll(
                    registerLabel,
                    usernameLabel,
                    usernameHBox,
                    passwordLabel,
                    passwordHBox,
                    confirmPasswordLabel,
                    confirmPasswordHBox,
                    emailLabel,
                    emailHBox,
                    phoneLabel,
                    phoneHBox,
                    captchaBox,
                    captchaField,
                    registerAccountButton,
                    backButton
            );

            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            registerScene = createSceneWithBackground(registerBox, visualBounds.getWidth(), visualBounds.getHeight());

            primaryStage.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Đã xảy ra lỗi khi chuyển đến giao diện đăng kí.");
        }
    }

    // Tạo capcha từ ramdom kí tự có sẵn
    private void generateCaptcha(GraphicsContext gc) {
        try {
            StringBuilder captchaBuilder = new StringBuilder();
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                captchaBuilder.append(chars.charAt(random.nextInt(chars.length())));
            }
            captchaText = captchaBuilder.toString();

            gc.clearRect(0, 0, 200, 50);
            gc.setFill(Color.BLACK);
            gc.setFont(javafx.scene.text.Font.font("Verdana", 28));
            gc.fillText(captchaText, 10, 35);
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Không thể tạo mã CAPTCHA.");
        }
    }

    public void showLoginScene() {
        try {
            primaryStage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Đã xảy ra lỗi khi quay lại giao diện đăng nhập.");
        }
    }

    // Quên mật khẩu
    public void showForgotPasswordScene() {
        try {
            VBox forgotPasswordBox = new VBox(15);
            forgotPasswordBox.setAlignment(Pos.CENTER);
            forgotPasswordBox.setPadding(new Insets(20));
            forgotPasswordBox.setMaxWidth(600);

            Label forgotPasswordLabel = new Label("Quên mật khẩu");
            forgotPasswordLabel.getStyleClass().add("form-title");

            Label usernameLabel = new Label("Tên đăng nhập:");
            usernameLabel.getStyleClass().add("input-label");

            ImageView usernameIcon = new ImageView(usernameIconImage);
            usernameIcon.setFitWidth(32);
            usernameIcon.setFitHeight(32);

            TextField usernameField = new TextField();
            usernameField.setPromptText("Nhập tên đăng nhập");
            usernameField.getStyleClass().add("text-field");
            usernameField.setPrefWidth(500);

            HBox usernameHBox = new HBox(10, usernameIcon, usernameField);
            usernameHBox.setAlignment(Pos.CENTER_LEFT);

            Label emailLabel = new Label("Email:");
            emailLabel.getStyleClass().add("input-label");

            ImageView emailIcon = new ImageView(emailIconImage);
            emailIcon.setFitWidth(32);
            emailIcon.setFitHeight(32);

            TextField emailField = new TextField();
            emailField.setPromptText("Nhập email");
            emailField.getStyleClass().add("text-field");
            emailField.setPrefWidth(500);

            HBox emailHBox = new HBox(10, emailIcon, emailField);
            emailHBox.setAlignment(Pos.CENTER_LEFT);

            Label phoneLabel = new Label("Số điện thoại:");
            phoneLabel.getStyleClass().add("input-label");

            ImageView phoneIcon = new ImageView(phoneIconImage);
            phoneIcon.setFitWidth(32);
            phoneIcon.setFitHeight(32);

            TextField phoneField = new TextField();
            phoneField.setPromptText("Nhập số điện thoại");
            phoneField.getStyleClass().add("text-field");
            phoneField.setPrefWidth(500);

            HBox phoneHBox = new HBox(10, phoneIcon, phoneField);
            phoneHBox.setAlignment(Pos.CENTER_LEFT);

            Label newPasswordLabel = new Label("Mật khẩu mới:");
            newPasswordLabel.getStyleClass().add("input-label");

            ImageView newPasswordIcon = new ImageView(passwordIconImage);
            newPasswordIcon.setFitWidth(32);
            newPasswordIcon.setFitHeight(32);

            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("Nhập mật khẩu mới");
            newPasswordField.getStyleClass().add("text-field");
            newPasswordField.setPrefWidth(500);

            HBox newPasswordHBox = new HBox(10, newPasswordIcon, newPasswordField);
            newPasswordHBox.setAlignment(Pos.CENTER_LEFT);

            Label confirmPasswordLabel = new Label("Xác nhận mật khẩu mới:");
            confirmPasswordLabel.getStyleClass().add("input-label");

            ImageView confirmPasswordIcon = new ImageView(passwordIconImage);
            confirmPasswordIcon.setFitWidth(32);
            confirmPasswordIcon.setFitHeight(32);

            PasswordField confirmPasswordField = new PasswordField();
            confirmPasswordField.setPromptText("Nhập lại mật khẩu mới");
            confirmPasswordField.getStyleClass().add("text-field");
            confirmPasswordField.setPrefWidth(500);

            HBox confirmPasswordHBox = new HBox(10, confirmPasswordIcon, confirmPasswordField);
            confirmPasswordHBox.setAlignment(Pos.CENTER_LEFT);

            Button resetPasswordButton = new Button("Đặt lại mật khẩu");
            resetPasswordButton.getStyleClass().add("primary-button");
            resetPasswordButton.setOnAction(e -> {
                try {
                    String username = usernameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    String newPassword = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();

                    if (!newPassword.equals(confirmPassword)) {
                        Noti.showFailureMessage("Mật khẩu mới không khớp!");
                    } else if (UserDAO.resetPassword(username, email, phone, newPassword)) {
                        Noti.showSuccessMessage("Đặt lại mật khẩu thành công!");
                        showLoginScene();
                    } else {
                        Noti.showFailureMessage("Thông tin không hợp lệ, vui lòng thử lại!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Noti.showFailureMessage("Đã xảy ra lỗi trong quá trình đặt lại mật khẩu. Vui lòng thử lại.");
                }
            });

            Button backButton = new Button("Quay lại");
            backButton.getStyleClass().add("secondary-button");
            backButton.setOnAction(e -> showLoginScene());

            forgotPasswordBox.getChildren().addAll(
                    forgotPasswordLabel,
                    usernameLabel,
                    usernameHBox,
                    emailLabel,
                    emailHBox,
                    phoneLabel,
                    phoneHBox,
                    newPasswordLabel,
                    newPasswordHBox,
                    confirmPasswordLabel,
                    confirmPasswordHBox,
                    resetPasswordButton,
                    backButton
            );

            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            forgotPasswordScene = createSceneWithBackground(forgotPasswordBox, visualBounds.getWidth(), visualBounds.getHeight());

            primaryStage.setScene(forgotPasswordScene);
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Đã xảy ra lỗi khi chuyển đến giao diện quên mật khẩu.");
        }
    }

    public void showHelpScene() {
        try {
            VBox helpBox = new VBox(15);
            helpBox.setAlignment(Pos.CENTER);
            helpBox.setPadding(new Insets(20));
            helpBox.setMaxWidth(600);

            Label helpLabel = new Label("Yêu cầu trợ giúp");
            helpLabel.getStyleClass().add("form-title");

            Label titleLabel = new Label("Tiêu đề:");
            titleLabel.getStyleClass().add("input-label");
            TextField titleField = new TextField();
            titleField.setPromptText("Nhập tiêu đề");
            titleField.getStyleClass().add("text-field");
            titleField.setPrefWidth(500);

            Label contentLabel = new Label("Nội dung:");
            contentLabel.getStyleClass().add("input-label");
            TextArea contentField = new TextArea();
            contentField.setPromptText("Nhập nội dung");
            contentField.setPrefRowCount(5);
            contentField.getStyleClass().add("text-area");
            contentField.setPrefWidth(500);

            Button sendHelpButton = new Button("Gửi");
            sendHelpButton.getStyleClass().add("primary-button");
            sendHelpButton.setOnAction(e -> {
                try {
                    String title = titleField.getText();
                    String content = contentField.getText();

                    if (title.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập tiêu đề!");
                    } else if (content.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập nội dung!");
                    } else {
                        Noti.showSuccessMessage("Yêu cầu trợ giúp đã được gửi thành công!");
                        titleField.clear();
                        contentField.clear();
                        showLoginScene();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Noti.showFailureMessage("Đã xảy ra lỗi khi gửi yêu cầu trợ giúp. Vui lòng thử lại.");
                }
            });

            Button backButton = new Button("Quay lại");
            backButton.getStyleClass().add("secondary-button");
            backButton.setOnAction(e -> showLoginScene());

            helpBox.getChildren().addAll(
                    helpLabel,
                    titleLabel,
                    titleField,
                    contentLabel,
                    contentField,
                    sendHelpButton,
                    backButton
            );

            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            helpScene = createSceneWithBackground(helpBox, visualBounds.getWidth(), visualBounds.getHeight());

            primaryStage.setScene(helpScene);
        } catch (Exception e) {
            e.printStackTrace();
            Noti.showFailureMessage("Đã xảy ra lỗi khi chuyển đến giao diện trợ giúp.");
        }
    }
}
