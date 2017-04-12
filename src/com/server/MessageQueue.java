package com.server;
import java.net.DatagramPacket;
import java.util.LinkedList;

public class MessageQueue {
    private LinkedList<DatagramPacket> messageQueue;

    public MessageQueue() {messageQueue = new LinkedList<DatagramPacket>();}

    public synchronized void add(DatagramPacket message) {
        messageQueue.add(message);
        notify(); // TODO all?
    }
    public synchronized DatagramPacket pop() throws InterruptedException {

        while(messageQueue.isEmpty()){
            wait(1000);                     //TODO ???? cóż tutaj Radku się dzieje
            if(messageQueue.isEmpty()){
                return(null);
            }
        }
        return messageQueue.pop();
    }
}