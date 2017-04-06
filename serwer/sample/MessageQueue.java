package sample;

import java.util.LinkedList;


public class MessageQueue {

    private LinkedList<String> messageQueue;

    public MessageQueue() {
        messageQueue = new LinkedList<String>();
    }

    public synchronized void add(String command) {  //TODO notify();
        messageQueue.add(command);
    }
    public synchronized String pop() {
        return messageQueue.pop();
    }
    public synchronized boolean isEmpty() {return messageQueue.isEmpty();}
}