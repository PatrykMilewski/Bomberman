package com.bomberman.gui.lobby;

import com.bomberman.gui.Buttons;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;


public class LobbyButtons extends Parent {
    public LobbyButtons() {
        VBox lobby = new VBox(20);
        lobby.setTranslateX(200);
        lobby.setTranslateY(100);

        Buttons join = new Buttons("Join Game");


        Buttons buttonNewGame = new Buttons("Nowa gra");
        Buttons buttonHighScores = new Buttons("Ranking");
        Buttons buttonExit = new Buttons("Wyjście");


        //lobby.getChildren().addAll(serversList);

        lobby.getChildren().addAll(buttonNewGame, buttonHighScores, buttonExit);
        getChildren().addAll(lobby);
    }
}
