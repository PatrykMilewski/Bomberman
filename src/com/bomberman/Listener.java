package com.bomberman;

import com.bomberman.fields.Player;
import com.bomberman.gui.menu.MainStage;
import javafx.animation.AnimationTimer;


public class Listener
{
    boolean up, down, left, right, bomb;
    private MainStage mainStage;
    private Player player;

    public Listener(MainStage mainSt)
    {
        this.mainStage = mainSt;
        this.player = mainStage.getPlayer();
        up = down = left = right = bomb = false;
    }

    public void listen()
    {
        mainStage.getStage().getScene().setOnKeyPressed(event ->
                {
                    switch (event.getCode()) {
                        case UP: up = true; System.out.println("up");  break;
                        case DOWN:  down = true; System.out.println("down"); break;
                        case LEFT:  left  = true; System.out.println("left"); break;
                        case RIGHT: right  = true; System.out.println("tight"); break;
                        case SPACE: bomb = true; System.out.println("bomba"); break;
                    }
                }
        );

        mainStage.getStage().getScene().setOnKeyReleased(event ->
        {
                switch (event.getCode()) {
                    case UP:    up = false; System.out.println("up rel"); break;
                    case DOWN:  down = false; System.out.println("down rel"); break;
                    case LEFT:  left  = false; System.out.println("left rel"); break;
                    case RIGHT: right  = false; System.out.println("right rel"); break;
                    case SPACE: bomb = false; System.out.println("bomb rel"); break;
                }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (up)  player.incCoords(0,-1);
                if (down) player.incCoords(0,1);
                if (left)  player.incCoords(-1,0);
                if (right)  player.incCoords(1,0);
                // bomb TODO

            }
        };
        timer.start();
    }
}
