package com.bomberman.fields;

import javafx.scene.image.ImageView;

/**
 * Created by rados on 13.03.2017.
 */
public class Bomb extends Field
{
    private int range;

    public Bomb(int x, int y, boolean destroyable) {
        super(x, y, destroyable);
        this.range = 2;
    }

    @Override
    public ImageView printFiled(int x, int y) {
        return null;
    }
}
