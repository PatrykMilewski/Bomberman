package com.client;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.io.IOException;

public class ClientMessageHandler extends Task {
    
    private ClientMessageQueue messageQueue;
    private Client client;
    boolean stay;
    public ClientMessageHandler(ClientMessageQueue messageQueue, Client client) {
        this.messageQueue = messageQueue;
        this.client = client;
        stay = true;
    }
    
    @Override
    protected Object call() throws Exception {
        
        while (stay) {
            String message = null;
            while (message == null) {
                if (!messageQueue.isEmpty())
                    message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
            }
            System.out.println("Z messageq: " + message);
            System.out.println("Wszedlem do game message handlera");
            JSONObject msg = new JSONObject(message);
            String cmd = msg.getString("cmd");
    
            if (cmd != null) {
        
                if (cmd.equals("join")) {
                    if (msg.getString("status").equals("OK")) {
                        client.setMyId(msg.getInt("id"));
                        System.out.println("Gramy, ID: " + client.getID()); //TODO Ready
    
                        Platform.runLater(() -> {
                            try {
                                client.startGame();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }); //TODO tylko do testow
                        System.out.println("DUPA44");
                        stay = false;
                    } else if (msg.getString("status").equals("start")) {
    
                    } else {
                        System.out.println("Nie udalo sie polaczyc z serverem");
                    }
                }
        
            }
        }
        return null;
    }
}
