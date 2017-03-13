package com.bomberman.menu;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenu extends Application {
    private Menu gameMenu;

    @Override
    public void start (Stage stage) throws Exception {
        stage.setTitle("Bomberman");
        Pane rootElem = new Pane();
        rootElem.setPrefSize(800, 600);

        InputStream background = Files.newInputStream(Paths.get("images/logos.png"));
        Image img = new Image(background);
        background.close();

        ImageView view = new ImageView(img);
        //dopasowywanie obrazu do ramki funkcja view.setFitWidth/Height

        gameMenu = new Menu();
        rootElem.getChildren().addAll(view, gameMenu);

        Scene scene = new Scene(rootElem);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}