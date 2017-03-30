package com.bomberman.gui;

import com.bomberman.gui.interfaceControllers.GameController;
import com.bomberman.gui.interfaceControllers.HighscoresController;
import com.bomberman.gui.interfaceControllers.LobbyController;
import com.bomberman.gui.interfaceControllers.MainStageController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class MainStage extends Application {
    protected static  FXMLLoader loader;
    protected static boolean debug = false;
    protected static Logger log = Logger.getLogger(MainStage.class.getCanonicalName());
    protected static Stage primaryStage;
    protected static Pane root;
    protected static Scene scene;
    // Child controllers
    @FXML
    public MainStageController mainStageController;
    @FXML
    public GameController gameController;
    @FXML
    public LobbyController lobbyController;
    @FXML
    public HighscoresController highscoresController;
    @FXML
    protected Pane primaryPane;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainStage.primaryStage = primaryStage;
        loader = new FXMLLoader(getClass().getResource("interfaceControllers/fxmlFiles/MainStage.fxml"));
        root = loader.load();
        mainStageController = loader.getController();
        primaryStage.setTitle("Bomberman");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getStage() {
        return primaryStage;
    }

    public Pane getRootElem() {
        return root;
    }
}
