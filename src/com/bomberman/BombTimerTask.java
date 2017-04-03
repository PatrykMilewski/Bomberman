package com.bomberman;

import com.bomberman.fields.Bomb;
import com.bomberman.gui.Consts;
import com.bomberman.gui.GameMap;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class BombTimerTask extends Task {
    private Bomb bomb;

    public BombTimerTask(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            if ((System.currentTimeMillis() - bomb.getStartTime()) > Consts.MILIS_TO_EXPLODE) {
                Platform.runLater(() -> bomb.explode());
                return null;
            }
        }
    }
}


