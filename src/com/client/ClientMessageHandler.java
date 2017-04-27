package com.client;

import com.client.gui.interfaceControllers.LobbyController;
import com.server.fields.Player;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.io.IOException;

public class ClientMessageHandler extends Task {

    private ClientMessageQueue messageQueue;
    private Client client;
    boolean stay;
    private LobbyController lobbyController;

    public ClientMessageHandler(ClientMessageQueue messageQueue, Client client, LobbyController lobbyController) {
        this.messageQueue = messageQueue;
        this.client = client;
        stay = true;
        this.lobbyController = lobbyController;
    }

    @Override
    protected Object call() throws Exception {

        while (stay) {
            String message = null;
            while (message == null) {
                if (!messageQueue.isEmpty()) {
                    message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
                }
            }
            System.out.println("Z ClientMessageq: " + message);
            JSONObject msg = new JSONObject(message);
            String status = msg.getString("status");

            if (status != null) {
                if (status.equals("OK")) {
                    client.setMyId(msg.getInt("id"));
                    System.out.println("Moj Id: " + client.getID() + "\tCzekam na rozpoczecie gry"); //TODO Ready
                } else if (status.equals("start")) {
                    Platform.runLater(() -> {
                        try {
                            client.startGame();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                     });
                    stay = false;
                } else if (status.equals("changeSlot")){
                    statusChangeSlot(msg);
                } else if (status.equals("updateSlots")){
                    statusUpdateSlots(msg);
                } else {
                    System.out.println("Nie udalo sie polaczyc z serverem");
                }
            }
        }
        return null;
    }

    private void statusChangeSlot(JSONObject msg) {
        int newSlot = msg.getInt("slotId");
        Platform.runLater(() -> client.setSlotId(newSlot));
    }

    private void statusUpdateSlots(JSONObject msg) {
        System.out.println("status slot metod: " + msg.toString());
        int slotId = msg.getInt("slotId");
        String text = msg.getString("text");
        Platform.runLater(() -> lobbyController.setPlayersSlot(slotId, text));
    }
}
