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
    private Broadcaster msgSender;
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
        int ID = 0;
        JSONArray answer = new JSONArray();
        JSONObject subAnswer = new JSONObject();
        JSONObject answerToSend = new JSONObject();

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
                msgSender.msgToOne(newClient, subAnswer.toString(), socket);
                Platform.runLater(() -> serverMessageController.sendMessage("Dolacza: " + newClient.getIPaddr()));
                if (clients.size() == ServerConsts.MAX_NUMBER_OF_PLAYERS){
                    Thread.sleep(1000);
                    JSONObject answerToStart = new JSONObject();
                    answerToStart.put("status", "start");
                    answerToStart.put("cmd", "join");
                    msgSender.broadcastMessage(clients, answerToStart.toString(), socket);
                    JSONObject answerToPrint = new JSONObject();
                    answerToPrint.put("cmd", "eMap");
                    answerToPrint.put("fields", logicController.printEntireMap());
                    msgSender.broadcastMessage(clients, answerToPrint.toString(), socket);
                }
            } else {
                subAnswer.put("status", "FUCK_OFF");
                msgSender.msgToOne(newClient, subAnswer.toString(), socket);
            }
        } else if (cmd.equals("key")) {         //TODO "Jesli gra sie rozpoczela"
            ID = msg.getInt("id");
            String key = msg.getString("but");
            int finalID = ID;



            if (logicController.getPlayer(ID).isAlive()){
                answerToSend.put("cmd", "move");
                JSONArray arrayJson = new JSONArray();

                answer.put(subAnswer.put("cmd", "change"));
                if (key.equals("BOMB")){
                    logicController.dropBomb(ID, arrayJson);
                } else if(logicController.incCoords(finalID, key, arrayJson)){
//                    Platform.runLater(() -> serverMessageController.sendMessage(answer.toString()));
                    answerToSend.put("fields", arrayJson);
                    System.out.println(answerToSend.toString());
                    msgSender.broadcastMessage(clients, answerToSend.toString(), socket);
                } else {
                    Platform.runLater(() -> serverMessageController.sendMessage("Brak możliwości ruchu"));
                }
            } else {
                Platform.runLater(() -> serverMessageController.sendMessage("Gracz nie żyje"));
            }
        }
    }
}
