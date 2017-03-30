package com.bomberman.gui.interfaceControllers;


import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class PlayerSlot {
    private Label playerNameLabel;
    private Rectangle playerColourRectangle;
    private String playersName;
    private int playersID;

    PlayerSlot(Label playerNameLabel, Rectangle playerColourRectangle) {
        this.playerNameLabel = playerNameLabel;
        this.playerColourRectangle = playerColourRectangle;
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
}
