package com.bomberman;

import com.bomberman.fields.Bomb;
import com.bomberman.gui.Consts;
import com.bomberman.gui.GameMap;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class BombTimer extends Task {
    public static boolean breakLoop = false;
    private ArrayList<Bomb> bombs;
    private GameMap map;

    public BombTimer(GameMap map){
        this.map = map;
    }

    @Override
    protected Object call() throws Exception {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now){
                Iterator it = map.getBombs().iterator();
                while (it.hasNext()){
                    Bomb tempBomb = (Bomb) it.next();
                    if (breakLoop){
                        breakLoop = false;
                        break;
                    }
                    if ((System.currentTimeMillis() - tempBomb.getStartTime()) > Consts.MILIS_TO_EXPLODE){
                        tempBomb.explode();
                        it.remove();
                    }
                }
            }
        };
        timer.start();
        return null;
    }
}
