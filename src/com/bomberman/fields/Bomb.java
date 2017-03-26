package com.bomberman.fields;

import javafx.scene.image.ImageView;

public class Bomb extends Field
{
    private int range;
    private int timer;

    public Bomb(int x, int y, boolean destroyable) {
        super(x, y, destroyable);
        this.range = 2;
        this.timer = 3;
    }

    public boolean decraseTimer() {
        timer--;

        if (timer != 0)
            return false;
        else
            return true;
    }

    @Override
    public ImageView printFiled(int x, int y) {
        return null;
    }
}
