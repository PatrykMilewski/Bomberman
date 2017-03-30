package com.bomberman.gui.interfaceControllers;

import com.bomberman.Game;
import com.bomberman.gui.GameMap;
import com.bomberman.gui.MainStage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;

import java.io.IOException;
import java.util.logging.Level;

public class MainStageController extends MainStage {
    @FXML
    void startNewGame() {
        log.info("Starting a new game.");
        try {
            loader = new FXMLLoader(getClass().getResource("fxmlFiles/Game.fxml"));
            root = loader.load();
            gameController = loader.getController();
            scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();

            GameMap map = new GameMap(this);
            Game game = new Game(this, map);

            game.GameLoop();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }

        root.getChildren();
    }

    @FXML
    void openLobby() {
        log.info("Opening lobby scene.");
        try {
            loader = new FXMLLoader(getClass().getResource("fxmlFiles/Lobby.fxml"));
            root = loader.load();
            lobbyController = loader.getController();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void openHighscores() {
        log.info("Opening highscores scene.");
        try {
            loader = new FXMLLoader(getClass().getResource("fxmlFiles/Highscores.fxml"));
            root = loader.load();
            highscoresController = loader.getController();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void exitGame() {
        log.info("Closing application.");

        this.primaryStage.close();
    }

    @FXML
    void backToMenu() {
        if (debug)
            log.info("Going back to main menu.");

        try {
            root = FXMLLoader.load(getClass().getResource("fxmlFiles/MainStage.fxml"));
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void mouseEnteredButton(Event event) {
        if (debug)
            log.info("Mouse enetered a button.");

        Button eventButton = (Button) event.getTarget();
        eventButton.setEffect(new Glow(0.5));
    }

    @FXML
    void mouseExitedButton(Event event) {
        if (debug)
            log.info("Mouse exited a button.");

        Button eventButton = (Button) event.getTarget();
        eventButton.setEffect(new Glow(0.0));
    }
}
