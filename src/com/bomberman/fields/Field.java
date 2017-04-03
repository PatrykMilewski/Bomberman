package com.bomberman.fields;
import com.bomberman.gui.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public abstract class Field {
    protected int x;
    protected int y;
    protected boolean destroyable;
    protected boolean empty;
    protected String imagePath;
    protected Bonus fieldUnderDestryableField;

    public Field(int x, int y, boolean destroyable) {
        this.x = x;
        this.y = y;
        this.destroyable = destroyable;
        this.empty = false;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public Bonus getFieldUnderDestryableField() {
        return fieldUnderDestryableField;
    }

    public boolean isEmpty() { return empty; }
    public boolean isDestroyable(){
        return destroyable;
    }

    public ImageView printFiled() {
        Image img = new Image("file:"+getImagePath());
        ImageView imgView = new ImageView(img);
        imgView.setX(this.x * Consts.PIXEL_SIZE);
        imgView.setY(this.y * Consts.PIXEL_SIZE);
        return imgView;
    }
}
