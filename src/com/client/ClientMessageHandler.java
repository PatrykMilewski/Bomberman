package com.client;

import com.client.gui.interfaceControllers.LobbyController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class ClientMessageHandler extends Task {
    private static Logger log = Logger.getLogger(ClientMessageHandler.class.getCanonicalName());
    private static final boolean debug = true;
    
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
    protected Object call() throws Exception {
        
        while (stay) {
            String message = null;
            while (message == null) {
                if (!messageQueue.isEmpty()) {
                    message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
                }
            }
    
            if (debug)
                log.info("Received message: " + message);
            
            JSONObject msg = new JSONObject(message);
            String status = msg.getString("status");
            
            if (status != null) {
                switch (status) {
                    case "OK":
                        client.setMyId(msg.getInt("id"));
    
                        if (debug)
                            log.info("My Id: " + client.getID() + "\tWaiting for game to start.");
                        
                        break;
                    case "start":
                        Platform.runLater(() -> {
                            try {
                                client.startGame();
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        stay = false;
                        break;
                    case "changeSlot":
                        statusChangeSlot(msg);
                        break;
                    case "updateSlots":
                        statusUpdateSlots(msg);
                        break;
                    default:
                        if (debug)
                            log.info("Failed to connect to server.");
                        
                        break;
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
        int slotId = msg.getInt("slotId");
        String text = msg.getString("text");
        Platform.runLater(() -> lobbyController.setPlayersSlot(slotId, text));
    }
}
