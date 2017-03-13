package com.bomberman;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SyncFailedException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public Client(int port) {
        InputStreamReader in = new InputStreamReader(System.in);
        Scanner keybord = new Scanner(System.in);
        try {
            Socket clientSocket = new Socket("localhost", port);
            System.out.print("Polaczylem sie do:\t" + clientSocket.getInetAddress());

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            String text = keybord.next();
            while (!text.equals("q")){
                out.writeUTF(text);
                text = keybord.next();
            }
        } catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}
