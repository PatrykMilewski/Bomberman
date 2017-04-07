package com.client;

import com.client.gui.ClientMainStage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client
{
    ExecutorService executor = Executors.newFixedThreadPool(3);
    private DatagramSocket socket;
    private InetAddress servIP;
    private ClientMessageQueue messages;
    private int server_port;
    private int myId;
    private ClientMainStage mainStage;

    public Client(InetAddress servIP, int server_port, ClientMainStage mainStage) throws IOException, InterruptedException {
        this.socket = new DatagramSocket();
        this.messages = new ClientMessageQueue();
        this.server_port = server_port;
        this.servIP = servIP;
        this.mainStage = mainStage;
        executor.submit(new Client_receiver(messages,socket));
        executor.submit(new ClientMessageHandler(messages, this));
        myId = 0;
        wannaJoin();
    }
    
    public void startGame() throws IOException, InterruptedException {
        mainStage.mainStageController.startNewGame();
        System.out.println("Siema1");
        ClientMap map = new ClientMap(mainStage);
        map.printEntireMap();
        executor.submit(new GameMessageHandler(messages, map));
        System.out.println("Siema3");
        ClientListener playerListener = new ClientListener(mainStage, this);
        playerListener.listen();    //TODO Listen w nowym watku?
    }
    private void send(String message) {
        System.out.println(message);
        DatagramPacket data = new DatagramPacket(message.getBytes(), message.length(), servIP, server_port);
        try {
            System.out.println(data.getData());
            socket.send(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKey(String which)
    {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "key");
        msg.put("but", which);
        msg.put("id", myId);
        System.out.println(msg.toString());
        send(msg.toString());
    }

    public void wannaJoin()
    {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "join");
        send(msg.toString());
    }

    public void setMyId(int id){this.myId = id;}
    public int getID(){return myId;}
    public ClientMainStage getMainStage() {
        return mainStage;
    }
    public ClientMessageQueue getMessages() {
        return messages;}
}

