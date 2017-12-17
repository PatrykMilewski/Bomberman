package com.server;

import com.elements.loggers.LoggerFactory;
import com.server.controllers.LogicController;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class WholeMapSender extends Task {
    
    private static Logger log = LoggerFactory.getLogger(WholeMapSender.class.getCanonicalName());
    
    private LogicController logicController;
    private DatagramSocket socket;
    private ArrayList<ClientData> clients;

    WholeMapSender(LogicController logicController, DatagramSocket socket, ArrayList<ClientData> clients){
        this.logicController = logicController;
        this.socket = socket;
        this.clients = clients;
    }

    Boolean gameOn = true; //TODO jezeli gra nadal trwa
    int iterator =0;
    @Override
    protected Object call() {
        while(gameOn) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            
            iterator++;
            JSONObject msg = new JSONObject();
            msg.put("cmd", "eMap");
            msg.put("fields", logicController.printEntireMap());
            log.info("Sending the whole map " + iterator);
            Broadcaster.broadcastMessage(clients, msg.toString(), socket);
        }
        return null;
    }
}
