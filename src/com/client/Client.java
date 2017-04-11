package com.client;

import com.client.gui.ClientMainStage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    private DatagramSocket socket;
    private InetAddress serverIP;
    private ClientMessageQueue messages;
    private int serverPort;
    private int myId;
    private ClientMainStage mainStage;

    public Client(ClientMainStage mainStage) throws IOException, InterruptedException {
        this.socket = new DatagramSocket();
        this.messages = new ClientMessageQueue();
        this.mainStage = mainStage;
        executor.submit(new Client_receiver(messages,socket));
        executor.submit(new ClientMessageHandler(messages, this));
        myId = 0;
    }
    
    public void startGame() throws IOException, InterruptedException {
        mainStage.mainStageController.startNewGame();
        ClientMap map = new ClientMap(mainStage);
        executor.submit(new GameMessageHandler(messages, map));
        ClientListener playerListener = new ClientListener(mainStage, this);
        playerListener.listen();    //TODO Listen w nowym watku?
    }
    private void send(String message) {
        System.out.println(message);
        DatagramPacket data = new DatagramPacket(message.getBytes(), message.length(), serverIP, serverPort);
        try {
            System.out.println(data.getData());
            socket.send(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKey(String which) {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "key");
        msg.put("but", which);
        msg.put("id", myId);
        System.out.println(msg.toString());
        send(msg.toString());
    }

    public void wannaJoin() {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "join");
        send(msg.toString());
    }
    
    public void setServerAddress(String serverIP, String serverPort) throws UnknownHostException {
        this.serverIP = InetAddress.getByName(serverIP);
        this.serverPort = Integer.parseInt(serverPort);
    }

    public void setMyId(int id){ this.myId = id; }
    public int getID(){ return myId; }
    
    public ClientMainStage getMainStage() {
        return mainStage;
    }
    
    public ClientMessageQueue getMessages() {
        return messages;
    }
}

