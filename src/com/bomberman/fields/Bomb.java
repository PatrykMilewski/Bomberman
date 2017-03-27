package com.bomberman.fields;

import javafx.scene.image.ImageView;

public class Bomb extends Field
{
    private int range;
    private long startTime;
    private Player ownerOfBomb;

    public long getStartTime() {
        return startTime;
    }

    public Bomb(int x, int y, boolean destroyable, Player player) {
        super(x, y, destroyable);
        this.range = 1;
        this.startTime = System.currentTimeMillis();
        this.imagePath = "images/Blocks/bomb/bomb.gif";
        this.ownerOfBomb = player;
    }

    public void explode() {
        System.out.print("Rozpeirdol bomby " + this.getX() + "  " + this.getY() +"\n");
        this.ownerOfBomb.incNBombs();
    }
}
