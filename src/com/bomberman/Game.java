package com.bomberman;

import com.bomberman.gui.menu.MainStage;
import com.bomberman.gui.menu.Map;

public class Game
{
    private boolean isRunning;
    Listener gameListener;

    public Game(MainStage mainStage, Map map)
    {
        isRunning = true;
        gameListener = new Listener(mainStage, map);

    }

    public void GameLoop() {
        gameListener.listen();
    }

}
