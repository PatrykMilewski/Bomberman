package com.bomberman.gui.interfaceControllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.util.regex.Pattern;

public class LobbyController extends MainStageController {
    private static final int playersAmount = 3;

    // IPv4 address pattern
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static boolean isIPAddressValid = false;
    private static String serverAddress;
    private static PlayerSlot playersSlots[] = new PlayerSlot[playersAmount];
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

    private static boolean validateIPv4(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    @FXML
    public void initialize() {
        playersSlots[0] = new PlayerSlot(player1NameLabel, player1Colour);
        playersSlots[1] = new PlayerSlot(player2NameLabel, player2Colour);
        playersSlots[2] = new PlayerSlot(player3NameLabel, player3Colour);
    }

    @FXML
    void addressEntered() {
        serverAddress = IPAddressField.getText();
        if (debug)
            log.info("User entered " + serverAddress + " server's address.");

        if (validateIPv4(serverAddress)) {
            IPAddressField.setStyle("-fx-text-fill: rgba(0,200,0,0.9);");
            isIPAddressValid = true;
        } else {
            log.info("Invalid IPv4 address format!");
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
    void connectToServer() {
        if (debug)
            log.info("Trying to connect to server: " + serverAddress);

        if (isIPAddressValid) {
            log.info("Connecting to server!");
        } else {
            log.info("Couldn't connect to server: " + serverAddress);

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
}
