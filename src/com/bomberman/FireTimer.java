package com.bomberman;

import com.bomberman.fields.Fire;
import com.bomberman.gui.Consts;
import com.bomberman.gui.GameMap;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;

import java.util.Iterator;

public class FireTimer extends Task {
    public static boolean breakLoop = false;
    private GameMap map;

    public FireTimer(GameMap map){
        this.map = map;
    }

    @Override
    protected Object call() throws Exception {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now){
                Iterator it = map.getFires().iterator();
                while (it.hasNext()){
                    Fire tempFire = (Fire) it.next();
                    if (breakLoop){
                        breakLoop = false;
                        break;
                    }
                    if (System.currentTimeMillis() - tempFire.getStartTime() > Consts.FIRE_MILIS){
                        tempFire.removeFire();
                        it.remove();
                    }
                }
            }
        };
        timer.start();
        return null;
    }
}

