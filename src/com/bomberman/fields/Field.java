package com.bomberman.fields;


public abstract class Field
{
    protected int x;
    protected int y;


    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Boolean canBeDestroyed()
    {
        return false;
    }
}
