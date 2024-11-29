package com.angrybirds.game;

import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class for Angry Birds, extending the libGDX Game class.
 * This class sets up the game window and initializes the first screen.
 */
public class AngryBirds extends Game {
    // SpriteBatch used for drawing 2D textures
    public SpriteBatch batch;
    // Virtual width and height of the game screen
    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;
    public static final float PPM = 100;
    public static int currentLevel = 1;
    public static int nowLevel = 1;
    public static int noOfBirdsInLevel = 2;
    public static int noOfPigsInLevel = 2;
    // Type of birds and pigs left
    public static int redBirdsLeft = 2;
    public static int yellowBirdsLeft = 0;
    public static int blueBirdsLeft = 0;
    public static int norPigsLeft = 2;
    public static int kingPigsLeft = 0;
    public static int helmetPigsLeft = 0;
    public static boolean isLoadedFirstTime = true;

    // Dimensions of various sprites (commented out for now)
    /*
    public static final int RED_BIRD_WIDTH = 45;
    public static final int RED_BIRD_HEIGHT = 44;
    public static final int YELLOW_BIRD_WIDTH = 64;
    public static final int YELLOW_BIRD_HEIGHT = 60;
    public static final int BLUE_BIRD_WIDTH = 33;
    public static final int BLUE_BIRD_HEIGHT = 35;
    public static final int SLINGSHOT_WIDTH = 85;
    public static final int SLINGSHOT_HEIGHT = 200;
    */

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
