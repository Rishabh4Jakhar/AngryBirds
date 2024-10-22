package com.angrybirds.game;

import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AngryBirds extends Game {
    public SpriteBatch batch;
    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;

    // Dimensions of various sprites
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
