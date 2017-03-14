package com.bomberman.fields;
import com.bomberman.gui.menu.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Field {
    protected int x;
    protected int y;
    protected boolean destroyable;
    protected String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public Field(int x, int y, boolean destroyable) {
        this.x = x;
        this.y = y;
        this.destroyable = destroyable;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public Boolean canBeDestroyed() { return this.destroyable; }

    public ImageView printFiled(int x, int y) {
        Image img = new Image("file:"+getImagePath());
        ImageView imgView = new ImageView(img);
        imgView.setX(x* Consts.PIXEL_SIZE);
        imgView.setY(y*Consts.PIXEL_SIZE);
        return imgView;
    }
}
