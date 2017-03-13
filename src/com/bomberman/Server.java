package com.bomberman;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    private int port;

    public Server(int port) {
        this.port = port;
        listen(this.port);
    }

    public void listen(int port){
        System.out.println("Server został uruchomiony");
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                Socket client = serverSocket.accept();
                executor.submit(() -> handleClient(client));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void handleClient(Socket socket){
        try {
            System.out.println("Polaczylem sie z klientem z:\t" + socket.getInetAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream());
            String text = in.readUTF();

            while (!text.equals("q")){
                text = in.readUTF();
                System.out.println(text);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String []args){
        Server server = new Server(5000);
    }
}
