package com.bomberman.gui;

import com.bomberman.BombTimerTask;
import com.bomberman.fields.*;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameMap extends Parent{
    private MainStage mainStage;
    private Field[][] mapFields;
    private Pane spaceForMap;
    private Pane spaceForScores;
    private Player player;
    private ArrayList<Fire> fires;
    private ExecutorService bombExecutors = Executors.newFixedThreadPool(Consts.MAX_N_BOMBS * 3);   //max liczba bomb na mapie * liczba graczy

    public GameMap(MainStage mainStage) throws IOException {
        mainStage.getRootElem().getChildren().clear();
        this.mainStage = mainStage;
        this.fires = new ArrayList<>();
        makeMap();
    }
    public Field getMapField(int x, int y){
        return this.mapFields[y][x];
    }

    public Player getPlayer() {return player;}

    public ArrayList<Fire> getFires(){
        return this.fires;
    }

    public void setMapField(int x, int y, Field field){
        this.mapFields[y][x] = field;
    }

    private void makeMap() throws IOException {
        fillMap();

        HBox mapGrids = new HBox(10);
        mapGrids.setTranslateX(27);
        mapGrids.setTranslateY(27);

        this.spaceForMap = mainStage.gameController.getGameMapPane();

        this.spaceForScores = mainStage.gameController.getGameScoresPane();

        mapGrids.getChildren().addAll(this.spaceForMap, this.spaceForScores);                           //dodaj do grida mapGrids
        getChildren().addAll(mapGrids);                                                                 //dodaj groda do klasy Map
        this.mainStage.getRootElem().getChildren().addAll(this);                         //dodaj wszystkie zmiany (Map) do glownego pejna
    }

    private void fillMap(){
        this.mapFields = new Field[Consts.DIMENSION][Consts.DIMENSION];
        for (int i=0; i<Consts.DIMENSION; i++){
            for (int j =0; j<Consts.DIMENSION; j++){
                this.mapFields[i][j] = new NormalBlock(j, i, false, true);          //Bloki puste
            }
        }

        for (int i = 1; i < Consts.DIMENSION; i+=2){
            for (int j = 1; j < Consts.DIMENSION; j+=2){
                this.mapFields[i][j] = new NormalBlock(j, i,  false, false);       //Blok nie do rozbicia
            }
        }

        Random generator = new Random();
        for (int i = 0; i < Consts.DIMENSION; i++){
            for (int j = 0; j < Consts.DIMENSION; j++){
                if ( !((i % 2==1) && (j % 2==1)) && (generator.nextDouble() > Consts.NORMAL_BLOCK_PROB))
                    this.mapFields[i][j] = new NormalBlock(j, i, true, false);     //Blok do rozbicia
            }
        }

        mapFields[0][0] = new NormalBlock(0, 0, false, true);             //pola puste dla graczy
        mapFields[0][1] = new NormalBlock(1, 0, false, true);
        mapFields[1][0] = new NormalBlock(0, 1, false, true);
        mapFields[0][Consts.DIMENSION-1] = new NormalBlock(Consts.DIMENSION-1, 0, false, true);
        mapFields[0][Consts.DIMENSION-2] = new NormalBlock(Consts.DIMENSION-2, 0, false, true);
        mapFields[1][Consts.DIMENSION-1] = new NormalBlock(Consts.DIMENSION-1, 1, false, true);
        mapFields[Consts.DIMENSION-1][0] = new NormalBlock(0, Consts.DIMENSION-1, false, true);
        mapFields[Consts.DIMENSION-2][0] = new NormalBlock(0, Consts.DIMENSION-2, false, true);
        mapFields[Consts.DIMENSION-1][1] = new NormalBlock(1, Consts.DIMENSION-1, false, true);
        mapFields[Consts.DIMENSION-1][Consts.DIMENSION-1] = new NormalBlock(Consts.DIMENSION-1, Consts.DIMENSION-1, false, true);
        mapFields[Consts.DIMENSION-2][Consts.DIMENSION-1] = new NormalBlock(Consts.DIMENSION-1, Consts.DIMENSION-2, false, true);
        mapFields[Consts.DIMENSION-1][Consts.DIMENSION-2] = new NormalBlock(Consts.DIMENSION-2, Consts.DIMENSION-1, false, true);
    }

    public void createPlayer(int x, int y, String name){
        this.mapFields[y][x] = new Player(x, y, true, name, this);
        player = (Player)this.mapFields[y][x];
        printEntireMap();
    }

    public void destroyField(int x, int y, Field newField){
        if (this.mapFields[y][x] instanceof Player){
            player.kill();
            player = null;
        } else if (this.mapFields[y][x].getFieldUnderDestryableField() != null && newField instanceof Fire){    //bonus pod spodem
            ((Fire) newField).setUnderField(this.mapFields[y][x].getFieldUnderDestryableField());
        }
        this.mapFields[y][x] = newField;
        this.printFieldOfMap(x, y);
    }

    public void deletePlayerFromMap(Player player){
        player.kill();
        player = null;
    }

    public boolean canMove(int x, int y){
        if (x < 0 || y < 0 || x > Consts.DIMENSION-1 || y > Consts.DIMENSION-1){
            return false;
        }
        if (this.mapFields[y][x].isEmpty()){
            return true;
        }
        return false;
    }

    public void addBomb(Bomb bomb){
        Task bombTimerTask = new BombTimerTask(bomb);
        bombExecutors.execute(bombTimerTask);
    }

    public void addFire(Fire fire) {
        this.fires.add(fire);
        printFireBlockOnMap(fire.getX(), fire.getY());
    }

    public void printEntireMap(){
        ImageView imgView = new ImageView();
        for (int i = 0; i < Consts.DIMENSION; i++){
            for (int j = 0; j < Consts.DIMENSION; j++){
                imgView = this.mapFields[i][j].printFiled();
                this.spaceForMap.getChildren().addAll(imgView);
            }
        }
        imgView = NormalBlock.printNormalBlock(this.player.getX(), this.player.getY());
        this.spaceForMap.getChildren().addAll(imgView);

        imgView = this.mapFields[player.getY()][player.getX()].printFiled();
        this.spaceForMap.getChildren().addAll(imgView);
    }

    public void printFieldOfMap(int x, int y){
        ImageView imgView = this.mapFields[y][x].printFiled();
        this.spaceForMap.getChildren().addAll(imgView);
    }

    public void printPlayerOnMap(){
        ImageView imgView = this.player.printPlayer();
        this.spaceForMap.getChildren().addAll(imgView);
    }

    public void printNormalBlockOnMap(int x, int y) {
        ImageView imgView = NormalBlock.printNormalBlock(x, y);
        this.spaceForMap.getChildren().addAll(imgView);
    }

    public void printFireBlockOnMap(int x, int y){
        ImageView imgView = Fire.printFireBlock(x, y);
        this.spaceForMap.getChildren().addAll(imgView);
    }
}