package com.bomberman.menu;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class MenuButtons extends Parent {

    public MenuButtons() {
        VBox menu = new VBox(20);
        menu.setTranslateX(200);
        menu.setTranslateY(280);

        MenuButton buttonNewGame = new MenuButton("Nowa gra");
        MenuButton buttonHighScores = new MenuButton("Ranking");
        MenuButton buttonExit = new MenuButton("Wyjście");

        menu.getChildren().addAll(buttonNewGame, buttonHighScores, buttonExit);
        getChildren().addAll(menu);
    }
}