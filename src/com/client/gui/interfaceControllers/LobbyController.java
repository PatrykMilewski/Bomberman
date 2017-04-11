package com.client.gui.interfaceControllers;

import com.client.Client;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class LobbyController extends MainStageController {
    private static final int playersAmount = 3;

    // IPv4 address pattern
    private static final Pattern PATTERN = Pattern.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})$");

    private static boolean isIPAddressValid = false;
    private static String serverAddress;
    private static String serverPort;
    private static PlayerSlot playersSlots[] = new PlayerSlot[playersAmount];
    
    private boolean colorPickerAdded;
    private PlayerSlot selectedSlot;
    private String playersName, playersColor;
    
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
        playersName = null;
        
        playersSlots[0] = new PlayerSlot(player1NameLabel, player1Color, true);
        playersSlots[1] = new PlayerSlot(player2NameLabel, player2Color, true);
        playersSlots[2] = new PlayerSlot(player3NameLabel, player3Color, true);
    
        colorPickerAdded = false;
        colorPicker = new ColorPicker();
        colorPicker.setStyle("-fx-color-label-visible: false;");
    }

    @FXML
    void addressEntered() {
        String rawAddress = IPAddressField.getText();
        serverAddress = (rawAddress.split(":"))[0];
        serverPort = (rawAddress.split(":"))[1];
        if (debug)
            log.info("User entered " + rawAddress + " server's address.");

        if (validateIPv4(rawAddress)) {
            IPAddressField.setStyle("-fx-text-fill: green;");
            isIPAddressValid = true;
        } else {
            log.warning("Invalid IPv4 address format!");
            IPAddressField.setStyle("-fx-text-fill: red;");
            isIPAddressValid = false;
            String alertMessage = "Address " + rawAddress + ",that you entered, is invalid! Please enter valid IPv4 address.";
            showAlert(Alert.AlertType.ERROR, "", "Invalid IP address!", alertMessage);
        }

    }

    @FXML
    void connectToServer() {
        if (debug)
            log.info("Trying to connect to server: " + serverAddress);

        if (isIPAddressValid) {
            if (debug)
                log.info("Connecting to server!");
            
            try {
                thisPlayer.setServerAddress(serverAddress, serverPort);
                thisPlayer.wannaJoin();
            }
            catch (UnknownHostException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
            //todo
        } else {
            log.warning("Couldn't connect to server: " + serverAddress);
            String alertMessage;
            if (serverAddress != null)
                alertMessage = "Address " + serverAddress + ",that you entered, is invalid! Please enter valid IPv4 address.";
            else
                alertMessage = "Please enter server IP address!";
            
            showAlert(Alert.AlertType.ERROR, "", "Invalid IP address!", alertMessage);
        }

    }

    @FXML
    void changeColor(Event event) {
        Pane eventPane = (Pane) event.getTarget();
        if (selectedSlot == null) {
            showAlert(Alert.AlertType.INFORMATION, "","It's not your slot!", "Please select slot first, then change your color!");
            return;
        }
        if (!selectedSlot.equalsColorPane(eventPane)) {
            showAlert(Alert.AlertType.INFORMATION, "", "It's not your slot!", "Please change your colour, not somebody else's!");
            return;
        }
        
        colorPicker.relocate(eventPane.getLayoutX(), eventPane.getLayoutY());
        colorPicker.setOnAction((ActionEvent newEvent) -> {
            playersColor =  colorPicker.getValue().toString().substring(2,10);
            eventPane.setStyle("-fx-background-color:" + "#" + playersColor);
            root.getChildren().remove(colorPicker);
            colorPickerAdded = false;
        });
        
        if (!colorPickerAdded) {
            ((Pane) eventPane.getParent() ).getChildren().add(colorPicker);
            colorPickerAdded = true;
        }
    }

    @FXML
    void selectGameSlot(Event event) {
        Label eventLabel = (Label) event.getTarget();
        for (int i = 0; i < playersAmount; i++) {
            if (playersSlots[i].equalsNameLabel(eventLabel)) {
                if (playersSlots[i].isEmpty()) {
                    if (selectedSlot != null)
                        selectedSlot.freeSlot();
                    selectedSlot = playersSlots[i];
                    selectedSlot.takeSlot(playersName);
                    log.info("Selected slot number " + i);
                }
                else if (playersSlots[i] == selectedSlot) {
                    if (debug)
                        log.info("Unselecting player's slot.");
                    
                    playersSlots[i].freeSlot();
                    selectedSlot = null;
                }
                else {
                    if (debug)
                        log.info("Selected slot is already taken!");
                    
                    String alertMessage = "Slot, that you selected, is already taken by other player!";
                    showAlert(Alert.AlertType.ERROR, "", "Slot already taken!", alertMessage);
                }
                return;
            }
        }
    }
    
    @FXML
    void testServerConnection() {
        if (debug)
            log.info("Testing connection to server on address: " + serverAddress);
        String alertMessage;
        if (serverAddress != null)
            return; //todo
        else {
            alertMessage = "Address " + serverAddress + ",that you entered, is invalid! Please enter valid IPv4 address.";
            showAlert(Alert.AlertType.ERROR, "", "Invalid IP address!", alertMessage);
        }
    }
    
    @FXML
    void setPlayersName(Event event) {
        TextField textField = (TextField) event.getTarget();
        playersName = textField.getText();
        
        if (debug)
            log.info("Player's name set to: " + playersName);
    }
}
