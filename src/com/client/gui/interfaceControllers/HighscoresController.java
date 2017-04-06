package com.client.gui.interfaceControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.util.logging.Level;

public class HighscoresController extends MainStageController {
    private final int topScoresAmount = 6;
    private final String highscoresDataName = "highscores.dat";
    
    private Label topScoresLabels[] = new Label[topScoresAmount];
    private SingleScore[] bestScores = new SingleScore[topScoresAmount];
    
    
    
    @FXML
    private Label top1LabelScore;
    @FXML
    private Label top2LabelScore;
    @FXML
    private Label top3LabelScore;
    @FXML
    private Label top4LabelScore;
    @FXML
    private Label top5LabelScore;
    @FXML
    private Label top6LabelScore;
    
    
    @FXML
    public void initialize() {
        topScoresLabels[0] = top1LabelScore;
        topScoresLabels[1] = top2LabelScore;
        topScoresLabels[2] = top3LabelScore;
        topScoresLabels[3] = top4LabelScore;
        topScoresLabels[4] = top5LabelScore;
        topScoresLabels[5] = top6LabelScore;
        
        for (int i = 0; i < topScoresAmount; i++)
            bestScores[i] = new SingleScore();
        
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(highscoresDataName))) {
            bestScores = (SingleScore[]) input.readObject();
            readHighscores();
        }
        catch (IOException e) {
            log.warning("Highscores file not found!");
            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(highscoresDataName))) {
                output.writeObject(bestScores);
                log.info("Highscores file saved to file!");
                readHighscores();
            }
            catch (IOException e1) {
                log.log(Level.SEVERE, e1.getMessage(), e1);
            }
        }
        catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    @FXML
    void resetHighscores() {
        for (int i = 0; i < topScoresAmount; i++)
            bestScores[i] = new SingleScore();
    
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(highscoresDataName))) {
            output.writeObject(bestScores);
            readHighscores();
            log.info("Highscores reseted!");
        }
        catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    @FXML
    void mouseClickedLabel() {
    
    }
    
    @FXML
    void mouseEnteredLabel() {
    
    }
    
    @FXML
    void mouseExitedLabel() {
    
    }
    
    public void addNewHighscore(SingleScore newScore) {
        SingleScore copy;
        for (int i = 0; i < topScoresAmount; i++) {
            if (bestScores[i].compareTo(newScore)) {                // newScore bigger
                if (i != topScoresAmount - 1) {
                    for (int j = topScoresAmount - 2; j >= i; j--)
                        bestScores[j + 1] = bestScores[j];
                }
                bestScores[i] = newScore;
                return;
            }
        }
    }
    
    private void readHighscores() {
        for (int i = 0; i < topScoresAmount; i++)
            topScoresLabels[i].setText(bestScores[i].get());
    }
}
