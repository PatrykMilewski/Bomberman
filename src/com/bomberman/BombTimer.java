package com.bomberman;

import com.bomberman.fields.Bomb;
import com.bomberman.fields.NormalBlock;
import com.bomberman.gui.menu.Consts;
import com.bomberman.gui.menu.GameMap;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Szczepan on 27.03.2017.
 */
public class BombTimer extends Task {
    private ArrayList<Bomb> bombs;
    private GameMap map;

    public BombTimer(GameMap map){
        this.bombs = map.getBombs();
        System.out.print("\nJestem");
    }

    @Override
    protected Object call() throws Exception {
        while (true){
            Iterator it = bombs.iterator();
            while (it.hasNext()){
                Bomb tempBomb = (Bomb) it.next();
                if (System.currentTimeMillis() - tempBomb.getStartTime() > Consts.MILIS_TO_EXPLODE){
                    tempBomb.explode();
                    it.remove();
                }
            }
        }
    }
}
