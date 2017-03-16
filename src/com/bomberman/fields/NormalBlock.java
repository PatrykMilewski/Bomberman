package com.bomberman.fields;

import com.bomberman.gui.menu.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NormalBlock extends Block
{

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

    public static ImageView printNormalBlock(int x, int y) {
        Image img = new Image("file:"+"images/Blocks/defaultBlock.png");
        ImageView imgView = new ImageView(img);
        imgView.setX(x* Consts.PIXEL_SIZE);
        imgView.setY(y* Consts.PIXEL_SIZE);
        return imgView;
    }
}
