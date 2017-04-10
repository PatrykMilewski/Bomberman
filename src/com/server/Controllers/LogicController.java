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
        this.mapFields[0][0] = new Player(0, 0, true, name);        //TODO
        players.add((Player) this.mapFields[0][0]);
    }

    public void destroyField(int x, int y, Field newField, JSONArray answer) {
        if (this.mapFields[y][x] instanceof Player) {
            int tempId = ((Player) this.mapFields[y][x]).getId();
            ((Player) this.mapFields[y][x]).kill();
            players.set(tempId, null);
        } else if (this.mapFields[y][x].getFieldUnderDestryableField() != null && newField instanceof Fire) {    //bonus pod spodem
            ((Fire) newField).setUnderField(this.mapFields[y][x].getFieldUnderDestryableField());
        }
        this.mapFields[y][x] = newField;
        printFieldOfMap(x, y, mapFields[y][x].getName(), answer); //TODO
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
        bombExecutors.execute(() -> {
            while (true) {
                if ((System.currentTimeMillis() - bomb.getStartTime()) > Consts.MILIS_TO_EXPLODE) {
                    Platform.runLater(() -> explode(bomb));
                    break;
                }
            }
        });
    }

    public void addFire(Fire fire) {
        this.fires.add(fire);
    }

    public String printEntireMap() {
        String mapp="";
        for (int i = 0; i < Consts.DIMENSION; i++) {       //
            for (int j = 0; j < Consts.DIMENSION; j++) {
                Integer blockNumber = fieldImages.get(mapFields[i][j].getName());
                mapp += Integer.toString(blockNumber);
                
//                if(blockNumber != 4) {
//                    JSONObject temp = new JSONObject();
//                    temp.put("f", Integer.toString(blockNumber));
//                    temp.put("y", Integer.toString(j));
//                    temp.put("x", Integer.toString(i));
//                    answer.put(temp);
//                }
            
            }
        }
        return mapp;
    }

    public void printFieldOfMap(int x, int y, String field, JSONArray answer) {
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get(field)));
        temp.put("y", Integer.toString(y));
        temp.put("x", Integer.toString(x));
        answer.put(temp);
    }

    public void printPlayerOnMap(int index, JSONArray answer) {
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get("Player")));
        temp.put("y", Integer.toString(players.get(index).getY()));
        temp.put("x", Integer.toString(players.get(index).getX()));
        answer.put(temp);
    }

    public void printNormalBlockOnMap(int x, int y, JSONArray answer) {
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get("NormalBlock")));
        temp.put("y", Integer.toString(y));
        temp.put("x", Integer.toString(x));
        answer.put(temp);
    }

    public void printFireBlockOnMap(int x, int y, JSONArray answer) {
        JSONObject temp = new JSONObject();
        temp.put("field", Integer.toString(fieldImages.get("Fire")));
        temp.put("y", Integer.toString(y));
        temp.put("x", Integer.toString(x));
        answer.put(temp);
    }

    public boolean incCoords(int finalID, String direction, JSONArray answer) {

        int diffX;
        int diffY;
        if (direction.equals("UP")) {
            diffX = 0;
            diffY = -1;
        } else if (direction.equals("LEFT")) {
            diffX = -1;
            diffY = 0;
        } else if (direction.equals("DOWN")) {
            diffX = 0;
            diffY = 1;
        } else {                //UP
            diffX = 1;
            diffY = 0;
        }
        int newX = players.get(finalID).getX() + diffX;
        int newY = players.get(finalID).getY() + diffY;

        if (canMove(newX, newY)) {

            int playerX = players.get(finalID).getX();
            int playerY = players.get(finalID).getY();

            if (getMapField(playerX, playerY) instanceof Bomb == false) {         //gracz schodzi ze zwyklego pola
                setMapField(playerX, playerY, new NormalBlock(playerX, playerY, false, true));
            } else {
                printNormalBlockOnMap(playerX, playerY, answer);                          //gracz schodzi z bomby
            }
            //Nowe pole
            if (getMapField(newX, newY) instanceof Fire == true) {      //wszedl w ogien
                printNormalBlockOnMap(playerX, playerY, answer);
                deletePlayerFromMap(players.get(finalID));
                return true;
            } else if (getMapField(newX, newY) instanceof Bonus == true) {  //wszedl na bonus
                ((Bonus) getMapField(newX, newY)).takeBonus(players.get(finalID));              //TODO wyslac do gracza info, ze moze szybciej beigac
                JSONObject msg = new JSONObject();
                msg.put("bonus", "speed");
                Broadcaster.msgToOne(clients.get(finalID), msg.toString(), socket);
                printNormalBlockOnMap(newX, newY, answer);
            }
            printFieldOfMap(playerX,playerY, "DefaultBlock", answer);
            players.get(finalID).move(diffX, diffY);
            setMapField(playerX, playerY, players.get(finalID));
            printFieldOfMap(players.get(finalID).getX(), players.get(finalID).getY(), "Player", answer);
            System.out.println(answer.toString());

//            this.map.setMapField(this.x, this.y, this);     //TODO
//            map.printFieldOfMap(this.x, this.y);
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
            printPlayerOnMap(player.getId(), answer);
        }
    }

    public void explode(Bomb bomb) {
        JSONArray answer = new JSONArray();
        if (bomb.getX() == bomb.getOwnerOfBomb().getX() && bomb.getY() == bomb.getOwnerOfBomb().getY()){
            bomb.getOwnerOfBomb().kill();
        }
        Fire newFire = new Fire(bomb.getX(), bomb.getY(), true);
        destroyField(bomb.getX(), bomb.getY(), newFire, answer);
        printFireBlockOnMap(bomb.getX(), bomb.getY(), answer);              //TODO rysuje bonus??????
        addFire(newFire);

        boolean firstUp = false;
        boolean firstRight = false;
        boolean firstDown = false;
        boolean firstLeft = false;
        for (int i = 1; i < bomb.getRange() + 1; i++) {
            if (bomb.getY() - i >= 0 && !firstUp) {                                         //up
                firstUp = checkFieldToBurn(0, bomb.getY() - i, answer);
            }
            if (bomb.getX() + i < Consts.DIMENSION && !firstRight) {                        //right
                firstRight = checkFieldToBurn(bomb.getX() + i, 0, answer);
            }
            if (bomb.getY() + i < Consts.DIMENSION && !firstDown) {                         //down
                firstDown = checkFieldToBurn(0, bomb.getY() + i, answer);
            }
            if (bomb.getX() - i >= 0 && !firstLeft) {                                       //left
                firstLeft = checkFieldToBurn(bomb.getX() - i, 0, answer);
            }
        }
        bomb.getOwnerOfBomb().incNBombs();
        Broadcaster.broadcastMessage(clients, answer.toString(), socket);
        System.out.println(answer.toString());
    }

    private boolean checkFieldToBurn(int xToCheck, int yToCheck, JSONArray answer) {
        if (getMapField(xToCheck, yToCheck) instanceof Bomb) {
            ((Bomb) getMapField(xToCheck, yToCheck)).decTime();
            return true;
        }
        if (!getMapField(xToCheck, yToCheck).isDestroyable()        //niezniszczalny kloc
                && !getMapField(xToCheck, yToCheck).isEmpty()) {
            return true;
        } else {
            Fire newFire = new Fire(xToCheck, yToCheck, false);
            if (getMapField(xToCheck, yToCheck).isDestroyable()) {               //do zniszczenia
                destroyField(xToCheck, yToCheck, newFire, answer);
                addFire(newFire);
                printFireBlockOnMap(xToCheck, yToCheck, answer);
                return true;
            } else {
/*                if (map.getMapField(xToCheck, yToCheck) instanceof Fire) {            //TODO jesli ogien ma przechodzic przez ogien (nowe watki?)
                    ((Fire) map.getMapField(xToCheck, yToCheck)).removeFire();
                }*/
                setMapField(xToCheck, yToCheck, newFire);
                addFire(newFire);
                return false;
            }
        }
    }

    //FIRE

    public void removeFire(Fire fire) {
        JSONArray message = new JSONArray();
        if (fire.getFieldUnderFireField() == null) {
            destroyField(fire.getX(), fire.getY(), new NormalBlock(fire.getX(), fire.getY(), false, true), message);
        } else {
            printNormalBlockOnMap(fire.getX(), fire.getY(), message);      //TODO
            destroyField(fire.getX(), fire.getY(), fire.getFieldUnderFireField(), message);
        }
        Broadcaster.broadcastMessage(clients, message.toString(), socket);
    }

    public void firesLoop(){
        firesExecutor.execute(() -> {
            Iterator it = getFires().iterator();
            while (it.hasNext()){
                Fire tempFire = (Fire) it.next();
                if (System.currentTimeMillis() - tempFire.getStartTime() > Consts.FIRE_MILIS){
                    removeFire(tempFire);
                    it.remove();
                }
            }
        });
    }
}
