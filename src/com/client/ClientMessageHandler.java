package com.client;

import com.bomberman.gui.GameMap;
import javafx.concurrent.Task;
import org.json.JSONObject;
public class ClientMessageHandler extends Task {

    private ClientMessageQueue messageQueue;
    private Client client;
    private ClientMap map;

    public ClientMessageHandler(ClientMessageQueue messageQueue, Client client, ClientMap map) {
        this.messageQueue = messageQueue;
        this.client = client;
        this.map = map;
    }

    @Override
    protected Object call() throws Exception {
        while(true)
        {
            String message = null;
            while (message == null) {
                if(!messageQueue.isEmpty())
                    message = messageQueue.pop(); //TODO "jezeli gra nadal trwa", pobierane z Game.
            }
            System.out.println("Z messageq: "+ message);

            JSONObject msg = new JSONObject(message);
            String cmd = msg.getString("cmd");;
            if(cmd !=null)
            {
                if(cmd.equals("join"))
                {
                    if(msg.getString("status").equals("OK"))
                    {
                        client.setMyId(msg.getInt("id"));
                        System.out.println("Gramy, ID: " + client.getID() ); //TODO logika dodania tego ziomala do gry
                    }
                    else
                    {
                        System.out.println("Nie udalo sie polaczyc z serverem");
                    }
                }

                else if(cmd.equals("key")) //TODO "Jesli gra sie rozpoczela"
                {

                }
            }

        }
    }

}
