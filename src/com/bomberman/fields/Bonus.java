package com.bomberman.fields;

/**
 * Created by rados on 13.03.2017.
 */
public class Bonus extends Field
{
    int type;

    public Bonus(int x, int y, int type) {
        super(x, y);
        this.type = type;
    }
}
