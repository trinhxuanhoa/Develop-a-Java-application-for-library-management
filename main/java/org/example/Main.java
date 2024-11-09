package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        Login login = new Login();
        login.login(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}


