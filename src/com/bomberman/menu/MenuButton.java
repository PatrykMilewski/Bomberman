package com.bomberman.menu;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MenuButton extends StackPane {
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