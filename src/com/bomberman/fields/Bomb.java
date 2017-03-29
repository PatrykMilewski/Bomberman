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
        this.range = 1;
        this.startTime = System.currentTimeMillis();
        this.imagePath = "images/Blocks/bomb/bomb.gif";
        this.ownerOfBomb = player;
        this.range = player.getRangeOfBomb();
        this.map = map;
    }

    public long getStartTime() {
        return startTime;
    }

    public void explode() {
        this.map.setMapField(this.x, this.y, new NormalBlock(this.x, this.y, false, true));

        Fire newFire = new Fire(this.x, this.y, true, this.map);
        this.map.addFire(newFire);

        boolean firstUp = false;
        boolean firstRight = false;
        boolean firstDown = false;
        boolean firstLeft = false;
        for (int i = 1; i < range + 1; i++) {
            if (this.y - i >= 0 && !firstUp) {                               //up
                firstUp = checkField(0, -i);
            }
            if (this.x + i < Consts.DIMENSION && !firstRight) {                 //right
                firstRight = checkField(i, 0);
            }
            if (this.y + i < Consts.DIMENSION && !firstDown) {                 //down
                firstDown = checkField(0, i);
            }
            if (this.x - i >= 0 && !firstLeft) {                              //left
                firstLeft = checkField(-i, 0);
            }
        }
        this.ownerOfBomb.incNBombs();
    }

    private boolean checkField(int diffX, int diffY) {
        if (map.getMapField(this.x + diffX, this.y + diffY) instanceof Bomb) {
            //((Bomb) map.getMapField(this.x + diffX, this.y + diffY)).explode();           //TODO problem z usuwaniem z listy w BombTimer
        }
        if (map.getMapField(this.x + diffX, this.y + diffY).isDestroyable() == false        //niezniszczalny kloc
                && map.getMapField(this.x + diffX, this.y + diffY).isEmpty() == false) {
            return true;
        } else {
            Fire newFire = new Fire(this.x + diffX, this.y + diffY, false, this.map);
            if (map.getMapField(this.x + diffX, this.y + diffY).isDestroyable()) {               //do zniszczenia
                map.destroyField(this.x + diffX, this.y + diffY, newFire);
                this.map.addFire(newFire);
                return true;
            } else {
                this.map.setMapField(this.x + diffX, this.y + diffY, newFire);
                this.map.addFire(newFire);
                return false;
            }
        }
    }
}
