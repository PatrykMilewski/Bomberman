package com.bomberman.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Buttons extends StackPane {
    private Text text;

    public Buttons(String txt) {
        text = new Text(txt);
        text.setFont(text.getFont().font(20));
        text.setFill(Color.WHITE);

        Rectangle textBackground = new Rectangle(800,80);
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

        DropShadow onClickGlow = new DropShadow(50,Color.WHITE);
        onClickGlow.setInput(new Glow());

        this.setOnMousePressed(event -> setEffect(onClickGlow));
        this.setOnMouseReleased(event -> setEffect(null));
    }
}