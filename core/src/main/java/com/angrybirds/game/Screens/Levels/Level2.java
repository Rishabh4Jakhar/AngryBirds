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

public class Level2 extends Level {
    private Stage stage;
    private Texture angryBirdSheet, uiTexture, moreUITexture;
    private Skin skin;
    private Pig pig1, pig2, pig3;
    private Texture birdSheet;
    private Texture blockSheet;
    private RedBird redBird1, redBird2;
    private YellowBird yellowBird;
    private Slingshot slingshot;
    private boolean isPaused = false;
    private TextureRegion pausePopUp;
    private Label pauseLabel;
    private ImageButton resumeButton, homeButton, skipButton;

    public Level2(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        super(game, gameCam, gamePort, background);
        stage = new Stage(gamePort, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        uiTexture = new Texture(Gdx.files.internal("SpriteSheet/UI.png"));        moreUITexture = new Texture(Gdx.files.internal("SpriteSheet/moreUI.png"));
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
        pig2 = new Pig(angryBirdSheet, 2843, 708, 107, 93);
        pig1 = new Pig(angryBirdSheet, 2843, 7, 103, 103);
        pig3 = new Pig(angryBirdSheet, 2843, 708, 107, 93);
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
        Label titleLabel = new Label("Level 2", labelStyle);
        titleLabel.setPosition(AngryBirds.V_WIDTH * 0.35f, AngryBirds.V_HEIGHT * 0.85f);
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
        // 6 Ice rods
        for (int i = 0; i < 6; i++) {
            Rectangle rod = new Rectangle("Ice Rod", 150, angryBirdSheet, 1090, 1344, 205, 21);
            rods.add(rod);
        }
        // TextureRegion wood_cube = new TextureRegion(angryBirdSheet, 803, 776, 84, 84);
        TextureRegion ice_rod_long = new TextureRegion(angryBirdSheet, 1090, 1344, 205, 21);
        // TextureRegion wood_triangle_with_space = new TextureRegion(angryBirdSheet, 887, 776, 84, 84);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);
        Sphere stone_ball = new Sphere("Stone Sphere", 200, angryBirdSheet, 975, 1703, 78, 78);
        //TextureRegion stone_ball = new TextureRegion(angryBirdSheet, 975, 1703, 78, 78);
        TextureRegion tnt = new TextureRegion(angryBirdSheet, 472, 901, 71, 68);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        //game.batch.draw(stone_ball, AngryBirds.V_WIDTH * 0.64f, AngryBirds.V_HEIGHT * 0.36f, 60, 60);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.138f, 0, 0, 150, ice_rod_long.getRegionHeight(), 1, 1, 90);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.78f, AngryBirds.V_HEIGHT * 0.138f, 0, 0, 150, ice_rod_long.getRegionHeight(), 1, 1, 90);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.633f, AngryBirds.V_HEIGHT * 0.339f, AngryBirds.V_WIDTH * 0.147f, 21);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.78f, AngryBirds.V_HEIGHT * 0.359f, 0, 0, 140, ice_rod_long.getRegionHeight(), 1, 1, 90);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.91f, AngryBirds.V_HEIGHT * 0.136f, 0, 0, 302, ice_rod_long.getRegionHeight(), 1, 1, 90);
        //game.batch.draw(ice_rod_long, AngryBirds.V_WIDTH * 0.763f, AngryBirds.V_HEIGHT * 0.539f, AngryBirds.V_WIDTH * 0.147f, 21);
        game.batch.draw(tnt, AngryBirds.V_WIDTH * 0.68f, AngryBirds.V_HEIGHT * 0.139f, 60, 60);
        //game.batch.draw(wood_triangle_with_space, AngryBirds.V_WIDTH * 0.5f, AngryBirds.V_HEIGHT * 0.139f, 60, 60);

        // Level designing
        for (int i = 0; i < 2; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(150, 21);
            rod.setRotation(90);
            if (i == 0) {
                rod.setPosition(AngryBirds.V_WIDTH * 0.56f, AngryBirds.V_HEIGHT * 0.265f);
            } else {
                rod.setPosition(AngryBirds.V_WIDTH * 0.69f, AngryBirds.V_HEIGHT * 0.265f);
            }
            //rod.setPosition(AngryBirds.V_WIDTH * 0.65f + (i * 0.13f), AngryBirds.V_HEIGHT * 0.138f);
            rod.draw(game.batch);
        }
        Rectangle support_rod = rods.get(2);
        support_rod.setSize(AngryBirds.V_WIDTH*0.147f, 21);
        support_rod.setPosition(AngryBirds.V_WIDTH * 0.633f, AngryBirds.V_HEIGHT * 0.339f);
        support_rod.draw(game.batch);
        Rectangle support_rod2 = rods.get(3);
        support_rod2.setSize(AngryBirds.V_WIDTH*0.147f, 21);
        support_rod2.setPosition(AngryBirds.V_WIDTH * 0.763f, AngryBirds.V_HEIGHT * 0.539f);
        support_rod2.draw(game.batch);
        for (int i = 4; i < 6; i++) {
            Rectangle rod = rods.get(i);
            if (i==4) {
                rod.setSize(130, 21);
                rod.setPosition(AngryBirds.V_WIDTH * 0.69f, AngryBirds.V_HEIGHT * 0.489f);
            } else {
                rod.setSize(292, 21);
                rod.setPosition(AngryBirds.V_WIDTH * 0.816f, AngryBirds.V_HEIGHT * 0.264f);
            }
            rod.setRotation(90);
            rod.draw(game.batch);
        }
        stone_ball.setPosition(AngryBirds.V_WIDTH * 0.64f, AngryBirds.V_HEIGHT * 0.36f);
        stone_ball.setSize(60, 60);
        stone_ball.draw(game.batch);
        // Sprites
        redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        yellowBird.setPosition(AngryBirds.V_WIDTH * 0.15f, AngryBirds.V_HEIGHT * 0.137f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.2f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.setSize(60, 60);
        pig2.setSize(75, 75);
        pig3.setSize(75, 75);
        pig1.setPosition(AngryBirds.V_WIDTH * 0.58f, AngryBirds.V_HEIGHT * 0.139f);
        pig2.setPosition(AngryBirds.V_WIDTH * 0.8f, AngryBirds.V_HEIGHT * 0.559f);
        pig3.setPosition(AngryBirds.V_WIDTH * 0.8f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.draw(game.batch);
        pig2.draw(game.batch);
        pig3.draw(game.batch);
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
