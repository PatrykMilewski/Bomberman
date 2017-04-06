package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class MainStage extends Application {

    private static Stage primaryStage;
    private static Listener playerListener;
    public Stage getPrimaryStage() {return primaryStage;}

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        Client client = new Client(InetAddress.getLocalHost(), Consts.PORT);
        playerListener = new Listener(this, client);
        playerListener.listen();    //TODO Listen w nowym watku?

    }


    public static void main(String[] args) {
        launch(args);
    }
}
