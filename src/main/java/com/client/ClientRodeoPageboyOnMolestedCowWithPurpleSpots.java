package com.client;

import com.client.gui.ClientConsts;
import com.elements.loggers.LoggerFactory;
import com.server.MessageHandler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

public class ClientRodeoPageboyOnMolestedCowWithPurpleSpots extends Task {
    
    private static Logger log = LoggerFactory.getLogger(ClientRodeoPageboyOnMolestedCowWithPurpleSpots.class.getCanonicalName());
    
    private String cmd;
    private JSONObject jObject;
    private Client client;
    private ClientMap map;

    public ClientRodeoPageboyOnMolestedCowWithPurpleSpots(String cmd, JSONObject jObject, Client client, ClientMap map){
        this.cmd = cmd;
        this.jObject = jObject;
        this.client = client;
        this.map = map;
    }

    @Override
    protected Object call() throws Exception {

        if (cmd.equals("eMap")) {
            Platform.runLater(() -> map.printEntireMap(jObject));
        } else if (cmd.equals("move")) {
            synchronized (this){cmdMove(jObject); notify();}
        } else if (cmd.equals("incspeed")) {
            cmdIncspeed();
        } else if (cmd.equals("scores")){
            cmdScores(jObject);
        } else if (cmd.equals("escores")){
            cmdEScores(jObject);
        } else if (cmd.equals("hs")){
            cmdHs(jObject);
        }

        return null;
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
            log.info("Adding player " + playerId + ": " + playerNick + "to hiscores table.");
            client.newGameScore(playerId, playerNick);
        }
    }

    private void cmdHs(JSONObject jObject) {
        JSONArray highScores = jObject.getJSONArray("hscores");
        client.updateHighScores(highScores);
    }
}
