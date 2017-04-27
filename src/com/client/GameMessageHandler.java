package com.client;

import com.client.gui.ClientConsts;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

public class GameMessageHandler extends Task {
    private static Logger log = Logger.getLogger(GameMessageHandler.class.getCanonicalName());
    private static final boolean debug = true;
    
    private ClientMessageQueue messageQueue;
    private ClientMap map;
    private Client client;
    
    GameMessageHandler(ClientMessageQueue messageQueue, ClientMap map, Client client) {
        this.messageQueue = messageQueue;
        this.map = map;
        this.client = client;
    }
    
    @Override
    protected Object call() throws Exception {
        
        while (true) {
            String message = null;
            while (message == null) {
                if (!messageQueue.isEmpty())
                    message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
            }
            
            if (debug)
                log.info("Received message from server: " + message);
            
            JSONObject jObject = new JSONObject(message);
            String cmd = jObject.getString("cmd");
            if (cmd != null) {
                switch (cmd) {
                    case "eMap":
                        Platform.runLater(() -> map.printEntireMap(jObject));
                        break;
                    case "move":
                        cmdMove(jObject);
                        break;
                    case "incspeed":
                        cmdIncspeed();
                        break;
                    case "scores":
                        cmdScores(jObject);
                        break;
                    case "escores":
                        cmdEScores(jObject);
                        break;
                }
            }
        }
    }
    
    private void cmdMove(JSONObject jObject) {
        JSONArray fields = jObject.getJSONArray("fields");
        for (int i = 0; i < fields.length(); i++) {
            JSONObject temp = fields.getJSONObject(i);
            Platform.runLater(() -> map.printOneField(temp.getInt("x"), temp.getInt("y"), temp.getInt("field")));
        }
    }
    
    private void cmdIncspeed() {
        if (client.getPlayersTimeBetweenMoves() > ClientConsts.MAX_SPEED) {
            client.setPlayersTimeBetweenMoves(client.getPlayersTimeBetweenMoves()
                    - ClientConsts.SPEED_INCREMENT);
        }
    }
    
    private void cmdScores(JSONObject jObject) {
        client.setGameScore(jObject.getInt("player"), jObject.getInt("score"));
    }
    
    private void cmdEScores(JSONObject jObject) {       //inicjalizacja tabeli wyników
        JSONArray players = jObject.getJSONArray("plrs");
        for (int i = 0; i < players.length(); i++) {
            JSONObject temp = players.getJSONObject(i);
            int playerId = temp.getInt("id");
            String playerNick = temp.getString("nick");
            System.out.println("Dopisze do tabeli gracza o ID i nicku:  " + playerId + "\t\t" + playerNick);
            
            client.newGameScore(playerId, playerNick);
        }
    }
}
