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

public class MainStage extends Application {
    private Stage stage;
    private Pane rootElem;

    @Override
    public void start (Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setTitle("Bomberman");
        this.rootElem = new Pane();
        rootElem.setPrefSize(800, 600);
        new MainMenu(this.stage, this.rootElem);

        Scene scene = new Scene(rootElem);
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public Pane getRootElem() {
        return rootElem;
    }

    public static void main(String args[]) {
        launch(args);
    }
}