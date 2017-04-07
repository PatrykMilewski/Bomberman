package com.client.gui.interfaceControllers;

import com.client.gui.ClientConsts;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.awt.*;

public class GameController extends MainStageController {
    private final static Dimension mapSize = new Dimension(ClientConsts.DIMENSION*ClientConsts.PIXEL_SIZE, ClientConsts.DIMENSION*ClientConsts.PIXEL_SIZE);
    private final static Dimension scoresSize = new Dimension(502, ClientConsts.DIMENSION*ClientConsts.PIXEL_SIZE);

    @FXML
    private Pane gameMapPane;
    @FXML
    private Pane gameScoresPane;

    @FXML
    public void initialize() {
        gameMapPane.setStyle("-fx-pref-width: " + mapSize.width + "px;");
        gameMapPane.setStyle("-fx-pref-height: " + mapSize.height + "px;");
        gameScoresPane.setStyle("-fx-pref-width: " + scoresSize.width + "px;");
        gameScoresPane.setStyle("-fx-pref-height: " + scoresSize.height + "px;");
    }
    
    public Pane getGameMapPane() {
        return gameMapPane;
    }

    public Pane getGameScoresPane() {
        return gameScoresPane;
    }
}
