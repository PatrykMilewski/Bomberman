package com.bomberman.fields;

/**
 * Created by rados on 13.03.2017.
 */
public class SpecialBlock extends Block {
    private int type;

    public SpecialBlock(int x, int y, boolean destroyable, int type) {
        super(x, y, destroyable);
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
