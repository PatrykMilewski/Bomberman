package com.bomberman.fields;

public class Bonus extends Field
{
    int type;

    public Bonus(int x, int y, boolean destroyable, int type) {
        super(x, y, destroyable);
        this.type = type;
    }
}
