package com.bomberman;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenu extends Application {
    private Menu gameMenu;

    @Override
    public void start (Stage stage) throws Exception {
        stage.setTitle("Bomberman");
        Pane rootElem = new Pane();
        rootElem.setPrefSize(800,600);

        InputStream background = Files.newInputStream(Paths.get("images/logos.png"));
        Image img = new Image(background);
        background.close();

        ImageView view = new ImageView(img);
        //dopasowywanie obrazu do ramki funkcja view.setFitWidth/Height

        gameMenu = new Menu();
        rootElem.getChildren().addAll(view, gameMenu);

        Scene scene = new Scene(rootElem);
        stage.setScene(scene);
        stage.show();
    }

    private class Menu extends Parent {
        public Menu() {
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

    private static class MenuButton extends StackPane {
        private Text text;

        public MenuButton(String txt) {
            text = new Text(txt);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle textBackground = new Rectangle(400,80);
            textBackground.setFill(Color.BLACK);
            textBackground.setOpacity(0.6);
            getChildren().addAll(textBackground, text);

            setOnMouseEntered(event -> {
                textBackground.setFill(Color.YELLOWGREEN);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event -> {
                textBackground.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });
        }
    }

    public static void main(String args[]) {
        launch(args);
    }
}