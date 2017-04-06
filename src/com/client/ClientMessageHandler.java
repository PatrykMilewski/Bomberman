package com.client;

import javafx.concurrent.Task;
import org.json.JSONObject;
public class ClientMessageHandler extends Task {

    private ClientMessageQueue messageQueue;
    private Client client;

    public ClientMessageHandler(ClientMessageQueue messageQueue, Client client) {
        this.messageQueue = messageQueue;
        this.client = client;
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
