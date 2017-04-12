package com.server;

import java.net.InetAddress;

public class ClientData {

    @Override
    public String toString() {
        return "ClientData [IPaddr=" + IPaddr + ", id=" + id + "]";
    }

    private InetAddress IPaddr;
    private int id;
    private int port;
    private boolean ready;

    public ClientData(InetAddress IP, int port, int id) {
        this.IPaddr = IP;
        this.id = id;
        this.port = port;
        ready = false;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getIPaddr() {
        return IPaddr;
    }

    public void setIPaddr(InetAddress iPaddr) {
        IPaddr = iPaddr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReady() {
        return ready;
    }

    public void changeReadyStatus() {
        ready = !ready;
    }
}