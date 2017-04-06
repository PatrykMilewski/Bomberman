package sample;

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
    ExecutorService executor = Executors.newFixedThreadPool(2);
    private DatagramSocket socket;
    private InetAddress servIP;
    private MessageQueue messages;
    private int server_port;
    private int myId;

    Client(InetAddress servIP, int server_port) throws SocketException {
        this.socket = new DatagramSocket();
        this.messages = new MessageQueue();
        this.server_port = server_port;
        this.servIP = servIP;
        executor.submit(new Client_receiver(messages,socket));
        executor.submit(new MessageHandler(messages, this));
        myId = 0;
        wannaJoin();
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
}
