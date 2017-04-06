package com.client.gui;

import com.client.Client;
import com.client.ClientListener;
import com.client.ClientMap;
import com.client.gui.interfaceControllers.GameController;
import com.client.gui.interfaceControllers.HighscoresController;
import com.client.gui.interfaceControllers.LobbyController;
import com.client.gui.interfaceControllers.MainStageController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.util.logging.Logger;

public class ClientMainStage extends Application {
    protected static FXMLLoader loader;
    protected static boolean debug = false;
    protected static Logger log = Logger.getLogger(ClientMainStage.class.getCanonicalName());
    protected static Stage primaryStage;
    protected static Pane root;
    protected static Scene scene;
    private static ClientListener playerListener;
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
        ClientMainStage.primaryStage = primaryStage;
        loader = new FXMLLoader(getClass().getResource("interfaceControllers/fxmlFiles/MainStage.fxml"));
        root = loader.load();
        mainStageController = loader.getController();
        primaryStage.setTitle("Bomberman");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        ClientMap map = new ClientMap();
        Client client = new Client(InetAddress.getLocalHost(), ClientConsts.PORT, map);
        playerListener = new ClientListener(this, client);
        playerListener.listen();    //TODO Listen w nowym watku?

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Pane getRootElem() {
        return root;
    }
}