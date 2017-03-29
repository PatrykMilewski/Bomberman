package com.bomberman.fields;

import com.bomberman.gui.Consts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bonus extends Field
{
    private String type;
    private static final Map<Integer, String> superPowers = createMapOfSuperPowers();
    private static final int nSuperPowers = superPowers.size();

    private static Map<Integer,String> createMapOfSuperPowers() {
        Map<Integer, String> tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "haste");
        tempMap.put(1, "incrange");
        tempMap.put(2, "incbombs");
        return tempMap;
    }

    public Bonus(int x, int y) {
        super(x, y, true);
        this.empty = true;

        generateBonus();
    }

    private void generateBonus() {
        Random generator = new Random();
        this.type = superPowers.get(generator.nextInt(nSuperPowers));
        this.imagePath = "images/Bonuses/" + this.type + ".png";
    }

    public void takeBonus(Player player) {
        if (type.equals("incrange")){
            player.incRange();
        } else if (type.equals("haste")){
            player.incSpeed();
        } else if (type.equals("incbombs")){
            player.incNBombs();
        }
    }
}
