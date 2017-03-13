package com.bomberman.fields;

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
}
