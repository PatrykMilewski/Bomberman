package com.client;

import com.client.gui.interfaceControllers.LobbyController;
import com.elements.interfaces.Callback;
import com.elements.loggers.LoggerFactory;
import com.server.MessageQueue;
import com.server.WholeMapSender;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class ClientMessageHandler extends Task implements Callback {
    private static Logger log = LoggerFactory.getLogger(ClientMessageHandler.class.getCanonicalName());

    private ClientMessageQueue messageQueue;
    private Client client;
    private boolean stay;
    private LobbyController lobbyController;

    ClientMessageHandler(ClientMessageQueue messageQueue, Client client, LobbyController lobbyController) {
        this.messageQueue = messageQueue;
        this.client = client;
        stay = true;
        this.lobbyController = lobbyController;
    }

    @Override
    protected synchronized Object call() {
        String message = null;
        while (stay) {
            while (message == null) {
                if (!messageQueue.isEmpty())
                    message = messageQueue.pop();
                else {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }
            
            log.info("Received message: " + message);

            handleMessage(message);
        }
        return null;
    }
    
    @Override
    public void callback() {
        notifyAll();
    }
    
    private void handleMessage(String message) {
        JSONObject msg = new JSONObject(message);
        String status = msg.getString("status");
    
        if (status != null) {
            if (status.equals("OK")) {
                client.setMyId(msg.getInt("id"));
                log.info("New client: " + client.getID() + "\tWaiting for game to start.");
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
            } else if (status.equals("hs")){
                statusHs(msg);
            }
            else {
                log.warning("Failed to connect to server");
            }
        }
    }
    
    private void statusHs(JSONObject msg) {
        JSONArray highScores = msg.getJSONArray("hscores");
        client.updateHighScores(highScores);
    }

    private void statusChangeSlot(JSONObject msg) {
        int newSlot = msg.getInt("slotId");
        Platform.runLater(() -> client.setSlotId(newSlot));
    }

    private void statusUpdateSlots(JSONObject msg) {
        int slotId = msg.getInt("slotId");
        String text = msg.getString("text");
        Platform.runLater(() -> lobbyController.setPlayersSlot(slotId, text));
    }
}
