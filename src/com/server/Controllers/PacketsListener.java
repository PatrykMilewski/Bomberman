package com.server.Controllers;

import javafx.concurrent.Task;
import com.server.Broadcaster;
import com.server.MessageHandler;
import com.server.MessageQueue;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketsListener extends Task{
    public final static int PORT = 7115;
    private final static int BUFFER = 2048;
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private GUIController controller;
    private DatagramSocket socket;
    private MessageQueue messages;
    private byte[] buf;

    public PacketsListener(GUIController controller) throws IOException {
        this.controller=controller;
        this.socket = new DatagramSocket(PORT);
        this.messages= new MessageQueue();
        buf = new byte[BUFFER];
        executor.submit(new MessageHandler(messages, controller, socket));
    }

    @Override
    protected Object call() throws Exception {
        controller.sendMessage("Serwer rozpoczal dzialanie");

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                buf = new byte[BUFFER];
                messages.add(packet);
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}