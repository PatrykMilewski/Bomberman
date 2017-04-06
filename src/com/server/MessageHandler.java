package com.server;

import com.server.Controllers.LogicController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import com.server.Controllers.ServerConsts;
import com.server.Controllers.GUIController;

import java.net.DatagramPacket;
import java.util.ArrayList;

public class MessageHandler extends Task {

    private ArrayList<ClientData> clients;
    private MessageQueue messageQueue;
    private GUIController serverMessageController;
    private Broadcaster msgSender;
    private LogicController logicController;

    public MessageHandler(MessageQueue messageQueue, GUIController msgController, Broadcaster broadcaster) {
        clients = new ArrayList<ClientData>();
        this.messageQueue = messageQueue;
        this.serverMessageController = msgController;
        this.msgSender = broadcaster;
        this.logicController = new LogicController();
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

    private void decodeCmd(String cmd, DatagramPacket codedMessage, JSONObject msg){
        int ID = 0;
        JSONArray answer = new JSONArray();
        JSONObject subAnswer = new JSONObject();
        if (cmd.equals("join")) {
            subAnswer.put("cmd", "join");
            ClientData newClient = new ClientData(codedMessage.getAddress(), codedMessage.getPort(), ID);

            /*Jeśli są miejsca to dodaj śmiecia do gry, przydziel ID i wyślij odpowiedz OK*/
            if (clients.size() < ServerConsts.MAX_NUMBER_OF_PLAYERS) {
                ID = clients.size();
                clients.add(newClient);
                logicController.createPlayer(ID, "Test");
                subAnswer.put("status", "OK");
                subAnswer.put("id", ID);
                Platform.runLater(() -> serverMessageController.sendMessage("Dolacza: " + newClient.getIPaddr()));
                if (clients.size() == ServerConsts.MAX_NUMBER_OF_PLAYERS){
                    JSONObject answerToStart = new JSONObject();
                    answerToStart.put("status", "start");
                    answerToStart.put("cmd", "join");
                    msgSender.broadcastMessage(clients, answerToStart.toString());
                }
            } else {
                subAnswer.put("status", "FUCK_OFF");
            }
            answer.put(subAnswer);
            msgSender.msgToOne(newClient, subAnswer.toString());
        } else if (cmd.equals("key")) {         //TODO "Jesli gra sie rozpoczela"
            ID = msg.getInt("id");
            String key = msg.getString("but");
            int finalID = ID;

            if (logicController.getPlayer(ID).isAlive()){
                if (key.equals("BOMB")){
                    logicController.dropBomb(ID, answer);
                } else if(logicController.incCoords(finalID, key, answer)){
                    answer.put(subAnswer.put("cmd", "move"));
                    Platform.runLater(() -> serverMessageController.sendMessage(answer.toString()));
                    msgSender.broadcastMessage(clients, answer.toString());
                } else {
                    Platform.runLater(() -> serverMessageController.sendMessage("Brak możliwości ruchu"));
                }
            } else {
                Platform.runLater(() -> serverMessageController.sendMessage("Gracz nie żyje"));
            }
        }
    }

}
