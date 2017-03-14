package com.bomberman;

import com.bomberman.gui.menu.MainStage;
import com.bomberman.gui.menu.Map;

public class Game
{
    private boolean isRunning;
    Listener gameListener;
    Map map;

    public Game(MainStage mainStage, Map map)
    {
        isRunning = true;
        gameListener = new Listener(mainStage);
        this.map = map;
    }

    public void GameLoop()
    {
        gameListener.listen();

    }


}
