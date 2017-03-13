package com.bomberman.fields;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NormalBlock extends Block
{
    private boolean empty;

    public NormalBlock(int x, int y, boolean destroyable, boolean empty) {
        super(x, y, destroyable);
        this.empty = empty;
        if ((this.canBeDestroyed()) && (!this.isEmpty())){
            this.imagePath = "images/Blocks/destroyableBlock.png";
        } else if ((!this.canBeDestroyed()) && (!this.isEmpty())){
            this.imagePath = "images/Blocks/unDestroyableBlock.png";
        } else {
            this.imagePath = "images/Blocks/defaultBlock.png";
        }
    }

    public boolean isEmpty() { return empty; }

    @Override
    public ImageView printFiled(int x, int y) {
        Image img = new Image("file:"+getImagePath());
        ImageView imgView = new ImageView(img);
        imgView.setX(x*25);
        imgView.setY(y*25);
        return imgView;
    }
}
