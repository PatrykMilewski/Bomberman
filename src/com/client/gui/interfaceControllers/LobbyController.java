package com.client.gui.interfaceControllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

public class LobbyController extends MainStageController {
    private static final int playersAmount = 3;

    // IPv4 address pattern
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static boolean isIPAddressValid = false;
    private static String serverAddress;
    private static PlayerSlot playersSlots[] = new PlayerSlot[playersAmount];
    
    private Color playersColors[] = new Color[playersAmount];
    
    @FXML
    private TextField IPAddressField;
    @FXML
    private Label pingLabel;
    @FXML
    private Pane player1Color;
    @FXML
    private Pane player2Color;
    @FXML
    private Pane player3Color;
    @FXML
    private Label player1NameLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private Label player3NameLabel;
    @FXML
    private ColorPicker colorPicker;

    private static boolean validateIPv4(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    @FXML
    public void initialize() {
        playersSlots[0] = new PlayerSlot(player1NameLabel, playersColors[0]);
        playersSlots[1] = new PlayerSlot(player2NameLabel, playersColors[1]);
        playersSlots[2] = new PlayerSlot(player3NameLabel, playersColors[2]);
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
    void changeColor(Event event)  {
        Pane eventPane = (Pane) event.getTarget();

        colorPicker = new ColorPicker();
        colorPicker.relocate(eventPane.getLayoutX(), eventPane.getLayoutY());

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                eventPane.setStyle("-fx-background-color:" + "#" + colorPicker.getValue().toString().substring(2,10));
                eventPane.getChildren().removeAll(colorPicker);
            }
        });

        Pane rootPane = (Pane) eventPane.getParent();
        rootPane.getChildren().add(colorPicker);
    }
    
    private void colorPickHandle() {
    
    }
    

    @FXML
    void selectGameSlot(Event event) {
    
    }
}
