package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Objects.Materials.Rectangle;
import com.angrybirds.game.Objects.Materials.Sphere;
import com.angrybirds.game.Objects.Pig;
import com.angrybirds.game.Objects.RedBird;
import com.angrybirds.game.Objects.Slingshot;
import com.angrybirds.game.Objects.YellowBird;
import com.angrybirds.game.Screens.LevelSelectScreen;
import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Level3 extends Level {
    private Stage stage;
    private Texture angryBirdSheet, uiTexture, moreUITexture;
    private Pig pig1, pig2, pig3, pig4, pig5;
    private Texture birdSheet;
    private Texture blockSheet;
    private RedBird redBird1, redBird2;
    private YellowBird yellowBird;
    private Slingshot slingshot;
    private Skin skin;
    private boolean isPaused = false;
    private TextureRegion pausePopUp;
    private Label pauseLabel;
    private ImageButton resumeButton, homeButton, skipButton;
    public Level3(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        super(game, gameCam, gamePort, background);
        stage = new Stage(gamePort, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        uiTexture = new Texture(Gdx.files.internal("SpriteSheet/UI.png"));
        moreUITexture = new Texture(Gdx.files.internal("SpriteSheet/moreUI.png"));
        pausePopUp = new TextureRegion(moreUITexture, 1039, 215, 680, 385);

        TextureRegion pauseButtonRegion = new TextureRegion(uiTexture, 547, 693, 97, 107);
        skin.add("pauseButton", pauseButtonRegion);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = skin.getDrawable("pauseButton");
        ImageButton pauseButton = new ImageButton(style);
        pauseButton.setSize(50, 60);
        pauseButton.getImage().setScale(1.25f);
        pauseButton.setPosition(10, AngryBirds.V_HEIGHT - 75);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = !isPaused;
            }
        });

        stage.addActor(pauseButton);
        birdSheet = new Texture(Gdx.files.internal("SpriteSheet/Birdsheet.png"));
        blockSheet = new Texture(Gdx.files.internal("SpriteSheet/Blocksheet.png"));
        redBird1 = new RedBird(birdSheet);
        redBird2 = new RedBird(birdSheet);
        yellowBird = new YellowBird(birdSheet);
        slingshot = new Slingshot(blockSheet);
        angryBirdSheet = new Texture(Gdx.files.internal("SpriteSheet/AngryBirds.png"));
        pig1 = new Pig(angryBirdSheet, 2843, 8, 103, 103);
        pig2 = new Pig(angryBirdSheet, 2843, 8, 103, 103);
        pig3 = new Pig(angryBirdSheet, 2843, 708, 107, 93);
        pig4 = new Pig(angryBirdSheet, 2843, 708, 107, 93);
        pig5 = new Pig(moreUITexture, 643, 312, 120, 126);

        // Load custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/angrybirds.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100; // Set the desired font size
        parameter.borderColor = Color.BLACK; // Set the outline color
        parameter.borderWidth = 2; // Set the outline width
        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        Label titleLabel = new Label("Level 3", labelStyle);
        titleLabel.setPosition(AngryBirds.V_WIDTH*0.35f, AngryBirds.V_HEIGHT*0.85f);
        stage.addActor(titleLabel);



        // Create pause label
        pauseLabel = new Label("Game Paused", labelStyle);
        pauseLabel.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 100, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 250);

        // Create buttons
        TextureRegion resumeButtonRegion = new TextureRegion(uiTexture, 443, 920, 100, 100);
        skin.add("resumeButton", resumeButtonRegion);
        ImageButton.ImageButtonStyle resumeStyle = new ImageButton.ImageButtonStyle();
        resumeStyle.imageUp = skin.getDrawable("resumeButton");
        resumeButton = new ImageButton(resumeStyle);
        resumeButton.setSize(125, 125);
        resumeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 250, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
        //resumeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 50, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
            }
        });

        TextureRegion homeButtonRegion = new TextureRegion(uiTexture, 108, 1419, 95, 95);
        skin.add("homeButton", homeButtonRegion);
        ImageButton.ImageButtonStyle homeStyle = new ImageButton.ImageButtonStyle();
        homeStyle.imageUp = skin.getDrawable("homeButton");
        homeButton = new ImageButton(homeStyle);
        homeButton.setSize(125, 125);
        //homeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 250, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
        homeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 50, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

        TextureRegion skipButtonRegion = new TextureRegion(uiTexture, 341, 580, 100, 100);
        skin.add("skipButton", skipButtonRegion);
        ImageButton.ImageButtonStyle skipStyle = new ImageButton.ImageButtonStyle();
        skipStyle.imageUp = skin.getDrawable("skipButton");
        skipButton = new ImageButton(skipStyle);
        skipButton.setSize(125, 125);
        skipButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 450, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        ArrayList<Rectangle> rods = new ArrayList<Rectangle>();

        //15 rods (7 wood + 5 ice + 3 stone)
        for (int i = 0; i < 7; i++) {
            Rectangle rod = new Rectangle("Wood Rod", 100, angryBirdSheet, 1138, 895, 205, 21);
            rods.add(rod); //indices 0-6
        }
        for (int i = 0; i < 5; i++) {
            Rectangle rod = new Rectangle("Ice Rod", 150, angryBirdSheet, 1090, 1344, 205, 21);
            rods.add(rod); //indices 7-11
        }
        for (int i = 0; i < 3; i++) {
            Rectangle rod = new Rectangle("Stone Rod", 200, angryBirdSheet, 1098, 1739, 205, 21);
            rods.add(rod); //indices 12-14
        }
        Sphere stone_ball1 = new Sphere("Stone Sphere", 200, angryBirdSheet, 975, 1703, 78, 78);
        Sphere stone_ball2 = new Sphere("Stone Sphere", 200, angryBirdSheet, 975, 1703, 78, 78);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);
        TextureRegion tnt = new TextureRegion(angryBirdSheet, 472, 901, 71, 68);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        game.batch.draw(tnt, AngryBirds.V_WIDTH * 0.68f, AngryBirds.V_HEIGHT * 0.139f, 60, 60);
        game.batch.draw(stone_ball1, AngryBirds.V_WIDTH * 0.535f, AngryBirds.V_HEIGHT * 0.367f, 60, 60);
        game.batch.draw(stone_ball2, AngryBirds.V_WIDTH * 0.823f, AngryBirds.V_HEIGHT * 0.367f, 60, 60);

        //support rods (6)
        for (int i = 0; i < 6; i++) {
            if (i<3) {
                Rectangle rod = rods.get(i); //rods with indices 0,1,2
                rod.setSize(AngryBirds.V_WIDTH * 0.139f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.49f + 0.1375f*i), AngryBirds.V_HEIGHT * 0.342f);
                rod.draw(game.batch);
            }
            else if (i<5) {
                Rectangle rod = rods.get(i+4); //rods with indices 7,8
                rod.setSize(AngryBirds.V_WIDTH *  0.122f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.503f + 0.121f * (4-i)) + 100, AngryBirds.V_HEIGHT * 0.342f + 150);
                rod.draw(game.batch);
            }
            else {
                Rectangle rod = rods.get(i + 7); //rod with index 12
                rod.setSize(AngryBirds.V_WIDTH * 0.122f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.503f + 0.06f) + 100, AngryBirds.V_HEIGHT * 0.342f + 301);
                rod.draw(game.batch);
            }
        }

        //Vertical rods(4+3+2=9)
        //Stone rods vertical(4)
        for (int i = 3; i < 7; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(147, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.815f - 0.132f * (i-3)), AngryBirds.V_HEIGHT * 0.494f - 164);
            rod.setRotation(90);
            rod.draw(game.batch);
        }

        //Ice rods vertical(3)
        for (int i = 9; i < 12; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(133, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.735f - 0.1135f * (i-9)), AngryBirds.V_HEIGHT * 0.494f);
            rod.setRotation(90);
            rod.draw(game.batch);
        }

        //Wood rods vertical(2)
        for (int i = 13; i < 15; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(134, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.675f - 0.106f * (i-13)), AngryBirds.V_HEIGHT * 0.494f + 150);
            rod.setRotation(90);
            rod.draw(game.batch);
        }

        // Sprites
        redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        yellowBird.setPosition(AngryBirds.V_WIDTH * 0.15f, AngryBirds.V_HEIGHT * 0.137f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.2f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.setSize(60, 60);
        pig2.setSize(60, 60);
        pig3.setSize(80, 80);
        pig4.setSize(80, 80);
        pig5.setSize(100, 100);
        pig1.setPosition(AngryBirds.V_WIDTH * 0.54f, AngryBirds.V_HEIGHT * 0.139f);
        pig2.setPosition(AngryBirds.V_WIDTH * 0.8f, AngryBirds.V_HEIGHT * 0.139f);
        pig3.setPosition(AngryBirds.V_WIDTH * 0.62f, AngryBirds.V_HEIGHT * 0.365f);
        pig4.setPosition(AngryBirds.V_WIDTH * 0.735f, AngryBirds.V_HEIGHT * 0.365f);
        pig5.setPosition(AngryBirds.V_WIDTH * 0.666f, AngryBirds.V_HEIGHT * 0.577f);
        pig1.draw(game.batch);
        pig2.draw(game.batch);
        pig3.draw(game.batch);
        pig4.draw(game.batch);
        pig5.draw(game.batch);
        redBird1.draw(game.batch);
        redBird2.draw(game.batch);
        yellowBird.draw(game.batch);
        slingshot.draw(game.batch);

        if (isPaused) {
            game.batch.draw(pausePopUp, (AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2);
            stage.addActor(pauseLabel);
            stage.addActor(resumeButton);
            stage.addActor(homeButton);
            stage.addActor(skipButton);
        } else {
            pauseLabel.remove();
            resumeButton.remove();
            homeButton.remove();
            skipButton.remove();
        }

        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        uiTexture.dispose();
        moreUITexture.dispose();
    }
}


