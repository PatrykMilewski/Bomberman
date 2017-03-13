package com.bomberman.fields;

public class SpecialBlock extends Block {
    private int type;

    public SpecialBlock(int x, int y, boolean destroyable, int type) {
        super(x, y, destroyable);
        this.type = type;
    }

    public int getType() { return type; }

}
