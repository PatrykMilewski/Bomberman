package com.server.Controllers;

import javafx.concurrent.Task;
import com.server.Broadcaster;
import com.server.MessageHandler;
import com.server.MessageQueue;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController extends Task{
    public final static int PORT = 7115;
    private final static int BUFFER = 2048;
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private GUIController controller;
    private DatagramSocket socket;
    private MessageQueue messages;
    private Broadcaster broadcaster;
    private byte[] buf;

    public GameController(GUIController controller) throws IOException {
        this.controller=controller;
        this.socket = new DatagramSocket(PORT);
        this.messages= new MessageQueue();
        this.broadcaster = new Broadcaster(socket);
        buf = new byte[BUFFER];
        executor.submit(new MessageHandler(messages, controller, broadcaster));
    }

    @Override
    protected Object call() throws Exception {
        controller.sendMessage("Serwer rozpoczal dzialanie");

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.println("Czekam na pakiecik");
                socket.receive(packet);
                buf = new byte[BUFFER];
                messages.add(packet);

            /*    executor.submit(() -> {
                    try {
                        handleClient(packet);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }); */

               /* String content = new String(buf, buf.length);

                InetAddress clientAddress = packet.getAddress();
                controller.sendMessage(clientAddress.toString() + " polaczyl sie");
                int clientPort = packet.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                if (!existingClients.contains(id)) {
                    existingClients.add( id );
                    clientPorts.add( clientPort );
                    clientAddresses.add(clientAddress);
                }

                System.out.println(id + " : " + content);
                byte[] data = (id + " : " +  content).getBytes();
                for (int i=0; i < clientAddresses.size(); i++) {
                    // InetAddress cl = clientAddresses.get(i);
                    // int cp = clientPorts.get(i);
                    //  packet = new DatagramPacket(data, data.length, cl, cp);
                    //  socket.send(packet);
                }*/

            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }

    private void handleClient(DatagramPacket packer) throws InterruptedException {
        while (true){
        }
    }
}