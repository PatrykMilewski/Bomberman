package com.bomberman;

import com.client.gui.ClientMainStage;
import com.bomberman.gui.GameMap;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game
{
    private boolean isRunning;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public Game(ClientMainStage mainStage, GameMap map) {
        map.createPlayer(0 , 0, "Milewski");
        isRunning = true;

        Task fireTimerTask = new FireTimer(map);
        executor.submit(fireTimerTask);
    }

}
