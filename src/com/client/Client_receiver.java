package com.client;

import javafx.concurrent.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client_receiver extends Task
{
    private ClientMessageQueue messages;
    private DatagramSocket serverSocket;
    private DatagramPacket data;
    byte[] receivedData;

    Client_receiver(ClientMessageQueue messages, DatagramSocket socket)
    {
        this.messages = messages;
        this.serverSocket = socket;
    }

    @Override
    public Object call()
    {
        while(true) {
            receivedData = new byte[1024];
            data = new DatagramPacket(receivedData, receivedData.length);
            try {
                serverSocket.receive(data);
                System.out.println("przyszlo (dodaje do kułeułe): " + new String(data.getData()));

            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (messages) {
                messages.add(new String((data.getData())));
                messages.notify();
            }
        }
    }
}
