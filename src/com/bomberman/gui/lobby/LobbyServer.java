package com.bomberman.gui.lobby;


import java.net.InetAddress;

public class LobbyServer {
    public LobbyServer(InetAddress IPAddress, int serverID) {
        this.IPAddress = IPAddress;
        this.serverID = serverID;
    }

    public InetAddress getIPAddress() { return IPAddress; }
    public void setIPAddress(InetAddress IPAddress) { this.IPAddress = IPAddress; }
    public int getServerID() { return serverID; }

    private InetAddress IPAddress;
    private int serverID;
}
