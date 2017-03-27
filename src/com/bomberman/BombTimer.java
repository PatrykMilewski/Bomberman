package com.bomberman;

import com.bomberman.fields.Bomb;
import com.bomberman.gui.menu.Consts;
import com.bomberman.gui.menu.GameMap;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Szczepan on 27.03.2017.
 */
public class BombTimer extends Task {
    private ArrayList<Bomb> bombs;
    private GameMap map;
    public static boolean breakLoop = false;

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
                    if (System.currentTimeMillis() - tempBomb.getStartTime() > Consts.MILIS_TO_EXPLODE){
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
