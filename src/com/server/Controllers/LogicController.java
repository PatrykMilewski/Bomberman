package com.server.Controllers;

import com.server.Broadcaster;
import com.server.ClientData;
import com.server.Consts;
import com.server.fields.Bomb;
import com.server.fields.Bonus;
import com.server.fields.Field;
import com.server.fields.Fire;
import com.server.fields.NormalBlock;
import com.server.fields.Player;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Szczepan on 06.04.2017.
 */
public class LogicController {
    private ArrayList<ClientData> clients;
    private DatagramSocket socket;
    private Field[][] mapFields;
    private LinkedList<Player> players;
    private ArrayList<Fire> fires;
    private ExecutorService bombExecutors = Executors.newFixedThreadPool(Consts.MAX_N_BOMBS * 3);   //max liczba bomb na mapie * liczba graczy
    private ExecutorService firesExecutor = Executors.newSingleThreadExecutor();
    private final Map<String, Integer> fieldImages = fillHashMap();
    private static int breakLoopFires = 0;

    private static final Map<String, Integer> fillHashMap() {
        Map<String, Integer> tempMap = new HashMap<>();
        tempMap.put("DefaultBlock", 0);
        tempMap.put("DestroyableBlock", 1);
        tempMap.put("Fire", 2);
        tempMap.put("Player", 3);
        tempMap.put("UnDestroyableBlock", 4);
        tempMap.put("Bomb", 5);
        tempMap.put("Bonushaste", 6);
        tempMap.put("Bonusincrange", 7);
        tempMap.put("Bonusincbombs", 8);
        return tempMap;
    }

    public LogicController(DatagramSocket socket, ArrayList<ClientData> clients) {
        this.players = new LinkedList<>();
        this.fires = new ArrayList<>();
        this.socket = socket;
        this.clients = clients;
        firesLoop();
    }

    public Field getMapField(int x, int y) {
        return this.mapFields[y][x];
    }

    public void setMapField(int x, int y, Field field) {
        this.mapFields[y][x] = field;
    }

    public Player getPlayer(int id) {
        return players.get(id);
    }

    public ArrayList<Fire> getFires() {
        return this.fires;
    }

    public void fillMap() {
        this.mapFields = new Field[Consts.DIMENSION][Consts.DIMENSION];
        for (int i = 0; i < Consts.DIMENSION; i++) {
            for (int j = 0; j < Consts.DIMENSION; j++) {
                this.mapFields[i][j] = new NormalBlock(j, i, false, true);          //Bloki puste
            }
        }

        for (int i = 1; i < Consts.DIMENSION; i += 2) {
            for (int j = 1; j < Consts.DIMENSION; j += 2) {
                this.mapFields[i][j] = new NormalBlock(j, i, false, false);       //Blok nie do rozbicia
            }
        }

        Random generator = new Random();
        for (int i = 0; i < Consts.DIMENSION; i++) {
            for (int j = 0; j < Consts.DIMENSION; j++) {
                if (!((i % 2 == 1) && (j % 2 == 1)) && (generator.nextDouble() > Consts.NORMAL_BLOCK_PROB))
                    this.mapFields[i][j] = new NormalBlock(j, i, true, false);     //Blok do rozbicia
            }
        }

        mapFields[0][0] = new NormalBlock(0, 0, false, true);             //pola puste dla graczy
        mapFields[0][1] = new NormalBlock(1, 0, false, true);
        mapFields[1][0] = new NormalBlock(0, 1, false, true);
        mapFields[0][Consts.DIMENSION - 1] = new NormalBlock(Consts.DIMENSION - 1, 0, false, true);
        mapFields[0][Consts.DIMENSION - 2] = new NormalBlock(Consts.DIMENSION - 2, 0, false, true);
        mapFields[1][Consts.DIMENSION - 1] = new NormalBlock(Consts.DIMENSION - 1, 1, false, true);
        mapFields[Consts.DIMENSION - 1][0] = new NormalBlock(0, Consts.DIMENSION - 1, false, true);
        mapFields[Consts.DIMENSION - 2][0] = new NormalBlock(0, Consts.DIMENSION - 2, false, true);
        mapFields[Consts.DIMENSION - 1][1] = new NormalBlock(1, Consts.DIMENSION - 1, false, true);
        mapFields[Consts.DIMENSION - 1][Consts.DIMENSION - 1] = new NormalBlock(Consts.DIMENSION - 1, Consts.DIMENSION - 1, false, true);
        mapFields[Consts.DIMENSION - 2][Consts.DIMENSION - 1] = new NormalBlock(Consts.DIMENSION - 1, Consts.DIMENSION - 2, false, true);
        mapFields[Consts.DIMENSION - 1][Consts.DIMENSION - 2] = new NormalBlock(Consts.DIMENSION - 2, Consts.DIMENSION - 1, false, true);
    }

