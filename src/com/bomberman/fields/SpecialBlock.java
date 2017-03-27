package com.bomberman.fields;

import javafx.scene.image.ImageView;

public class SpecialBlock extends Block {
    private int type;

    public SpecialBlock(int x, int y, boolean destroyable, int type) {
        super(x, y, destroyable);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public ImageView printFiled() {
        return null;
    }
}
