package com.bomberman.gui.menu;


import com.bomberman.Game;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuNew extends Application {
    private Stage stage;
    private Pane rootElem;
    private Scene mainScene;

    @Override
    public void start (Stage stage) throws Exception {

        this.stage = stage;
        this.stage.setTitle("Bomberman");
        this.rootElem = new Pane();
        rootElem.setPrefSize(1280, 768);
        new MainMenu(this);

        this.mainScene = new Scene(rootElem);
        stage.setScene(mainScene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public Pane getRootElem() {
        return rootElem;
    }

    @FXML
    void startNewGame() {
        try{
            GameMap map = new GameMap(mainStage);
            game = new Game(mainStage, map);
            game.GameLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openLobby() {

    }

    @FXML
    void openHiscores() {

    }

    @FXML
    void exitGame() {

    }

    @FXML
    void mouseEnteredButton() {

    }

    @FXML
    void mouseExitedButton() {

    }

    public static void main(String args[]) {
        launch(args);
    }
}
