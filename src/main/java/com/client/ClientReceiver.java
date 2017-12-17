package com.client;

import com.elements.loggers.LoggerFactory;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class ClientReceiver extends Task {
    private static Logger log = LoggerFactory.getLogger(ClientReceiver.class.getCanonicalName());
    
    private ClientMessageQueue messages;
    private DatagramSocket serverSocket;
    private DatagramPacket data;
    private byte[] receivedData;
    
    ClientReceiver(ClientMessageQueue messages, DatagramSocket socket) {
        this.messages = messages;
        this.serverSocket = socket;
    }
    
    @Override
    public Object call() {
        while (true) {
            receivedData = new byte[1024];
            data = new DatagramPacket(receivedData, receivedData.length);
            try {
                serverSocket.receive(data);
                log.info("Received data pack (adding to queue): " + new String(data.getData()));
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            messages.add(new String((data.getData())));
        }
    }
}
