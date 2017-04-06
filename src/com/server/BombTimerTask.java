package com.server;

import com.server.fields.Bomb;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class BombTimerTask extends Task {
    private Bomb bomb;

    public BombTimerTask(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            if ((System.currentTimeMillis() - bomb.getStartTime()) > Consts.MILIS_TO_EXPLODE) {
//                Platform.runLater(() -> bomb.explode());
                return null;
            }
        }
    }
}


