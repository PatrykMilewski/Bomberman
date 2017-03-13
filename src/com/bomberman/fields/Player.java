package com.bomberman.fields;

import java.util.Map;
import java.util.HashMap;

public class Player extends Field
{
    private String name;
    private int index;
    private float speed;
    private Map<SuperPowers, Boolean> superPowers = new HashMap<SuperPowers, Boolean>();

    public Player(int x, int y, boolean destroyable, String name, int index) {
        super(x, y, destroyable);
        superPowers.put(SuperPowers.invisible, false);
        superPowers.put(SuperPowers.faster, false);
        superPowers.put(SuperPowers.terminator, false);
    }

    public boolean getSuperPower(String s)
    {
        return superPowers.get(s);
    }

    public float getSpeed() { return speed; }
    public int getIndex() {return index; }
    public String getName() { return name; }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void setSuperPowers(SuperPowers superPower, Boolean trig)
    {
        superPowers.put(superPower, trig);
    }

}
