package com.bomberman.fields;

/**
 * Created by rados on 13.03.2017.
 */
public class NormalBlock extends Block
{
    private boolean empty;

    public NormalBlock(int x, int y, boolean empty) {
        super(x, y);
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
