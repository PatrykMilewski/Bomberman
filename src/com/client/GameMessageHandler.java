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

    public GameMessageHandler(ClientMessageQueue messageQueue, ClientMap map) {
        this.messageQueue = messageQueue;
        this.map = map;
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
                    System.out.println("Zwiekszam moja predkosc");
                    //TODO MUL tutaj zrobi zwiekszenie predkosci gracza
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
}
