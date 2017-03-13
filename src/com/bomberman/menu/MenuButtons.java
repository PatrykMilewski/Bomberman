package com.bomberman.menu;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuButtons extends Parent {

    public MenuButtons(Pane rootElem) {
        VBox menu = new VBox(20);
        menu.setTranslateX(200);
        menu.setTranslateY(280);

        MenuButton buttonNewGame = new MenuButton("Nowa gra");
        MenuButton buttonHighScores = new MenuButton("Ranking");
        MenuButton buttonExit = new MenuButton("Wyjście");
        buttonNewGame.setOnMouseClicked(event -> {
            try {
                new Map(rootElem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menu.getChildren().addAll(buttonNewGame, buttonHighScores, buttonExit);
        getChildren().addAll(menu);
    }
}