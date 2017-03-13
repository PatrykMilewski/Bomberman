package com.bomberman.gui.menu;

import com.bomberman.fields.Block;
import com.bomberman.fields.NormalBlock;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Map extends Parent{
    private MainStage mainStage;
    private Block[][] mapFields;
    private Pane spaceForMap;
    private Pane spaceForScores;


    public Map(MainStage mainStage) throws IOException {
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

        HBox mapGrids = new HBox(4);
        mapGrids.setTranslateX(37);
        mapGrids.setTranslateY(37);

        this.spaceForMap = new Pane();
        this.spaceForMap.setPrefSize(525, 525);
        this.spaceForMap.setStyle("-fx-background-color: brown;");

        this.spaceForScores = new Pane();
        this.spaceForScores.setPrefSize(197, 525);
        this.spaceForScores.setStyle("-fx-background-color: brown;");

        mapGrids.getChildren().addAll(this.spaceForMap, this.spaceForScores);                           //dodaj do grida mapGrids
        getChildren().addAll(mapGrids);                                                                 //dodaj groda do klasy Map
        this.mainStage.getRootElem().getChildren().addAll(viewBackground, this);                         //dodaj wszystkie zmiany (Map) do glownego pejna

        printMap();
    }

    private void printMap(){
        javafx.scene.shape.Rectangle textBackground = new javafx.scene.shape.Rectangle();

        for (int i = 0; i < 21; i++){
            for (int j = 0; j < 21; j++){
                textBackground = this.mapFields[i][j].printFiled(this.mapFields[i][j].getX(), this.mapFields[i][j].getY());
                this.spaceForMap.getChildren().addAll(textBackground);
            }
        }
    }

    private void fillMap(){
        this.mapFields = new NormalBlock[21][21];
        for (int i=0; i<21; i++){
            for (int j =0; j<21; j++){
                this.mapFields[i][j] = new NormalBlock(j, i, false, true);          //Bloki puste
            }
        }

        for (int i = 1; i < 21; i+=2){
            for (int j = 1; j < 21; j+=2){
                this.mapFields[i][j] = new NormalBlock(j, i,  false, false);       //Blok nie do rozbicia
            }
        }

        Random generator = new Random();
        for (int i = 0; i < 21; i++){
            for (int j = 0; j < 21; j++){
                if ((i % 2!=1) && (j % 2!=1) && generator.nextBoolean())
                    this.mapFields[i][j] = new NormalBlock(j, i, true, false);     //Blok do rozbicia
            }
        }
        mapFields[0][0] = new NormalBlock(0, 0, false, true);                                                        //pola puste dla graczy
        mapFields[0][20] = new NormalBlock(20, 0, false, true);
        mapFields[20][0] = new NormalBlock(0, 20, false, true);
        mapFields[20][20] = new NormalBlock(20, 20, false, true);
    }
}