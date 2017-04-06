package com.server.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Uzytkownik on 27.03.2017.
 */
public class GUIController implements Initializable
{
    ExecutorService executor = Executors.newSingleThreadExecutor();
    @FXML ListView serverListView;
    @FXML Label serverIP;

    private final ObservableList<String> data=
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String ajpi = InetAddress.getLocalHost().getHostAddress();
            serverIP.setText("Server IP: "+ ajpi);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg){
        data.add(msg);
        serverListView.setItems(data);
    }

    public void go() throws IOException {
        Task startServer = new GameController(this);
        executor.submit(startServer);
    }
}
