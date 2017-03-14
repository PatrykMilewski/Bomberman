package com.bomberman;

import com.bomberman.fields.Player;
import com.bomberman.gui.menu.MainStage;
import javafx.animation.AnimationTimer;


public class Listener
{
    private static final int MOVIN = 20;
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
            int dx=0, dy=0;
            @Override
            public void handle(long now){
                if(up)
                {dy--;
                    if(dy==-1) player.incCoords(0,-1);
                    if(dy<-MOVIN) dy = 0;
                }

                if(down)
                {dy++;
                    if(dy==1) player.incCoords(0,1);
                    if(dy>MOVIN) dy = 0; }

                if(left)
                {dx--;
                    if(dx==-1) player.incCoords(-1,0);
                    if(dx<-MOVIN) dx = 0;}

                if(right)
                {dx++;
                    if(dx==1) player.incCoords(1,0);
                    if(dx>MOVIN) dx = 0;}

                // bomb TODO

            }
        };
        timer.start();
    }
}