package com.bomberman.gui.menu;

import com.bomberman.fields.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainStage extends Application {
    private Stage stage;
    private Pane rootElem;
    private Scene mainScene;
    private Player player;

    @Override
    public void start (Stage stage) throws Exception {
        this.player = new Player(0,0, true, "Mileski");   //TODO dobrze

        this.stage = stage;
        this.stage.setTitle("Bomberman");
        this.rootElem = new Pane();
        rootElem.setPrefSize(800, 600);
        new MainMenu(this);

        this.mainScene = new Scene(rootElem);
        stage.setScene(mainScene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public Pane getRootElem() {
        return rootElem;
    }


    public static void main(String args[]) {
        launch(args);
    }

    public Player getPlayer() {return player;}
}