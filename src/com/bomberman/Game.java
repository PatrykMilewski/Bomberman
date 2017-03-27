package com.bomberman;

import com.bomberman.gui.MainStage;
import com.bomberman.gui.GameMap;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game
{
    Listener gameListener;
    private boolean isRunning;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public Game(MainStage mainStage, GameMap map) {
        map.createPlayer(0 , 0, "Milewski");
        isRunning = true;
        gameListener = new Listener(mainStage, map);
        Task bombTimerTask = new BombTimer(map);
        executor.submit(bombTimerTask);
        Task fireTimerTask = new FireTimer(map);
        executor.submit(fireTimerTask);
    }

    public void GameLoop() {
        gameListener.listen();
    }

}
