package com.bomberman.fields;

import com.bomberman.gui.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class NormalBlock extends Block {

    public NormalBlock(int x, int y, boolean destroyable, boolean empty) {
        super(x, y, destroyable);
        this.empty = empty;
        if ((this.isDestroyable()) && (!this.isEmpty())) {
            initDestroyableBLock();
        } else if ((!this.isDestroyable()) && (!this.isEmpty())) {
            this.imagePath = "images/Blocks/unDestroyableBlock.png";
        } else {
            this.imagePath = "images/Blocks/defaultBlock.png";
        }
    }

    public static ImageView printNormalBlock(int x, int y) {
        Image img = new Image("file:" + "images/Blocks/defaultBlock.png");
        ImageView imgView = new ImageView(img);
        imgView.setX(x * Consts.PIXEL_SIZE);
        imgView.setY(y * Consts.PIXEL_SIZE);
        return imgView;
    }

    private void initDestroyableBLock() {
        this.imagePath = "images/Blocks/destroyableBlock.png";
        Random generator = new Random();
        if (generator.nextDouble() <= Consts.SPECIAL_BLOCK_PROB) {       //utworz bonus pod spodem
            this.fieldUnderDestryableField = new Bonus(this.x, this.y);
//            System.out.println(this.fieldUnderDestryableField.getType());
        } else {
            this.fieldUnderDestryableField = null;
        }
    }


}
