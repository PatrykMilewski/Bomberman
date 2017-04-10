package com.server.fields;

import com.server.Consts;
import java.util.Map;
import java.util.HashMap;

public class Player extends Field {
    private String nick;
    private float speed;
    private int nBombs;
    private int rangeOfBomb;
    private boolean isAlive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Player(int x, int y, boolean destroyable, String nick) {
        super(x, y, destroyable);
        this.nick = nick;
        this.isAlive = true;
        this.name = "Player";
        this.speed = 10;
        this.nBombs = 1;
        this.rangeOfBomb = 1;
    }

    public String getName() {
        return name;
    }

    public int getRangeOfBomb() {
        return rangeOfBomb;
    }

    public int getNBombs(){
        return nBombs;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void move(int diffX, int diffY) {
        this.x += diffX;
        this.y += diffY;
    }

    public void kill() {
        if (isAlive()){
            System.out.print("ZGON!");
            isAlive = false;
        }
    }

    public void incSpeed() {
        if (this.speed < Consts.MAX_SPEED) {
            this.speed++;
        }
    }

    public void incNBombs() {
        if (this.nBombs < Consts.MAX_N_BOMBS) {
            this.nBombs++;
        }
    }

    public void incRange() {
        if (this.rangeOfBomb < Consts.MAX_RANGE_OF_BOMB) {
            this.rangeOfBomb++;
        }
    }

    public void decNBombs() {
        nBombs--;
    }
}
