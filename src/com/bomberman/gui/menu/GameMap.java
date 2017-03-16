package com.bomberman.gui.menu;

import com.bomberman.fields.*;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GameMap extends Parent{
    private MainStage mainStage;
    private Field[][] mapFields;
    private Pane spaceForMap;
    private Pane spaceForScores;
    private Player player;

    public GameMap(MainStage mainStage) throws IOException {
        mainStage.getRootElem().getChildren().clear();              //TODO
        this.mainStage = mainStage;

        makeMap();
    }

    private void makeMap() throws IOException {
        InputStream backgroundMap = Files.newInputStream(Paths.get("images/backgroundmap.jpg"));
        Image img = new Image(backgroundMap);
        backgroundMap.close();
        ImageView viewBackground = new ImageView(img);

        fillMap();

        HBox mapGrids = new HBox(10);
        mapGrids.setTranslateX(27);
        mapGrids.setTranslateY(27);

        this.spaceForMap = new Pane();
        this.spaceForMap.setPrefSize(Consts.DIMENSION*Consts.PIXEL_SIZE, Consts.DIMENSION*Consts.PIXEL_SIZE);
        this.spaceForMap.setStyle("-fx-background-color: brown;");

        this.spaceForScores = new Pane();
        this.spaceForScores.setPrefSize(502, Consts.DIMENSION*Consts.PIXEL_SIZE);
        this.spaceForScores.setStyle("-fx-background-color: brown;");

        mapGrids.getChildren().addAll(this.spaceForMap, this.spaceForScores);                           //dodaj do grida mapGrids
        getChildren().addAll(mapGrids);                                                                 //dodaj groda do klasy Map
        this.mainStage.getRootElem().getChildren().addAll(viewBackground, this);                         //dodaj wszystkie zmiany (Map) do glownego pejna

//        printEntireMap();
    }

    public void destroyField(int x, int y){
        this.mapFields[y][x] = new NormalBlock(x, y, false, true);
    }

    public void printEntireMap(){
        ImageView imgView = new ImageView();
        for (int i = 0; i < Consts.DIMENSION; i++){
            for (int j = 0; j < Consts.DIMENSION; j++){
                imgView = this.mapFields[i][j].printFiled(this.mapFields[i][j].getX(), this.mapFields[i][j].getY());
                this.spaceForMap.getChildren().addAll(imgView);
            }
        }
        imgView = NormalBlock.printNormalBlock(this.player.getX(), this.player.getY());
        this.spaceForMap.getChildren().addAll(imgView);

        imgView = this.mapFields[player.getY()][player.getX()].printFiled(this.player.getX(), this.player.getY());
        this.spaceForMap.getChildren().addAll(imgView);
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
                if ( !((i % 2==1) && (j % 2==1)) && (generator.nextDouble() > 0.5))
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

    public Player getPlayer() {return player;}

    public void setMapField(int x, int y, Field field){
        this.mapFields[y][x] = field;
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

    public void createPlayer(int x, int y, String name){
        this.mapFields[y][x] = new Player(x, y, true, name, this);
        player = (Player)this.mapFields[y][x];

        printEntireMap();           //TODO ????
    }
}