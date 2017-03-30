package com.bomberman.gui.interfaceControllers;

import com.bomberman.gui.Consts;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.awt.*;

public class GameController extends MainStageController {
    private final static Dimension mapSize = new Dimension(Consts.DIMENSION*Consts.PIXEL_SIZE, Consts.DIMENSION*Consts.PIXEL_SIZE);
    private final static Dimension scoresSize = new Dimension(502, Consts.DIMENSION*Consts.PIXEL_SIZE);

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
