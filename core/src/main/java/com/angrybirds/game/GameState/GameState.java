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

    // Score
    private int score;

    // Birds, pigs, and blocks
    private ArrayList<BirdState> birds;
    private ArrayList<PigState> pigs;
    private ArrayList<MaterialState> blocks;

    // Bird state
    private BirdState currentBird;

    public GameState(int level, ArrayList<BirdState> birds, ArrayList<PigState> pigs, ArrayList<MaterialState> blocks, int score, BirdState currentBird) {
        this.level = level;
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
}
