package com.server;

import com.client.gui.interfaceControllers.HighscoresController;
import com.elements.loggers.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Highscores {
    private static Logger log = LoggerFactory.getLogger(Highscores.class.getCanonicalName());
    
    private static final String HIGHSCORES_DATA_NAME = "highscores.dat";
    private static final int TOP_SCORES_AMOUNT = 6;
    
    private SingleScore[] bestScores;

    public Highscores() throws IOException, ClassNotFoundException {
        bestScores = new SingleScore[TOP_SCORES_AMOUNT];
        for (int i =0; i < TOP_SCORES_AMOUNT; i++){
            bestScores[i] = new SingleScore();
        }
        readScores();
        bestScores[0].set("test", 1234);
    }

    private void readScores(){
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(HIGHSCORES_DATA_NAME))) {
            bestScores = (SingleScore[]) input.readObject();
        }
        catch (IOException e) {
            log.warning("Highscores file not found!");
            saveScores();
        }
        catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void saveScores(){
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(HIGHSCORES_DATA_NAME))) {
            output.writeObject(bestScores);
            log.info("Highscores file saved to file!");
        }
        catch (IOException e1) {
            log.log(Level.SEVERE, e1.getMessage(), e1);
        }
    }

    public JSONArray getScoresInJSON(){
        JSONArray returnMessage = new JSONArray();

            for (SingleScore score : bestScores){
                if (score == null) {
                    break;
                }
                JSONObject singleScore = new JSONObject();
                singleScore.put("name", score.getName());
                singleScore.put("score", Integer.toString(score.getScore()));
                returnMessage.put(singleScore);
            }
            return returnMessage;
    }

    public void addNewHighscore(SingleScore newScore) {
        for (int i = 0; i < TOP_SCORES_AMOUNT; i++) {
            if (bestScores[i].compareTo(newScore)) {                // newScore bigger
                if (i != TOP_SCORES_AMOUNT - 1) {
                    for (int j = TOP_SCORES_AMOUNT - 2; j >= i; j--)
                        bestScores[j + 1] = bestScores[j];
                }
                bestScores[i] = newScore;
                return;
            }
        }
    }
}
