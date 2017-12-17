package com.server;

import com.elements.loggers.LoggerFactory;
import com.server.controllers.LogicController;
import com.server.controllers.ServerLobbyController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONObject;
import com.server.controllers.GUIController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MessageHandler extends Task {
    
    private static Logger log = LoggerFactory.getLogger(MessageHandler.class.getCanonicalName());
    
    private ArrayList<ClientData> clients;
    private MessageQueue messageQueue;
    private GUIController serverMessageController;
    private LogicController logicController;
    private ServerLobbyController serverLobbyController;
    private DatagramSocket socket;
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    private Highscores highscores;

    public MessageHandler(MessageQueue messageQueue, GUIController msgController, DatagramSocket socket) throws IOException, ClassNotFoundException {
        clients = new ArrayList<>();
        this.messageQueue = messageQueue;
        this.serverMessageController = msgController;
        this.socket = socket;
        this.logicController = new LogicController(socket, clients);
        this.serverLobbyController = new ServerLobbyController(socket, clients);
        this.highscores = new Highscores();
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            DatagramPacket codedMessage = null;
            while (codedMessage == null) {
                codedMessage = messageQueue.pop();
            }

            String message = new String(codedMessage.getData());
            log.info(message);

            JSONObject msg = null;
            try {
                msg = new JSONObject(message);
            } catch (Exception e) {
                Platform.runLater(() -> serverMessageController.sendMessage("Zle zformatowany JSON"));
            }

            String cmd = null;
            try {
                cmd = msg.getString("cmd");
            } catch (Exception e) {
                Platform.runLater(() -> serverMessageController.sendMessage("Potrzeba frazy cmd"));
            }
            executor.submit(new MessagePageboy(codedMessage, msg, cmd, clients, socket,
                    logicController, serverLobbyController, serverMessageController, highscores));
        }
    }
}