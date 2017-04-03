package com.bomberman.fields;

import com.bomberman.gui.Consts;
import com.bomberman.gui.GameMap;

public class Bomb extends Field {
    private int range;
    private long startTime;
    private Player ownerOfBomb;
    private GameMap map;

    public Bomb(int x, int y, boolean destroyable, Player player, GameMap map) {
        super(x, y, destroyable);
        this.startTime = System.currentTimeMillis();
        this.imagePath = "images/Blocks/bomb/bomb.gif";
        this.ownerOfBomb = player;
        this.range = player.getRangeOfBomb();
        this.map = map;
    }

    public synchronized long getStartTime() {
        return startTime;
    }

    public void explode() {
        if (this.getX() == ownerOfBomb.getX() && this.getY() == ownerOfBomb.getY()){
            ownerOfBomb.kill();
        }
        Fire newFire = new Fire(this.x, this.y, true, this.map);
        map.destroyField(this.x, this.y, newFire);
        this.map.addFire(newFire);

        boolean firstUp = false;
        boolean firstRight = false;
        boolean firstDown = false;
        boolean firstLeft = false;
        for (int i = 1; i < range + 1; i++) {
            if (this.y - i >= 0 && !firstUp) {                                  //up
                firstUp = checkFieldToBurn(0, -i);
            }
            if (this.x + i < Consts.DIMENSION && !firstRight) {                 //right
                firstRight = checkFieldToBurn(i, 0);
            }
            if (this.y + i < Consts.DIMENSION && !firstDown) {                  //down
                firstDown = checkFieldToBurn(0, i);
            }
            if (this.x - i >= 0 && !firstLeft) {                                //left
                firstLeft = checkFieldToBurn(-i, 0);
            }
        }
        this.ownerOfBomb.incNBombs();
    }

    private boolean checkFieldToBurn(int diffX, int diffY) {
        if (map.getMapField(this.x + diffX, this.y + diffY) instanceof Bomb) {
            ((Bomb) map.getMapField(this.x + diffX, this.y + diffY)).decTime();
            return true;
        }
        if (!map.getMapField(this.x + diffX, this.y + diffY).isDestroyable()        //niezniszczalny kloc
                && !map.getMapField(this.x + diffX, this.y + diffY).isEmpty()) {
            return true;
        } else {

            Fire newFire = new Fire(this.x + diffX, this.y + diffY, false, this.map);
            if (map.getMapField(this.x + diffX, this.y + diffY).isDestroyable()) {               //do zniszczenia
                map.destroyField(this.x + diffX, this.y + diffY, newFire);
                this.map.addFire(newFire);
                return true;
            } else {
/*                if (map.getMapField(this.x + diffX, this.y + diffY) instanceof Fire) {            //TODO jesli ogien ma przechodzic przez ogien (nowe watki?)
                    ((Fire) map.getMapField(this.x + diffX, this.y + diffY)).removeFire();
                }*/
                this.map.setMapField(this.x + diffX, this.y + diffY, newFire);
                this.map.addFire(newFire);
                return false;
            }
        }
    }

    private void decTime() {
        this.startTime = System.currentTimeMillis() - Consts.MILIS_TO_EXPLODE + 300;        //po 300 ms wybuchnie bomba
    }
}
