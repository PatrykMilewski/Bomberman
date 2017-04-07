package com.client;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rados on 06.04.2017.
 */
public class GameMessageHandler extends Task
{
    private ClientMessageQueue messageQueue;
    private ClientMap map;
    //private Client client;
    
    public GameMessageHandler(ClientMessageQueue messageQueue, ClientMap map)
    {
        this.messageQueue = messageQueue;
        this.map = map;
    }
    
    @Override
    protected Object call() throws Exception {
       
            while(true)
            {
                
                String message = null;
                while (message == null) {
                    if (!messageQueue.isEmpty())
                        message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
                }
                System.out.println("Z messageq KLIENTA: " + message);
            
                JSONArray msg = new JSONArray(message);
                //String cmd = msg.getString("cmd");
               // if (cmd != null) {
               
                
             //   }
          
               // Platform.runLater(() -> map.printEntireMap());
            }
    }
}
