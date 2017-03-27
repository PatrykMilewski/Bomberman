package com.bomberman;

import com.bomberman.gui.menu.MainStage;
import com.bomberman.gui.menu.GameMap;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game
{
    private boolean isRunning;
    Listener gameListener;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Game(MainStage mainStage, GameMap map)
    {
        map.createPlayer(0 , 0, "Milewski");
        isRunning = true;
        gameListener = new Listener(mainStage, map);
        Task bomberTimerTask = new BombTimer(map);
        executor.submit(bomberTimerTask);
    }

    public void GameLoop() {
        gameListener.listen();
    }

}
