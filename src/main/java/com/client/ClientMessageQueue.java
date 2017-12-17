package com.client;

import com.elements.interfaces.Callback;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ClientMessageQueue {
    
    private Queue<String> messageQueue;
    private Set<Callback> callbacks = new HashSet<>();
    
    ClientMessageQueue() {
        messageQueue = new LinkedList<>();
    }
    
    synchronized void add(String command) {
        messageQueue.add(command);
        callbacks.forEach(Callback::callback);
    }
    
    void addCallback(Callback callback) {
        this.callbacks.add(callback);
    }

    synchronized String pop() {
        return messageQueue.remove();
    }
    
    public synchronized boolean isEmpty() {
        return messageQueue.isEmpty();
    }
}