package com.bomberman;

import com.bomberman.gui.MainStage;
import com.bomberman.gui.GameMap;

public class Game
{
    Listener gameListener;
    private boolean isRunning;

    public Game(MainStage mainStage, GameMap map)
    {
        map.createPlayer(0 , 0, "Milewski");
        isRunning = true;
        gameListener = new Listener(mainStage, map);
    }

    public void GameLoop() {
        gameListener.listen();
    }

}