    public void createPlayer(int ID, String name) {
        if (ID == 0){
            this.mapFields[0][0] = new Player(0, 0, true, name);        //TODO
            players.add((Player) this.mapFields[0][0]);
        } else if (ID == 1){
            this.mapFields[Consts.DIMENSION-1][Consts.DIMENSION-1] = new Player(Consts.DIMENSION-1, Consts.DIMENSION-1, true, name);        //TODO
            players.add((Player) this.mapFields[Consts.DIMENSION-1][Consts.DIMENSION-1]);
        }
    }

    public void destroyField(int x, int y, Field newField, JSONArray answer) {
        if (this.mapFields[y][x] instanceof Player) {
            int tempId = ((Player) this.mapFields[y][x]).getId();
            ((Player) this.mapFields[y][x]).kill();
            players.set(tempId, null);
        }
        this.mapFields[y][x] = newField;
        printFieldOfMap(x, y, answer); //TODO
    }

    public void deletePlayerFromMap(Player player) {
        int tempId = player.getId();                //TODO?????????? czy ustawic nowe pole?
        getPlayer(player.getId()).kill();
        players.set(tempId, null);
    }

    public boolean canMove(int x, int y) {
        if (x < 0 || y < 0 || x > Consts.DIMENSION - 1 || y > Consts.DIMENSION - 1) {
            return false;
        }
        if (this.mapFields[y][x].isEmpty()) {
            return true;
        }
        return false;
    }

    public void addBomb(Bomb bomb) {
        bombExecutors.submit(() -> {
            while (true) {
                if ((System.currentTimeMillis() - bomb.getStartTime()) > Consts.MILIS_TO_EXPLODE) {
                    Platform.runLater(() -> explode(bomb));
                    break;
                }
            }
            return;
        });
    }

    public void addFire(Fire fire) {
        breakLoopFires = 1;
        this.fires.add(fire);
    }

    public String printEntireMap() {
        String mapp="";
        for (int i = 0; i < Consts.DIMENSION; i++) {       //
            for (int j = 0; j < Consts.DIMENSION; j++) {
                Integer blockNumber = fieldImages.get(mapFields[j][i].getName());
                mapp += Integer.toString(blockNumber);
            }
        }
        return mapp;
    }

