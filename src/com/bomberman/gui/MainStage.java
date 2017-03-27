package com.bomberman.gui;

import com.bomberman.Game;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class MainStage extends Application {
    // IPv4 address pattern
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static boolean debug = false;
    private static Logger log = Logger.getLogger(MainStage.class.getCanonicalName());
    private static Stage primaryStage;
    private static Pane root;
    private static Scene scene;
    private final int playersAmount = 3;
    // Lobby elements
    @FXML
    private TextField IPAddressField;
    @FXML
    private Label pingLabel;
    @FXML
    private Rectangle player1Colour;
    @FXML
    private Rectangle player2Colour;
    @FXML
    private Rectangle player3Colour;
    @FXML
    private Label player1NameLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private Label player3NameLabel;

    private PlayerSlot playersSlots[] = new PlayerSlot[playersAmount];
    private String serverAddress = "NULL";
    private boolean isIPAddressValid = false;

    private static boolean validateIPv4(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainStage.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
        primaryStage.setTitle("Bomberman");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


        playersSlots[0] = new PlayerSlot(player1NameLabel, player1Colour);
        playersSlots[1] = new PlayerSlot(player2NameLabel, player2Colour);
        playersSlots[2] = new PlayerSlot(player3NameLabel, player3Colour);
    }

    public Stage getStage() {
        return primaryStage;
    }

    public Pane getRootElem() {
        return root;
    }

    @FXML
    void startNewGame() {
        log.info(format("Starting a new game."));
            try{
                GameMap map = new GameMap(this);
                Game game = new Game(this, map);

                game.GameLoop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        try {
//            root = FXMLLoader.load(getClass().getResource("Game.fxml"));
//            scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e) {
//            log.log(Level.SEVERE, e.getMessage(), e);
//        }

    @FXML
    void openLobby() {
        log.info(format("Opening lobby scene."));
        try {
            root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void openHiscores() {
        log.info(format("Opening hiscores scene."));
        try {
            root = FXMLLoader.load(getClass().getResource("Hiscores.fxml"));
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void exitGame() {
        log.info(format("Closing application."));

        this.primaryStage.close();
    }

    @FXML
    void mouseEnteredButton(Event event) {
        if (debug)
            log.info(format("Mouse enetered a button."));

        Button eventButton = (Button) event.getTarget();
        eventButton.setEffect(new Glow(0.5));
    }

    @FXML
    void mouseExitedButton(Event event) {
        if (debug)
            log.info(format("Mouse exited a button."));

        Button eventButton = (Button) event.getTarget();
        eventButton.setEffect(new Glow(0.0));
    }

    @FXML
    void addressEntered() {
        serverAddress = IPAddressField.getText();
        if (debug)
            log.info(format("User entered " + serverAddress + " server's address."));

        if (validateIPv4(serverAddress)) {
            IPAddressField.setStyle("-fx-text-fill: rgba(0,200,0,0.9);");
            isIPAddressValid = true;
        } else {
            log.info(format("Invalid IPv4 address format!"));
            IPAddressField.setStyle("-fx-text-fill: rgba(200,0,0,0.9);");
            isIPAddressValid = false;

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Invalid IP address!");
            alert.setContentText("Address " + serverAddress + ",that you entered, is invalid! Please enter valid IPv4 address.");
            alert.showAndWait();
        }

    }

    @FXML
    void changeColour(Event event) {

    }

    @FXML
    void selectGameSlot(Event event) {

    }

    @FXML
    void backToMenu() {
        if (debug)
            log.info(format("Going back to main menu."));

        try {
            root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    void connectToServer() {
        if (debug)
            log.info(format("Trying to connect to server: " + serverAddress));

        if (isIPAddressValid) {

        } else {
            log.info(format("Couldn't connect to server: " + serverAddress));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Invalid IP address!");
            alert.setContentText("Address " + serverAddress + ",that you entered, is invalid! Please enter valid IPv4 address.");
            alert.showAndWait();
        }

    }
}
