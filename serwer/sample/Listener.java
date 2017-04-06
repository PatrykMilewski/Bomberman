package sample;

import javafx.animation.AnimationTimer;


public class Listener {
    boolean up, down, left, right, bomb;
    private long lastDropedBomb;
    private long lastMove;
    private MainStage mainStage;
    private Client client;

    public Listener(MainStage mainSt, Client client) {
        this.mainStage = mainSt;
        this.client = client;
        lastMove = 0;
        lastDropedBomb = 0;
        up = down = left = right = bomb = false;
    }

    public void listen() {
        mainStage.getPrimaryStage().getScene().setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case UP:    up =    true;   break;
                        case DOWN:  down =  true;   break;
                        case LEFT:  left =  true;   break;
                        case RIGHT: right = true;   break;
                        case SPACE: bomb =  true;   break;
                    }
                }
        );

        mainStage.getPrimaryStage().getScene().setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:    up =    false;      break;
                case DOWN:  down =  false;      break;
                case LEFT:  left =  false;      break;
                case RIGHT: right = false;      break;
                case SPACE: bomb =  false;      break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (up) {
                    if (now / Consts.TIME_CUTTER - lastMove > (Consts.TIME_BETWEEN_MOVES )) { //TODO playergetspid tu jakis byl
                        lastMove = now / Consts.TIME_CUTTER;
                        System.out.print("Gora");
                        client.sendKey("UP");
                        //player.incCoords(0, -1);
                    }
                }

                if (down) {
                    if (now / Consts.TIME_CUTTER - lastMove > Consts.TIME_BETWEEN_MOVES ) {
                        lastMove = now / Consts.TIME_CUTTER;
                        client.sendKey("DOWN");
                        //player.incCoords(0, 1);
                    }
                }

                if (left) {
                    if (now / Consts.TIME_CUTTER - lastMove >(Consts.TIME_BETWEEN_MOVES)) {
                        lastMove = now / Consts.TIME_CUTTER;
                        client.sendKey("LEFT");
                        //player.incCoords(-1, 0);
                    }
                }

                if (right) {
                    if (now / Consts.TIME_CUTTER - lastMove > (Consts.TIME_BETWEEN_MOVES)) {
                        lastMove = now / Consts.TIME_CUTTER;
                        client.sendKey("RIGHT");
                        //player.incCoords(1, 0);
                    }
                }

                if (bomb) {
                    if (now / Consts.TIME_CUTTER - lastDropedBomb > Consts.TIME_BETWEEN_BOMBS) {
                        lastDropedBomb = now / Consts.TIME_CUTTER;
                        //player.dropBomb();
                    }
                }
            }
        };
        timer.start();
    }
}