package com.bomberman.fields;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by rados on 13.03.2017.
 */
public class NormalBlock extends Block
{
    private boolean empty;


    public NormalBlock(int x, int y, boolean destroyable, boolean empty) {
        super(x, y, destroyable);
        this.empty = empty;
        if (this.canBeDestroyed()){
            this.imagePath = "images/destroyableBlock.png";
        } else {
            this.imagePath = "images/testBlock.png";

        }
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public Rectangle printFiled(int x, int y) {
        Rectangle textBackground = new Rectangle();
        if (!this.isEmpty()){
            textBackground = new Rectangle(x*25, y*25, 25,25);
            textBackground.setFill(Color.BLACK);
        }

        return textBackground;
    }
}
