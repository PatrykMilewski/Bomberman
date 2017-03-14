package com.bomberman.fields;

import com.bomberman.gui.menu.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;
import java.util.HashMap;

public class Player extends Field
{


    private String name;
    private int index;
    private float speed;
    private Map<SuperPowers, Boolean> superPowers = new HashMap<SuperPowers, Boolean>();

    public Player(int x, int y, boolean destroyable, String name) {
        super(x, y, destroyable);
        this.name = name;
        index = 0;
        superPowers.put(SuperPowers.invisible, false);
        superPowers.put(SuperPowers.faster, false);
        superPowers.put(SuperPowers.terminator, false);
        this.imagePath = "images/Blocks/playerBlock.png";

    }

    public boolean getSuperPower(String s)
    {
        return superPowers.get(s);
    }

    public void incCoords (int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    public int getindex() { return index; }
    public float getSpeed() { return speed; }
    public int getIndex() {return index; }
    public String getName() { return name; }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void setSuperPowers(SuperPowers superPower, Boolean trig)
    {
        superPowers.put(superPower, trig);
    }

    @Override
    public ImageView printFiled(int x, int y) {
        Image img = new Image("file:"+getImagePath());
        ImageView imgView = new ImageView(img);
        imgView.setX(x* Consts.PIXEL_SIZE);                         //TODO zmienic na pixelowe wartosci
        imgView.setY(y*Consts.PIXEL_SIZE);
        return imgView;
    }
}
