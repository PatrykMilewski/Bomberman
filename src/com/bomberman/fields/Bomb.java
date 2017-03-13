package com.bomberman.fields;

public class Bomb extends Field
{
    private int range;

    public Bomb(int x, int y, boolean destroyable) {
        super(x, y, destroyable);
        this.range = 2;
    }
}
