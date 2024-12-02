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
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // Thêm biến OTP
    private static String otp = "5156565";

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
                    if (UserDAO.getRole(Createinterface.userId()).equals("admin"))
                        inf.interFaceAdmin(this);
                    else
                        inf.interFaceUser(this);
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

            /*Label phoneLabel = new Label("Số điện thoại:");
            phoneLabel.getStyleClass().add("input-label");

            ImageView phoneIcon = new ImageView(phoneIconImage);
            phoneIcon.setFitWidth(32);
            phoneIcon.setFitHeight(32);

            TextField phoneField = new TextField();
            phoneField.setPromptText("Nhập số điện thoại");
            phoneField.getStyleClass().add("text-field");
            phoneField.setPrefWidth(500);

            HBox phoneHBox = new HBox(10, phoneIcon, phoneField);
            phoneHBox.setAlignment(Pos.CENTER_LEFT);*/

            // Nút gửi OTP
            Button sendOTPButton = new Button("Gửi OTP");
            sendOTPButton.getStyleClass().add("primary-button");

            // Ô nhập OTP (ẩn mặc định)
            TextField otpField = new TextField();
            otpField.setPromptText("Nhập mã OTP");
            otpField.getStyleClass().add("text-field");
            otpField.setPrefWidth(500);
            otpField.setVisible(false);

            // Nút xác nhận OTP (ẩn mặc định)
            Button verifyOTPButton = new Button("Xác nhận OTP");
            verifyOTPButton.getStyleClass().add("primary-button");
            verifyOTPButton.setVisible(false);

            // Sự kiện khi bấm nút gửi OTP
            sendOTPButton.setOnAction(e -> {
                // Kiểm tra thông tin người dùng nhập
                String username = usernameField.getText();
                String email = emailField.getText();
                otp = generateRandomNumbersString(6);
                if (username.isEmpty() || email.isEmpty()) {
                    Noti.showFailureMessage("Vui lòng nhập đầy đủ thông tin!");
                } else {

                    if(email.compareTo(getEmail(username))==0) {
                        // Hiển thị ô nhập OTP và nút xác nhận OTP
                        otpField.setVisible(true);
                        verifyOTPButton.setVisible(true);
                        try {
                            LibraryManagement.sendEmail(email, otp);
                            Noti.showSuccessMessage("Mã OTP đã được gửi!");
                            sendOTPButton.setDisable(true);
                            PauseTransition pause = new PauseTransition(Duration.seconds(60));
                            pause.setOnFinished(event -> sendOTPButton.setDisable(false)); // Bật lại nút sau khi hết thời gian chờ
                            pause.play();
                        } catch (Exception ex) {
                            Noti.showFailureMessage("Lỗi khi gửi otp");
                        }
                    } else {
                        Noti.showFailureMessage("Email không trùng với tên đăng nhập");
                    }
                }
            });

            // Sự kiện khi bấm nút xác nhận OTP
            verifyOTPButton.setOnAction(e -> {
                String enteredOTP = otpField.getText();
                if (enteredOTP.equals(otp)) {
                    Noti.showSuccessMessage("Xác thực OTP thành công!");

                    // Chuyển sang phần đặt lại mật khẩu
                    showResetPasswordScene(usernameField.getText());
                } else {
                    Noti.showFailureMessage("Mã OTP không chính xác!");
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
                    /*phoneLabel,
                    phoneHBox,*/
                    sendOTPButton,
                    otpField,
                    verifyOTPButton,
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

    // Phương thức mới để hiển thị giao diện đặt lại mật khẩu
    private void showResetPasswordScene(String username) {
        VBox resetPasswordBox = new VBox(15);
        resetPasswordBox.setAlignment(Pos.CENTER);
        resetPasswordBox.setPadding(new Insets(20));
        resetPasswordBox.setMaxWidth(600);

        Label resetPasswordLabel = new Label("Đặt lại mật khẩu");
        resetPasswordLabel.getStyleClass().add("form-title");

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
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (!newPassword.equals(confirmPassword)) {
                    Noti.showFailureMessage("Mật khẩu mới không khớp!");
                } else if (UserDAO.updatePassword(username, newPassword)) {
                    Noti.showSuccessMessage("Đặt lại mật khẩu thành công!");
                    showLoginScene();
                } else {
                    Noti.showFailureMessage("Đặt lại mật khẩu không thành công, vui lòng thử lại!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Noti.showFailureMessage("Đã xảy ra lỗi trong quá trình đặt lại mật khẩu. Vui lòng thử lại.");
            }
        });

        Button backButton = new Button("Quay lại");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(e -> showLoginScene());

        resetPasswordBox.getChildren().addAll(
                resetPasswordLabel,
                newPasswordLabel,
                newPasswordHBox,
                confirmPasswordLabel,
                confirmPasswordHBox,
                resetPasswordButton,
                backButton
        );

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Scene resetPasswordScene = createSceneWithBackground(resetPasswordBox, visualBounds.getWidth(), visualBounds.getHeight());

        primaryStage.setScene(resetPasswordScene);
    }

    public void showHelpScene() {
        try {
            VBox helpBox = new VBox(15);
            helpBox.setAlignment(Pos.CENTER);
            helpBox.setPadding(new Insets(20));
            helpBox.setMaxWidth(600);

            Label helpLabel = new Label("Yêu cầu trợ giúp");
            helpLabel.getStyleClass().add("form-title");

            Label emailLabel = new Label("Email nhận phản hồi:");
            emailLabel.getStyleClass().add("input-label");
            TextField emailField = new TextField();
            emailField.setPromptText("Nhập email");
            emailField.getStyleClass().add("text-field");
            emailField.setPrefWidth(500);

            Label userIdLabel = new Label("ID người dùng:");
            userIdLabel.getStyleClass().add("input-label");
            TextField userIdField = new TextField();
            userIdField.setPromptText("Nhập ID người dùng");
            userIdField.getStyleClass().add("text-field");
            userIdField.setPrefWidth(500);

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
            sendHelpButton.setPrefWidth(200);
            sendHelpButton.setOnAction(e -> {
                try {
                    String email = emailField.getText();
                    String userId = userIdField.getText();
                    String title = titleField.getText();
                    String content = contentField.getText();

                    if (email.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập email!");
                    } else if (userId.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập ID người dùng!");
                    } else if (title.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập tiêu đề!");
                    } else if (content.isEmpty()) {
                        Noti.showFailureMessage("Bạn chưa nhập nội dung!");
                    } else {
                        // Xử lý gửi yêu cầu trợ giúp tại đây
                        Noti.showSuccessMessage("Yêu cầu trợ giúp đã được gửi thành công!");
                        emailField.clear();
                        userIdField.clear();
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
            backButton.setPrefWidth(200);

            helpBox.getChildren().addAll(
                    helpLabel,
                    emailLabel,
                    emailField,
                    userIdLabel,
                    userIdField,
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

    public static String generateRandomNumbersString(int count) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int number = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 99
            result.append(number);
            if (i < count - 1) {
            }
        }

        return result.toString();
    }

    public static String getEmail(String userName) {
        String sql = "select email from users where username = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()){
                resultSet.next();
                return resultSet.getString("email");
            }
        }catch (SQLException e) {
            System.out.println("Lỗi email: "+e.getMessage());
        }
        return "";
    }
}
