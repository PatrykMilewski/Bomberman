package com.client.gui.interfaceControllers;

import java.io.Serializable;
import java.util.Comparator;

class SingleScore implements Serializable {
    private String playersName;
    private int score;
    public SingleScore() {
        this.playersName = "Not set yet!";
        this.score = 0;
    }
    
    void set(String playersName, int score) {
        this.playersName = playersName;
        this.score = score;
    }
    
    String get() {
        return (playersName + " - " + score);
    }
    
    boolean compareTo(SingleScore input) {
        return this.score < input.score;
    }
    
    
}