package com.angrybirds.game.GameState;

import com.angrybirds.game.Objects.Bird;
import com.angrybirds.game.Objects.Materials.Material;
import com.angrybirds.game.Objects.Pig;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // Level Number
    private int level;
    private int currentLevel;
    private boolean isLoadedFirstTime = true;
    private int score;

    // Birds, pigs, and blocks
    private ArrayList<BirdState> birds;
    private ArrayList<PigState> pigs;
    private ArrayList<MaterialState> blocks;

    // Bird state
    private BirdState currentBird;

    public GameState(int level, int currentLevel, ArrayList<BirdState> birds, ArrayList<PigState> pigs, ArrayList<MaterialState> blocks, int score, BirdState currentBird) {
        this.level = level;
        this.currentLevel = currentLevel;
        this.birds = birds;
        this.pigs = pigs;
        this.blocks = blocks;
        this.score = score;
        this.currentBird = currentBird;
    }

    // Getters and Setters
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<BirdState> getBirds() {
        return birds;
    }

    public void setBirds(ArrayList<BirdState> birds) {
        this.birds = birds;
    }

    public ArrayList<PigState> getPigs() {
        return pigs;
    }

    public void setPigs(ArrayList<PigState> pigs) {
        this.pigs = pigs;
    }

    public ArrayList<MaterialState> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<MaterialState> blocks) {
        this.blocks = blocks;
    }

    public BirdState getCurrentBird() {
        return currentBird;
    }

    public void setCurrentBird(BirdState currentBird) {
        this.currentBird = currentBird;
    }

    public int getNoOfBirds() {
        return birds.size();
    }

    public int getNoOfPigs() {
        return pigs.size();
    }

    public int getRedBirds() {
        int redBirds = 0;
        for (BirdState bird : birds) {
            if (bird.getName().equals("Red Bird")) {
                redBirds++;
            }
        }
        return redBirds;
    }

    public int getYellowBirds() {
        int yellowBirds = 0;
        for (BirdState bird : birds) {
            if (bird.getName().equals("Yellow Bird")) {
                yellowBirds++;
            }
        }
        return yellowBirds;
    }

    public int getBlueBirds() {
        int blueBirds = 0;
        for (BirdState bird : birds) {
            if (bird.getName().equals("Blue Bird")) {
                blueBirds++;
            }
        }
        return blueBirds;
    }

    public int getNorPigs() {
        int norPigs = 0;
        for (PigState pig : pigs) {
            if (pig.getType() == 1) {
                norPigs++;
            }
        }
        return norPigs;
    }

    public int getKingPigs() {
        int kingPigs = 0;
        for (PigState pig : pigs) {
            if (pig.getType() == 3) {
                kingPigs++;
            }
        }
        return kingPigs;
    }

    public int getHelmetPigs() {
        int helmetPigs = 0;
        for (PigState pig : pigs) {
            if (pig.getType() == 2) {
                helmetPigs++;
            }
        }
        return helmetPigs;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean isLoadedFirstTime() {
        return isLoadedFirstTime;
    }

    public void setLoadedFirstTime(boolean isLoadedFirstTime) {
        this.isLoadedFirstTime = isLoadedFirstTime;
    }
}
