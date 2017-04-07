package com.client;

import com.client.gui.ClientConsts;
import com.client.gui.ClientMainStage;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

public class ClientMap extends Parent {

    int map[][];
    HashMap<Integer, String> fieldImages;
    private ClientMainStage mainStage;
    private Pane spaceForMap;
    private Pane spaceForScores;
    public ClientMap(ClientMainStage mainStage) throws IOException{
        
        map = new int[ClientConsts.DIMENSION][ClientConsts.DIMENSION];
        fieldImages = new HashMap<>();
        this.mainStage = mainStage;
        fillHashMap();
        System.out.println("DUPA5");
        makeMap();
        System.out.println("DUPA6");
    }

    private void makeMap() throws IOException {

        HBox mapGrids = new HBox(10);
        mapGrids.setTranslateX(27);
        mapGrids.setTranslateY(27);

        for (int i = 1; i < ClientConsts.DIMENSION; i += 2)
            for (int j = 1; j < ClientConsts.DIMENSION; j += 2)
                this.map[i][j] = 4;       //Blok nie do rozbicia
        System.out.println("DUPA7");
            this.spaceForMap = mainStage.gameController.getGameMapPane();
            this.spaceForScores = mainStage.gameController.getGameScoresPane();
        System.out.println("DUPA8");
            mapGrids.getChildren().addAll(this.spaceForMap, this.spaceForScores);
        System.out.println("DUPA9");//dodaj do grida mapGrids
            getChildren().addAll(mapGrids);
        System.out.println("DUPA10");//dodaj groda do klasy Map
            this.mainStage.getRootElem().getChildren().addAll(this);
        System.out.println("DUPA11");//dodaj wszystkie zmiany (Map) do glownego pejna
    }

    public void printEntireMap(){
        System.out.println("SIEMA Z PRINTA MORDY");
        for (int i = 0; i < ClientConsts.DIMENSION; i++)
            for (int j = 0; j < ClientConsts.DIMENSION; j++)
            {
                System.out.print(map[i][j]);
                String temp = fieldImages.get(map[i][j]);
                Image img = new Image("file:" + "images/Blocks/" + temp);
                ImageView imgView = new ImageView(img);
                imgView.setX(i * ClientConsts.PIXEL_SIZE);
                imgView.setY(j * ClientConsts.PIXEL_SIZE);
                this.spaceForMap.getChildren().addAll(imgView);
            }
    }

    private final void fillHashMap()
    {
        fieldImages.put(0, "defaultBlock.png");
        fieldImages.put(1, "destroyableBlock.png");
        fieldImages.put(2, "fireblock1.png");
        fieldImages.put(3, "playerBlock.png");
        fieldImages.put(4, "unDestroyableBlock.png");
        fieldImages.put(5, "bomb/bomb.gif");
    }
}
