package com.bomberman.fields;

import javafx.scene.image.ImageView;

/**
 * Created by rados on 13.03.2017.
 */
public class Bonus extends Field
{
    int type;

    public Bonus(int x, int y, boolean destroyable, int type) {
        super(x, y, destroyable);
        this.type = type;
    }

    @Override
    public ImageView printFiled(int x, int y) {
        return null;
    }
}