    public void printFieldOfMap(int x, int y, JSONArray answer) {
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get(getMapField(x, y).getName())));
        temp.put("y", Integer.toString(y));
        temp.put("x", Integer.toString(x));
        answer.put(temp);
    }

    public void printNamedBlockOnMap(int x, int y, String Name, JSONArray answer){
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get(Name)));
        temp.put("y", Integer.toString(y));
        temp.put("x", Integer.toString(x));
        answer.put(temp);
    }

    public boolean incCoords(int finalID, String direction, JSONArray answer) {
        int diffX = 0;
        int diffY = 0;
        if (direction.equals("UP")) {
            diffY = -1;
        } else if (direction.equals("LEFT")) {
            diffX = -1;
        } else if (direction.equals("DOWN")) {
            diffY = 1;
        } else {                //RIGHT
            diffX = 1;
        }

        int newX = players.get(finalID).getX() + diffX;
        int newY = players.get(finalID).getY() + diffY;

        if (canMove(newX, newY)) {
            int playerX = players.get(finalID).getX();
            int playerY = players.get(finalID).getY();
            if (getMapField(playerX, playerY) instanceof Bomb == false) {         //gracz schodzi ze zwyklego pola
                setMapField(playerX, playerY, new NormalBlock(playerX, playerY, false, true));
            } else {
                printNamedBlockOnMap(playerX, playerY, "DefaultBlock", answer);     //gracz schodzi z bomby
            }
            //Nowe pole
            if (getMapField(newX, newY) instanceof Fire == true) {              //wszedl w ogien
                printNamedBlockOnMap(playerX, playerY, "DefaultBlock", answer);
                deletePlayerFromMap(players.get(finalID));
                return true;
            } else if (getMapField(newX, newY) instanceof Bonus == true) {      //wszedl na bonus
                ((Bonus) getMapField(newX, newY)).takeBonus(players.get(finalID));              //TODO wyslac do gracza info, ze moze szybciej beigac
                if (getMapField(newX, newY).getName().equals("Bonushaste")){
                    JSONObject msg = new JSONObject();
                    msg.put("cmd", "incspeed");
                    Broadcaster.msgToOne(clients.get(finalID), msg.toString(), socket);
                }
                printNamedBlockOnMap(newX, newY, "DefaultBlock", answer);
            }
            printFieldOfMap(playerX,playerY, answer);
            players.get(finalID).move(diffX, diffY);
            setMapField(players.get(finalID).getX(), players.get(finalID).getY(), players.get(finalID));
            printFieldOfMap(players.get(finalID).getX(), players.get(finalID).getY(), answer);
            return true;
        }
        return false;
    }

    //BOMB

    public void dropBomb(int playerIndex, JSONArray answer) {
        Player player = players.get(playerIndex);
        int x = player.getX();
        int y = player.getY();
        if (player.getNBombs() > 0 && (getMapField(x, y) instanceof Bomb == false) && player.isAlive()) {
            setMapField(x, y, new Bomb(x, y, true, player));
            addBomb((Bomb) getMapField(x, y));
            player.decNBombs();
            printNamedBlockOnMap(x, y, "Bomb", answer);
            printNamedBlockOnMap(x, y, "Player", answer);
        }
    }

    public void explode(Bomb bomb) {
        JSONObject answerToSend = new JSONObject();
        answerToSend.put("cmd", "move");

        JSONArray arrayOfFields = new JSONArray();
        if (bomb.getX() == bomb.getOwnerOfBomb().getX() && bomb.getY() == bomb.getOwnerOfBomb().getY()){
            bomb.getOwnerOfBomb().kill();
        }
        Fire newFire = new Fire(bomb.getX(), bomb.getY(), true);
        destroyField(bomb.getX(), bomb.getY(), newFire, arrayOfFields);
        addFire(newFire);

        boolean firstUp = false;
        boolean firstRight = false;
        boolean firstDown = false;
        boolean firstLeft = false;
        for (int i = 1; i < bomb.getRange() + 1; i++) {
            if (bomb.getY() - i >= 0 && !firstUp) {                                         //up
                firstUp = checkFieldToBurn(bomb.getX(), bomb.getY() - i, arrayOfFields);
            }
            if (bomb.getX() + i < Consts.DIMENSION && !firstRight) {                        //right
                firstRight = checkFieldToBurn(bomb.getX() + i, bomb.getY(), arrayOfFields);
            }
            if (bomb.getY() + i < Consts.DIMENSION && !firstDown) {                         //down
                firstDown = checkFieldToBurn(bomb.getX(), bomb.getY() + i, arrayOfFields);
            }
            if (bomb.getX() - i >= 0 && !firstLeft) {                                       //left
                firstLeft = checkFieldToBurn(bomb.getX() - i, bomb.getY(), arrayOfFields);
            }
        }
        bomb.getOwnerOfBomb().incNBombs();
        answerToSend.put("fields", arrayOfFields);
        Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
        System.out.println("Wybuch:\t\t" + answerToSend.toString());
    }

    private boolean checkFieldToBurn(int xToCheck, int yToCheck, JSONArray answer) {
        if (getMapField(xToCheck, yToCheck) instanceof Bomb) {
            ((Bomb) getMapField(xToCheck, yToCheck)).decTime();
            return true;
        }
        if (!getMapField(xToCheck, yToCheck).isDestroyable()                                //niezniszczalny kloc
                && !getMapField(xToCheck, yToCheck).isEmpty()) {
            return true;
        } else {
            Fire newFire = new Fire(xToCheck, yToCheck, false);
            if (getMapField(xToCheck, yToCheck).isDestroyable()) {                          //do zniszczenia
                newFire.setUnderField(getMapField(xToCheck, yToCheck).getFieldUnderDestryableField());
                destroyField(xToCheck, yToCheck, newFire, answer);
                addFire(newFire);
                return true;
            } else {
/*                if (map.getMapField(xToCheck, yToCheck) instanceof Fire) {            //TODO jesli ogien ma przechodzic przez ogien (nowe watki?)
                    ((Fire) map.getMapField(xToCheck, yToCheck)).removeFire();
                }*/
                setMapField(xToCheck, yToCheck, newFire);
                addFire(newFire);
                printNamedBlockOnMap(xToCheck, yToCheck, "Fire", answer);
                return false;
            }
        }
    }

    //FIRE

    public void removeFire(Fire fire, JSONArray fieldsArray) {
        if (fire.getFieldUnderFireField() == null) {
            destroyField(fire.getX(), fire.getY(), new NormalBlock(fire.getX(), fire.getY(), false, true), fieldsArray);
        } else {
            printNamedBlockOnMap(fire.getX(), fire.getY(), "DefaultBlock", fieldsArray);
            setMapField(fire.getX(), fire.getY(), fire.getFieldUnderFireField());
            printFieldOfMap(fire.getX(), fire.getY(), fieldsArray);
        }
    }

    public  void firesLoop(){
        firesExecutor.execute(() -> {
            while (true){
                Iterator it = getFires().iterator();
                JSONObject answerToSend = new JSONObject();
                answerToSend.put("cmd", "move");
                JSONArray fieldsArray = new JSONArray();
                while (it.hasNext()){
                    if (breakLoopFires == 1){
                        try {
                            Thread.sleep(10);                           //TODO da sie to rozwiazac inaczej?
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        breakLoopFires = 0;
                        break;
                    }
                    Fire tempFire = (Fire) it.next();
                    if (System.currentTimeMillis() - tempFire.getStartTime() > Consts.FIRE_MILIS){
                        removeFire(tempFire, fieldsArray);
                        it.remove();
                    }
                }
                if (!fieldsArray.isNull(0)){
                    answerToSend.put("fields", fieldsArray);
                    Broadcaster.broadcastMessage(clients, answerToSend.toString(), socket);
                }
            }
        });
    }
}
