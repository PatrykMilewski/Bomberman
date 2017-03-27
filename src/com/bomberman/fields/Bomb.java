package com.bomberman.fields;

import javafx.scene.image.ImageView;

public class Bomb extends Field
{
    private int range;
    private int timer;

    public Bomb(int x, int y, boolean destroyable) {
        super(x, y, destroyable);
        this.range = 1;
        this.timer = 3;
        this.imagePath = "images/Blocks/bomb/bomb.gif";
    }

    public boolean decraseTimer() {
        timer--;
        if (timer != 0)
            return false;
        else
            return true;
    }

}
