package com.bomberman.gui.lobby;

import java.net.InetAddress;

public class LobbyPlayer {
    public LobbyPlayer(String name, boolean isAdmin) {
        this.name = name;
        this.isAdmin = isAdmin;
        actualisePing();
    }

    public void actualisePing() {
        //ServerInstance.getPing(this.IPAddress);
        //todo
    }

    public String getName() { return name; }
    public int getPing() { return ping; }
    public boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    private String name = "Undefined";
    private int ping = -1;
    private boolean isAdmin;
    private InetAddress IPAddress;
}
