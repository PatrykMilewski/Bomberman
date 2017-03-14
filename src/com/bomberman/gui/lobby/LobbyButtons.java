package com.bomberman.gui.lobby;

import com.bomberman.gui.Buttons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;


public class LobbyButtons extends Parent {
    public LobbyButtons() {
        VBox lobby = new VBox(20);
        lobby.setTranslateX(200);
        lobby.setTranslateY(100);

        Buttons join = new Buttons("Join Game");

        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList ();
        //for (int i = 0 ; i < )
        //items.add()
        list.setItems(items);


        //lobby.getChildren().addAll(serversList);

        //lobby.getChildren().addAll(buttonNewGame, buttonHighScores, buttonExit);
        //getChildren().addAll(lobby);
    }
    public void countServersAmount() {
        //todo
    }

    public void countPlayersAmount() {
        //todo
    }

    private int serversAmount;
    private int playersAmount;
}
