package com.client.gui.interfaceControllers;


import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerSlot {
    private Label playerNameLabel;
    private Pane playersColorPane;
    private String playersName;
    private int playersID;
    private boolean isEmpty;

    PlayerSlot(Label playerNameLabel, Pane playersColorPane, boolean isEmpty) {
        this.playerNameLabel = playerNameLabel;
        this.playersColorPane = playersColorPane;
        this.isEmpty = isEmpty;
    }

    public int getPlayersID() {
        return playersID;
    }

    public void setPlayersID(int playersID) {
        this.playersID = playersID;
    }

    public String getPlayersName() {
        return playersName;
    }
    
    public Pane getColorPane() { return this.playersColorPane; }
    
    public void setColorPane(Pane input) { this.playersColorPane = input; }
    
    boolean isEmpty() { return isEmpty; }
    
    void takeSlot(String playersName) {
        isEmpty = false;
        playerNameLabel.setText(playersName);
    }
    
    void freeSlot() {
        isEmpty = true;
        playerNameLabel.setText("Free slot");
    }
    
    void setPlayersName(String name) {
        playerNameLabel.setText(name);
    }
    
    boolean equalsColorPane(Pane input) {
        return this.playersColorPane == input;
    }
    
    boolean equalsNameLabel(Label input) {
        return this.playerNameLabel == input;
    }
    
}
