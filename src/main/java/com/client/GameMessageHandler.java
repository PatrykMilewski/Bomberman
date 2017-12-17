package com.client;

import com.elements.loggers.LoggerFactory;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GameMessageHandler extends Task {
    private static Logger log = LoggerFactory.getLogger(GameMessageHandler.class.getCanonicalName());

    private ExecutorService executor = Executors.newFixedThreadPool(3);
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
            
            log.info("Received message from server: " + message);

            JSONObject jObject = new JSONObject(message);
            String cmd = jObject.getString("cmd");
            if (cmd != null) {
                executor.submit(new ClientRodeoPageboyOnMolestedCowWithPurpleSpots(cmd,jObject,client, map));
            }
        }
    }


}
