package com.bomberman;

import com.bomberman.fields.Bomb;
import com.bomberman.fields.Fire;
import com.bomberman.gui.menu.Consts;
import com.bomberman.gui.menu.GameMap;
import com.sun.javafx.tk.Toolkit;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Szczepan on 27.03.2017.
 */
public class FireTimer extends Task {
    private ArrayList<Fire> fires;
    private GameMap map;
    public static boolean breakLoop = false;

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

