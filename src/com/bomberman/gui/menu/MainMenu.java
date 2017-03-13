package com.bomberman.gui.menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenu{
    private MenuButtons gameMenu;

    public MainMenu(MainStage mainStage) throws IOException {
        InputStream background = Files.newInputStream(Paths.get("images/logos.png"));
        Image img = new Image(background);
        background.close();

        ImageView view = new ImageView(img);
        //dopasowywanie obrazu do ramki funkcja view.setFitWidth/Height

        gameMenu = new MenuButtons(mainStage);
        mainStage.getRootElem().getChildren().addAll(view, gameMenu);
    }
}