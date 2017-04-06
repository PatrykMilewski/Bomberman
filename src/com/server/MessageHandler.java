package com.server;

import javafx.concurrent.Task;
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

    public MessageHandler(MessageQueue messageQueue, GUIController msgController, Broadcaster broadcaster) {
        clients = new ArrayList<ClientData>();
        this.messageQueue = messageQueue;
        this.serverMessageController = msgController;
        this.msgSender = broadcaster;
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            DatagramPacket codedMessage = null;         //TODO numerowanie pakietow
            while (codedMessage == null) {
                codedMessage = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
            }

            String message = new String(codedMessage.getData());
            System.out.println(message);

            JSONObject msg = null;
            try {

                msg = new JSONObject(message);

            } catch (Exception e) {
                serverMessageController.sendMessage("Zle zformatowany JSON");
            }

            String cmd = null;
            try {
                if (msg != null) {
                    cmd = msg.getString("cmd");
                }
            } catch (Exception e) {
                serverMessageController.sendMessage("Potrzeba frazy cmd");
            }

            if (cmd != null) {
                int ID = 0;

                if (cmd.equals("join")) {
                    JSONObject answer = new JSONObject();
                    answer.put("cmd", "join");
                    ClientData newClient = new ClientData(codedMessage.getAddress(), codedMessage.getPort(), ID);

                    /*Jeśli są miejsca to dodaj śmiecia do gry, przydziel ID i wyślij odpowiedz OK*/
                    if (clients.size() < ServerConsts.MAX_NUMBER_OF_PLAYERS) {
                        ID = 1 + clients.size();
                        clients.add(newClient);
                        answer.put("status", "OK");
                        answer.put("id", ID);
                        serverMessageController.sendMessage("Dolacza: " + newClient.getIPaddr());
                    } /*Jak nie ma miejsc to przeslij odpowiedz WYKURWIAJ*/ else
                        answer.put("status", "FUCK_OFF");
                    msgSender.msgToOne(newClient, answer.toString());
                } else if (cmd.equals("key")) //TODO "Jesli gra sie rozpoczela"
                {
                    ID = msg.getInt("id");
                    String key = msg.getString("but");
                    if (key.equals("UP")) {
                        //TODO IF (moze sie poruszyc):
                        msgSender.broadcastMessage(clients, "chuj"); // i tutaj trzeba poodeslac wiadomosc co sie odjebalo
                        serverMessageController.sendMessage("ID: " + ID + " Ruszyc w gore? na luzie");
                    }
                    if (key.equals("DOWN")) {
                        serverMessageController.sendMessage("ID: " + ID + " Ruszyc w dol? oki");
                    }
                    if (key.equals("LEFT")) {
                        serverMessageController.sendMessage("ID: " + ID + " Ruszyc w lewo? Wypad śmieciu");
                    }
                    if (key.equals("RIGHT")) {
                        serverMessageController.sendMessage("ID: " + ID + " Ruszyc w prawo? no problemo");
                    }
                }
            }

        }
    }

}
