package com.bomberman.fields;

import com.bomberman.gui.menu.Consts;
import com.bomberman.gui.menu.GameMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;
import java.util.HashMap;

public class Player extends Field {
    private String name;
    private int index;
    private float speed;
    private GameMap map;
    private int pixX;
    private int pixY;
    private int nBombs;
    private Map<SuperPowers, Boolean> superPowers = new HashMap<SuperPowers, Boolean>();

    public Player(int x, int y, boolean destroyable, String name, GameMap map) {
        super(x, y, destroyable);
        this.name = name;
        index = 0;
        superPowers.put(SuperPowers.invisible, false);
        superPowers.put(SuperPowers.faster, false);
        superPowers.put(SuperPowers.terminator, false);
        this.imagePath = "images/Blocks/playerBlock.png";
        this.map = map;
//        this.pixX = Consts.PIXEL_SIZE/2;
//        this.pixY = Consts.PIXEL_SIZE/2;
        this.speed = 2;
        this.nBombs = 2;
    }

    public boolean getSuperPower(String s){
        return superPowers.get(s);
    }

    public void incCoords (int x, int y){
        if(this.map.canMove(this.x + x, this.y + y)){
            if (map.getMapField(this.x, this.y) instanceof Bomb == false){      //jak postawi gracz bombę i jest pod nim, to nie usuwaj
                this.map.setMapField(this.x, this.y, new NormalBlock(this.x, this.y, false, true));
            }
            map.printFieldOfMap(this.x, this.y);
            this.x += x;
            this.y += y;
            this.map.setMapField(this.x, this.y, this);
            map.printFieldOfMap(this.x, this.y);
        }
    }

/*    public void incPixCords (int pixX, int pixY){
        for (int i = 0; i < speed; i++){
            this.pixX += pixX * speed;
            this.pixY += pixY * speed;
            map.printEntireMap();
        }
        if (this.pixX > Consts.PIXEL_SIZE){
            incCoords(1,0);
            this.pixX = Consts.PIXEL_SIZE/2;
        }
        if (this.pixY > Consts.PIXEL_SIZE){
            incCoords(0, 1);
            this.pixY = Consts.PIXEL_SIZE/2;
        }
        if (this.pixX < 0) {
            incCoords(-1, 0);
            this.pixX = Consts.PIXEL_SIZE/2;
        }
        if (this.pixX < 0) {
            incCoords(0, -1);
            this.pixY = Consts.PIXEL_SIZE/2;
        }
    }*/


    public int getindex() { return index; }
    public float getSpeed() { return speed; }
    public int getIndex() {return index; }
    public String getName() { return name; }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setSuperPowers(SuperPowers superPower, Boolean trig) {
        superPowers.put(superPower, trig);
    }

    public void dropBomb(){
        if (nBombs > 0){
            map.setMapField(this.x, this.y, new Bomb(this.x, this.y, true));
            nBombs--;
        }
        map.printFieldOfMap(this.x, this.y);
    }

/*    @Override
    public ImageView printFiled(() {
        Image img = new Image("file:"+getImagePath());
        ImageView imgView = new ImageView(img);
        imgView.setX(x* Consts.PIXEL_SIZE);
        imgView.setY(y*Consts.PIXEL_SIZE);
//        imgView.setX(x* Consts.PIXEL_SIZE + pixX - Consts.PIXEL_SIZE/2);                         //TODO zmienic na pixelowe wartosci
//        imgView.setY(y*Consts.PIXEL_SIZE + pixY - Consts.PIXEL_SIZE/2);
        return imgView;
    }*/
}
