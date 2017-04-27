package com.client;

import com.client.gui.ClientConsts;
import com.server.fields.Player;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameMessageHandler extends Task {
    private ClientMessageQueue messageQueue;
    private ClientMap map;
    private Client client;

    public GameMessageHandler(ClientMessageQueue messageQueue, ClientMap map, Client client) {
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
            System.out.println("Z messageq KLIENTA: " + message);

            JSONObject jObject = new JSONObject(message);
            String cmd = jObject.getString("cmd");
            if (cmd != null) {
                if (cmd.equals("eMap")) {
                    Platform.runLater(() -> map.printEntireMap(jObject));
                } else if (cmd.equals("move")) {
                    cmdMove(jObject);
                } else if (cmd.equals("incspeed")) {
                    cmdIncspeed();
                } else if (cmd.equals("scores")){
                    cmdScores(jObject);
                } else if (cmd.equals("escores")){
                    cmdEScores(jObject);
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

    private void cmdIncspeed(){
        if (client.getPlayersTimeBetweenMoves() > ClientConsts.MAX_SPEED) {
            client.setPlayersTimeBetweenMoves(client.getPlayersTimeBetweenMoves()
                    - ClientConsts.SPEED_INCREMENT);
        }
    }

    private void cmdScores(JSONObject jObject) {
        int player = jObject.getInt("player");
        String score = jObject.getString("score");

        //TODO edycja wyników do wyświetlenia
    }

    private void cmdEScores(JSONObject jObject) {       //inicjalizacja tabeli wyników
        JSONArray players = jObject.getJSONArray("plrs");
        for (int i = 0; i < players.length(); i++){
            JSONObject temp = players.getJSONObject(i);
            int playerId = temp.getInt("id");
            String playerNick = temp.getString("nick");
            System.out.println("Dopisze do tabeli gracza o ID i nicku:  " + playerId + "\t\t" + playerNick);

        }
    }
}
