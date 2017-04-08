package com.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Created by rados on 02.04.2017.
 */
public class Broadcaster {
//    private DatagramSocket serverSocket;  // Socket to send/receive messages on
//
//
//    public Broadcaster(DatagramSocket serverSocket) {
//        this.serverSocket = serverSocket;
//    }

    public static void broadcastMessage(ArrayList<ClientData> clients, String message, DatagramSocket serverSocket) {

        for (ClientData client : clients) {
            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, client.getIPaddr(), client.getPort());
            try {
                serverSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static void msgToOne(ClientData client, String message, DatagramSocket serverSocket) {

            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, client.getIPaddr(), client.getPort());
            try {
                serverSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
    }
}