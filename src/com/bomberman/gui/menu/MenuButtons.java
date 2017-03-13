package com.bomberman.gui.menu;

import com.bomberman.Listener;
import com.bomberman.gui.Buttons;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuButtons extends Parent {

    Listener list;
    public MenuButtons(MainStage mainStage) {
        VBox menu = new VBox(20);
        menu.setTranslateX(200);
        menu.setTranslateY(280);
        list = new Listener(mainStage);

        Buttons buttonNewGame = new Buttons("Nowa gra");
        Buttons buttonHighScores = new Buttons("Ranking");
        Buttons buttonExit = new Buttons("Wyjście");
        buttonNewGame.setOnMouseClicked(event -> {
            try {
                new Map(mainStage);
                list.listen();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menu.getChildren().addAll(buttonNewGame, buttonHighScores, buttonExit);
        getChildren().addAll(menu);
    }
}