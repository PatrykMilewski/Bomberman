package com.client;

import com.client.gui.ClientConsts;
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
                    String mapp = jObject.getString("fields");
                    System.out.println(mapp);
                    for (int i = 0; i < ClientConsts.DIMENSION; i++)
                        for (int j = 0; j < ClientConsts.DIMENSION; j++) {
                            int field = Integer.parseInt(mapp.substring(0, 1));
                            mapp = mapp.substring(1);
                            map.setMapField(i, j, field);
                            if (field == 3){
                                int y = i;
                                int x = j;
                                Platform.runLater(() -> map.printOneField(x, y, 3));
                            }
                        }
                    Platform.runLater(() -> map.printEntireMap());
                } else if (cmd.equals("move")) {
                    JSONArray fields = jObject.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject temp = fields.getJSONObject(i);
//                        System.out.println(temp);
//                        Platform.runLater(() ->map.setMapField(temp.getInt("x"),temp.getInt("y"),temp.getInt("field")));    //TODO
                        Platform.runLater(() -> map.printOneField(temp.getInt("x"), temp.getInt("y"), temp.getInt("field")));
                    }
                } else if (cmd.equals("incspeed")) {
                    System.out.println("Zwiekszam moja predkosc");
                    //TODO MUL tutaj zrobi zwiekszenie predkosci gracza
                }
            }
        }
    }
}
