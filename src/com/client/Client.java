package com.client;

import com.client.exceptions.PlayersColorNullException;
import com.client.exceptions.PlayersNameNullException;
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
    
    private String playersName;
    private String playersColor;
    
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

    public void wannaJoin(String serverIP, String serverPort) throws UnknownHostException {
        this.serverIP = InetAddress.getByName(serverIP);
        this.serverPort = Integer.parseInt(serverPort);
        
        JSONObject msg = new JSONObject();
        msg.put("cmd", "join");
        send(msg.toString());
    }

    public void setMyId(int id) { this.myId = id; }
    public int getID() { return myId; }

    public void setPlayersColor(String playersColor) {
        this.playersColor = playersColor;
    }
    
    public void setPlayersName(String playersName) {
        this.playersName = playersName;
    }
    
    public void isReadyToJoin() throws PlayersNameNullException, PlayersColorNullException{
        if (playersName == null)
            throw new PlayersNameNullException(myId);
        
        if (playersColor == null)
            throw new PlayersColorNullException(myId);
    }
}

