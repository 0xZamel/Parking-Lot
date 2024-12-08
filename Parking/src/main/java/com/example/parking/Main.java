package com.example.parking;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.Objects;

public class Main extends Application {
    Stage window;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FeedBackFXML.fxml")));
        Scene scene = new Scene(root);

        window.setScene(scene);
        window.show();
    }
}