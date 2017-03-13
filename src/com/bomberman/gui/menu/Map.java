package com.bomberman.gui.menu;

import java.io.IOException;

public class Map {

    public Map(MainStage mainStage) throws IOException {
        System.out.print("Jestem w Mapie");

        mainStage.getRootElem().getChildren().clear();
    }
}