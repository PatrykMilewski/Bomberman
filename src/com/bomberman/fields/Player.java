package com.bomberman.fields;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by rados on 13.03.2017.
 */
public class Player extends Field
{
    private String name;
    private int index;
    private float speed;
    private Map<String, Boolean> superPowers = new HashMap<String, Boolean>();

    public Player(int x, int y, String name, int index) {
        super(x, y);
        superPowers.put("Niewidzialny", false);
        superPowers.put("Przypieszony", false);
        superPowers.put("Terminator", false);
    }

    public boolean getSuperPower(String s)
    {
        return superPowers.get(s);
    }

    public float getSpeed()
    {
        return speed;
    }
    public int index()
    {
        return index;
    }
    public String getName() {
        return name;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void setSuperPowers(String s, Boolean trig)
    {
        superPowers.put(s, trig);
    }

}
