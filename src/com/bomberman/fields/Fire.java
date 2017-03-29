package com.bomberman.fields;

import com.bomberman.gui.Consts;
import com.bomberman.gui.GameMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Szczepan on 27.03.2017.
 */
public class Fire extends Field {
    private long startTime;
    //    private Player ownerOfBomb;                   //TODO jesli bedziemy robic punktacje
    private GameMap map;
    private Bonus fieldUnderFireField;


    public Fire(int x, int y, boolean destroyable, GameMap map) {
        super(x, y, destroyable);
        this.empty = true;
        this.startTime = System.currentTimeMillis();
        this.imagePath = "images/Blocks/fireblock.png";
        this.map = map;
        this.fieldUnderFireField = null;
    }

    public static ImageView printFireBlock(int x, int y) {
        Image img = new Image("file:" + "images/Blocks/fireblock1.png");
        ImageView imgView = new ImageView(img);
        imgView.setX(x * Consts.PIXEL_SIZE);
        imgView.setY(y * Consts.PIXEL_SIZE);
        return imgView;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setUnderField(Bonus field) {
        this.fieldUnderFireField = field;
    }

    public void removeFire() {
        if (this.fieldUnderFireField == null) {
            map.destroyField(this.x, this.y, new NormalBlock(this.x, this.y, false, true));
        } else {
            map.printNormalBlockOnMap(this.x, this.y);
            map.destroyField(this.x, this.y, fieldUnderFireField);
        }
    }
}
