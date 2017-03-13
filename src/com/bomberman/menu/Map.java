package com.bomberman.menu;

import javafx.scene.layout.Pane;

import java.io.IOException;

public class Map {

    public Map(MainStage mainStage) throws IOException {
        System.out.print("Jestem w Mapie");

        mainStage.getRootElem().getChildren().clear();
    }
}