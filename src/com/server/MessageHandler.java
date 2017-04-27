package com.server;

import com.server.Controllers.LogicController;
import com.server.Controllers.ServerLobbyController;
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
    private ServerLobbyController serverLobbyController;

    public MessageHandler(MessageQueue messageQueue, GUIController msgController, DatagramSocket socket) {
        clients = new ArrayList<ClientData>();
        this.messageQueue = messageQueue;
        this.serverMessageController = msgController;
        this.socket = socket;
        this.logicController = new LogicController(socket, clients);
        this.serverLobbyController = new ServerLobbyController(socket, clients);
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
                cmd = msg.getString("cmd");
            } catch (Exception e) {
                Platform.runLater(() -> serverMessageController.sendMessage("Potrzeba frazy cmd"));
            }

            decodeCmd(cmd, codedMessage, msg);
        }
    }

    private void decodeCmd(String cmd, DatagramPacket codedMessage, JSONObject msg) throws InterruptedException {
        if (cmd.equals("join")) {
            cmdJoin(codedMessage);
        } else if (cmd.equals("ready")){
            cmdReady(msg);
        } else if (cmd.equals("updateSlots")){
            cmdUpdateSlots(msg);
        } else if (cmd.equals("key")) {
            cmdKey(msg);
        }
    }

    private void cmdJoin( DatagramPacket codedMessage) throws InterruptedException {
        ClientData newClient = new ClientData(codedMessage.getAddress(), codedMessage.getPort(), 0);
        /*Jeśli są miejsca to dodaj gracza do gry, przydziel ID i wyślij odpowiedz OK*/
        if (clients.size() < ServerConsts.MAX_NUMBER_OF_PLAYERS) {
            handleClient(newClient);
        } else {
            rejectClient(newClient);
        }
    }

    private void handleClient(ClientData newClient){
        JSONObject answerToSend = new JSONObject();
        int clientId = clients.size();
        newClient.setId(clientId);
        clients.add(newClient);
        answerToSend.put("status", "OK");
        answerToSend.put("id", clientId);
        Broadcaster.msgToOne(newClient, answerToSend.toString(), socket);
        sendSlots(newClient);
        Platform.runLater(() -> serverMessageController.sendMessage("Dolacza: " + newClient.getIPaddr()));
    }

    private void sendSlots(ClientData newClient) {
        serverLobbyController.sendSlotsToClient(newClient);
    }

    private void rejectClient(ClientData newClient){
        JSONObject answerToSend = new JSONObject();
        answerToSend.put("status", "ACCESS_DENIED");
        Broadcaster.msgToOne(newClient, answerToSend.toString(), socket);
        Platform.runLater(() -> serverMessageController.sendMessage("Odrzucilem klienta: " + newClient.getIPaddr()));
    }

    private void cmdReady(JSONObject msg) throws InterruptedException {
        int clientId = msg.getInt("id");
        clients.get(clientId).changeReadyStatus();
        for (ClientData client : clients){              //Jest tylu, ilu się połączyło
            if (!client.isReady()){
                return;
            }
        }
        //Jesli wszyscy klienci sa gotowi, to zaczynamy gre
        startGame();
    }

    private void startGame() throws InterruptedException {
        JSONObject answerToStart = new JSONObject();
        answerToStart.put("status", "start");
        Broadcaster.broadcastMessage(clients, answerToStart.toString(), socket);
        JSONObject answerToPrint = new JSONObject();

        logicController.fillMap();
        logicController.createPlayers(clients, "Test");

        answerToPrint.put("cmd", "eMap");
        answerToPrint.put("fields", logicController.printEntireMap());
        Broadcaster.broadcastMessage(clients, answerToPrint.toString(), socket);
        Platform.runLater(() -> serverMessageController.sendMessage("Start gry"));
    }

    private void cmdUpdateSlots(JSONObject msg) {
        int newIdSlot = msg.getInt("newSlotId");
        int oldIdSlot = msg.getInt("oldSlotId");
        int clientId = msg.getInt("clientId");
        String textOnSlot = msg.getString("text");
        serverLobbyController.changeSlot(newIdSlot, oldIdSlot, textOnSlot, clientId);
    }

    private void cmdKey(JSONObject msg) {
        int clientId = msg.getInt("id");
        if (logicController.getPlayer(clientId).isAlive()){
            String key = msg.getString("but");
            JSONObject answerToSend = new JSONObject();
            JSONArray arrayToSend = new JSONArray();
            answerToSend.put("cmd", "move");
            if (key.equals("BOMB")){
                keyBomb(clientId, answerToSend, arrayToSend);
            } else {                                                //key == UP || RIGT || DOWN || LEFT
                keyMakeMove(clientId, key, answerToSend, arrayToSend);
            }
        } else {
            Platform.runLater(() -> serverMessageController.sendMessage("Gracz nie żyje"));
        }
    }

    private void keyBomb(int clientId, JSONObject answerToSend, JSONArray arrayToSend){
        logicController.dropBomb(clientId, arrayToSend);
        Platform.runLater(() -> serverMessageController.sendMessage(arrayToSend.toString()));
        answerToSend.put("fields", arrayToSend);
        Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
    }

    private void keyMakeMove(int clientId, String key, JSONObject answerToSend, JSONArray arrayToSend){
        if(logicController.incCoords(clientId, key, arrayToSend)){
            Platform.runLater(() -> serverMessageController.sendMessage(arrayToSend.toString()));
            answerToSend.put("fields", arrayToSend);
            Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
        } else {
            Platform.runLater(() -> serverMessageController.sendMessage("Brak możliwości ruchu"));
        }
    }
}
