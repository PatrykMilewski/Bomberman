package com.client;

import com.client.exceptions.PlayersColorNullException;
import com.client.exceptions.PlayersNameNullException;
import com.client.gui.ClientConsts;
import com.client.gui.ClientMainStage;
import com.client.gui.interfaceControllers.LobbyController;
import com.elements.loggers.LoggerFactory;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class.getCanonicalName());
    
    private static Random generator = new Random();
    
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    private DatagramSocket socket;
    private InetAddress serverIP;
    private ClientMessageQueue messagesQueue;
    private ClientMainStage mainStage;
    private int serverPort, myId, slotId, playersTimeBetweenMoves;
    
    private String playersName, playersColor;
    
    public Client(ClientMainStage mainStage, LobbyController lobbyController) throws IOException, InterruptedException {
        this.socket = new DatagramSocket();
        this.messagesQueue = new ClientMessageQueue();
        this.mainStage = mainStage;
        ClientReceiver clientReceiver = new ClientReceiver(messagesQueue, socket);
        ClientMessageHandler clientMessageHandler = new ClientMessageHandler(messagesQueue, this, lobbyController);
        messagesQueue.addCallback(clientMessageHandler);
        executor.submit(clientReceiver);
        executor.submit(clientMessageHandler);
        slotId = -1;
        myId = 0;
        playersColor = Integer.toHexString(generator.nextInt(16581375 + 1));
        playersTimeBetweenMoves = ClientConsts.TIME_BETWEEN_MOVES;
    }
    
    void startGame() throws IOException, InterruptedException {
        ClientMainStage.mainStageController.startNewGame();
        ClientMap map = new ClientMap(mainStage);
        executor.submit(new GameMessageHandler(messagesQueue, map, this));
        ClientListener playerListener = new ClientListener(mainStage, this);
        playerListener.listen();    //TODO Listen w nowym watku?
    }

    public void send(String message) {
        log.info(message);
        DatagramPacket data = new DatagramPacket(message.getBytes(), message.length(), serverIP, serverPort);
        try {
            log.info(Arrays.toString(data.getData()));
            socket.send(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSlot(int oldSlotId, int newSlotId, String textOnLabel) {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "updateSlots");
        msg.put("text", textOnLabel);
        msg.put("newSlotId", Integer.toString(newSlotId));
        msg.put("oldSlotId", Integer.toString(oldSlotId));
        msg.put("clientId", Integer.toString(myId));
        log.info("Sending a new game slot.");
        send(msg.toString());
    }

    public void sendQuitGameMessage() //todo
    {
/*        JSONObject msg = new JSONObject();
        msg.put("cmd", "quit");
        msg.put("id",Integer.toString(myId));
        send(msg.toString());*/
    }


    void sendKey(String which) {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "key");
        msg.put("but", which);
        msg.put("id", myId);
        send(msg.toString());
    }

    public void wannaJoin(String serverIP, String serverPort) throws UnknownHostException {
        this.serverIP = InetAddress.getByName(serverIP);
        this.serverPort = Integer.parseInt(serverPort);
        JSONObject msg = new JSONObject();
        msg.put("cmd", "join");
        send(msg.toString());
    }

    void setMyId(int id) {
        this.myId = id;
    }

    public int getID() {
        return myId;
    }

    void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setPlayersColor(String playersColor) {
        this.playersColor = playersColor;
    }
    
    public String getPlayersColor() {
        return this.playersColor;
    }

    int getPlayersTimeBetweenMoves() {
        return playersTimeBetweenMoves;
    }

    void setPlayersTimeBetweenMoves(int playersTimeBetweenMoves) {
        this.playersTimeBetweenMoves = playersTimeBetweenMoves;
    }
    
    public void setPlayersName(String playersName) {
        this.playersName = playersName;
    }
    
    public void isReadyToJoin() throws PlayersNameNullException, PlayersColorNullException {
        if (playersName == null)
            throw new PlayersNameNullException(myId);
        
        if (playersColor == null)
            throw new PlayersColorNullException(myId);
    }
    
    public boolean isNameSet() {
        return playersName != null;
    }
    
    void newGameScore(int playersId, String playersName) {
        Platform.runLater(() -> ClientMainStage.gameController.initializeScoreLabel(playersId, playersName));
    }
    
    void setGameScore(int playersId, int newScore) {
        Platform.runLater(() -> ClientMainStage.gameController.setScoreLabel(playersId, newScore));
    }

    public void updateHighScores(JSONArray highScores) {
        Platform.runLater(() -> ClientMainStage.highscoresController.setScores(highScores));
    }
}
