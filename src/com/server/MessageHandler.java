package com.server;

import com.server.Controllers.LogicController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import com.server.Controllers.ServerConsts;
import com.server.Controllers.GUIController;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class MessageHandler extends Task {

    private ArrayList<ClientData> clients;
    private MessageQueue messageQueue;
    private GUIController serverMessageController;
    private LogicController logicController;
    private DatagramSocket socket;

    public MessageHandler(MessageQueue messageQueue, GUIController msgController, DatagramSocket socket) {
        clients = new ArrayList<ClientData>();
        this.messageQueue = messageQueue;
        this.serverMessageController = msgController;
        this.socket = socket;
        this.logicController = new LogicController(socket, clients);
        logicController.fillMap();
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            DatagramPacket codedMessage = null;         //TODO numerowanie pakietow
            while (codedMessage == null) {
                codedMessage = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.        mniejszy delay?
            }

            String message = new String(codedMessage.getData());
            System.out.println(message);

            JSONObject msg = null;
            try {
                msg = new JSONObject(message);
            } catch (Exception e) {
                Platform.runLater(() -> serverMessageController.sendMessage("Zle zformatowany JSON"));
            }

            String cmd = null;
            try {
                if (msg != null) {
                    cmd = msg.getString("cmd");
                }
            } catch (Exception e) {
                Platform.runLater(() -> serverMessageController.sendMessage("Potrzeba frazy cmd"));
            }
            if (cmd != null) {
                decodeCmd(cmd, codedMessage, msg);
            }
        }
    }

    private void decodeCmd(String cmd, DatagramPacket codedMessage, JSONObject msg) throws InterruptedException {
        if (cmd.equals("join")) {
            cmdJoin(codedMessage);
        } else if (cmd.equals("key")) {         //TODO "Jesli gra sie rozpoczela"
            cmdKey(msg);
        }
    }

    private void cmdJoin( DatagramPacket codedMessage) throws InterruptedException {
        JSONObject answerToSend = new JSONObject();
        answerToSend.put("cmd", "join");
        ClientData newClient = new ClientData(codedMessage.getAddress(), codedMessage.getPort(), 0);        //TODO 0?

        /*Jeśli są miejsca to dodaj śmiecia do gry, przydziel ID i wyślij odpowiedz OK*/
        if (clients.size() < ServerConsts.MAX_NUMBER_OF_PLAYERS) {
            int ID = clients.size();
            clients.add(newClient);
            logicController.createPlayer(ID, "Test");
            answerToSend.put("status", "OK");
            answerToSend.put("id", ID);
            Broadcaster.msgToOne(newClient, answerToSend.toString(), socket);
            Platform.runLater(() -> serverMessageController.sendMessage("Dolacza: " + newClient.getIPaddr()));
            if (clients.size() == ServerConsts.MAX_NUMBER_OF_PLAYERS){
                Thread.sleep(1000);         //TODO Co to?
                JSONObject answerToStart = new JSONObject();
                answerToStart.put("status", "start");
                answerToStart.put("cmd", "join");
                Broadcaster.broadcastMessage(clients, answerToStart.toString(), socket);
                JSONObject answerToPrint = new JSONObject();
                answerToPrint.put("cmd", "eMap");
                answerToPrint.put("fields", logicController.printEntireMap());
                Broadcaster.broadcastMessage(clients, answerToPrint.toString(), socket);
            }
        } else {
            answerToSend.put("status", "FUCK_OFF");
            Broadcaster.msgToOne(newClient, answerToSend.toString(), socket);
        }
    }

    private void cmdKey(JSONObject msg) {
        int ID;
        JSONObject answerToSend = new JSONObject();
        JSONArray arrayToSend = new JSONArray();

        ID = msg.getInt("id");
        String key = msg.getString("but");
        int finalID = ID;

        if (logicController.getPlayer(ID).isAlive()){
            answerToSend.put("cmd", "move");
            if (key.equals("BOMB")){
                logicController.dropBomb(ID, arrayToSend);
                answerToSend.put("fields", arrayToSend);
                Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
            } else if(logicController.incCoords(finalID, key, arrayToSend)){        //key == UP || RIGT || DOWN || LEFT
                Platform.runLater(() -> serverMessageController.sendMessage(arrayToSend.toString()));
                answerToSend.put("fields", arrayToSend);
                Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
            } else {
                Platform.runLater(() -> serverMessageController.sendMessage("Brak możliwości ruchu"));
            }
        } else {
            Platform.runLater(() -> serverMessageController.sendMessage("Gracz nie żyje"));
        }
    }
}
