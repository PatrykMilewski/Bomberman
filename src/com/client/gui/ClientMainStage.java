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
    
    private ClientMainStage clientMainStage;

    // Child controllers
    @FXML
    public static MainStageController mainStageController;
    @FXML
    public static GameController gameController;
    @FXML
    public static LobbyController lobbyController;
    @FXML
    public static HighscoresController highscoresController;
    @FXML
    protected static Pane primaryPane;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        loader = new FXMLLoader(getClass().getResource("interfaceControllers/fxmlFiles/MainStage.fxml"));
        root = loader.load();
        mainStageController = loader.getController();
        primaryStage.setTitle("Bomberman");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        Client client = new Client(InetAddress.getLocalHost(), ClientConsts.PORT, this);

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Pane getRootElem() {
        return root;
    }
}