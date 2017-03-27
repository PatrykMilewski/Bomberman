package com.bomberman;

import com.bomberman.gui.menu.MainStage;
import com.bomberman.gui.menu.GameMap;

public class Game
{
    private boolean isRunning;
    Listener gameListener;

    public Game(MainStage mainStage, GameMap map)
    {
        map.createPlayer(0 , 0, "Milewski");
        isRunning = true;
        gameListener = new Listener(mainStage, map);
        gameListener.listen();
    }

}
