package com.client.gui.interfaceControllers;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerSlot {
    private Label playerNameLabel;
    private Color playersColor;
    private String playersName;
    private int playersID;

    PlayerSlot(Label playerNameLabel, Color playersColor) {
        this.playerNameLabel = playerNameLabel;
        this.playersColor = playersColor;
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

    public void setPlayersName(String playersName) {
        this.playersName = playersName;
    }
    
    public Color getColor() { return this.playersColor; }
    
    public void setColor(Color input) { this.playersColor = input; }
}
