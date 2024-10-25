package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Objects.Materials.Cube;
import com.angrybirds.game.Objects.Materials.Triangle;
import com.angrybirds.game.Objects.Pig;
import com.angrybirds.game.Objects.RedBird;
import com.angrybirds.game.Objects.Slingshot;
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

public class Level1 extends Level {
    private Stage stage;
    private Texture angryBirdSheet, uiTexture, moreUITexture;
    private Skin skin;
    private Pig pig1;
    private Pig pig2;
    private Texture birdSheet;
    private Texture blockSheet;
    private RedBird redBird1;
    private RedBird redBird2;
    private Slingshot slingshot;
    private boolean isPaused = false;
    private TextureRegion pausePopUp;
    private Label pauseLabel, levelClearedLabel, levelFailedLabel;
    private ImageButton resumeButton, homeButton, skipButton, skipButton2, greenButton, redButton;

    public Level1(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
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
        slingshot = new Slingshot(blockSheet);
        angryBirdSheet = new Texture(Gdx.files.internal("SpriteSheet/AngryBirds.png"));
        pig2 = new Pig(angryBirdSheet, 2843, 7, 103, 103);
        pig1 = new Pig(angryBirdSheet, 2843, 7, 103, 103);

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
        Label titleLabel = new Label("Level 1", labelStyle);
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

        skin.add("skipButton2", skipButtonRegion);
        ImageButton.ImageButtonStyle skipStyle2 = new ImageButton.ImageButtonStyle();
        skipStyle2.imageUp = skin.getDrawable("skipButton2");
        skipButton2 = new ImageButton(skipStyle2);
        skipButton2.setSize(125, 125);
        skipButton2.setPosition((AngryBirds.V_WIDTH - levelClearedPopUp.getRegionWidth()) / 2, (AngryBirds.V_HEIGHT - levelClearedPopUp.getRegionHeight()) / 2 + 50);
        skipButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        // Labels for level clear level fail
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(); // Use default font
        labelStyle2.font = new BitmapFont();
        // Increase font size
        labelStyle2.font.getData().setScale(3);
        levelClearedLabel = new Label("Level Cleared", labelStyle2);
        levelClearedLabel.setPosition((AngryBirds.V_WIDTH - levelClearedPopUp.getRegionWidth()) / 2 - 60 , (AngryBirds.V_HEIGHT - levelClearedPopUp.getRegionHeight()) / 2 + 200);

        levelFailedLabel = new Label("Level Failed", labelStyle2);
        levelFailedLabel.setPosition((AngryBirds.V_WIDTH - levelFailedPopUp.getRegionWidth()) / 2 - 60, (AngryBirds.V_HEIGHT - levelFailedPopUp.getRegionHeight()) / 2 + 200);



        // Add dummy buttons
        addDummyButtons();
    }

    private void addDummyButtons() {
        // Green button for level cleared
        TextureRegion greenButtonRegion = new TextureRegion(uiTexture, 332, 696, 95, 95); // Adjust coordinates as needed
        skin.add("greenButton", greenButtonRegion);
        ImageButton.ImageButtonStyle greenStyle = new ImageButton.ImageButtonStyle();
        greenStyle.imageUp = skin.getDrawable("greenButton");
        greenButton = new ImageButton(greenStyle);
        greenButton.setSize(50, 50);
        greenButton.setPosition(AngryBirds.V_WIDTH - 130, AngryBirds.V_HEIGHT - 60);
        greenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isLevelCleared = true;
            }
        });

        // Red button for level failed
        TextureRegion redButtonRegion = new TextureRegion(uiTexture, 114, 1032, 92, 86);  // Adjust coordinates as needed
        skin.add("redButton", redButtonRegion);
        ImageButton.ImageButtonStyle redStyle = new ImageButton.ImageButtonStyle();
        redStyle.imageUp = skin.getDrawable("redButton");
        redButton = new ImageButton(redStyle);
        redButton.setSize(50, 50);
        redButton.setPosition(AngryBirds.V_WIDTH - 60, AngryBirds.V_HEIGHT - 60);
        redButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isLevelFailed = true;
            }
        });

        stage.addActor(greenButton);
        stage.addActor(redButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        ArrayList<Cube> cubes = new ArrayList<Cube>();
        // For loop to create 5 cube objects
        for (int i = 0; i < 5; i++) {
            Cube cube = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
            cube.setSize(60, 60);
            cubes.add(cube);
        }
        Triangle wood_triangle = new Triangle("Wood Triangle", 100, angryBirdSheet, 887, 776, 84, 84);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);

        // Level Designing
        for (int i = 0; i < 2; i++) {
            cubes.get(i).setPosition(AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.139f + (i * 0.081f * AngryBirds.V_HEIGHT));
            cubes.get(i).draw(game.batch);
        }
        for (int i = 2; i < 5; i++) {
            cubes.get(i).setPosition(AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.139f + ((i - 2) * 0.081f * AngryBirds.V_HEIGHT));
            cubes.get(i).draw(game.batch);
        }
        wood_triangle.setPosition(AngryBirds.V_WIDTH * 0.6f, AngryBirds.V_HEIGHT * 0.139f);
        wood_triangle.setSize(60, 60);
        wood_triangle.draw(game.batch);

        // Sprites
        redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.13f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.setSize(60, 60);
        pig2.setSize(60, 60);
        pig1.setPosition(AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.3f);
        pig2.setPosition(AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.38f);
        pig1.draw(game.batch);
        pig2.draw(game.batch);
        redBird1.draw(game.batch);
        redBird2.draw(game.batch);
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
        renderPopUps();
        if (isLevelCleared) { // Add button to go to next level in level clear pop up
            stage.addActor(levelClearedLabel);
            stage.addActor(skipButton2);
        } else if (isLevelFailed) { // Add button to retry level in level fail pop up
            stage.addActor(levelFailedLabel);
            stage.addActor(skipButton2);
        } else {
            levelClearedLabel.remove();
            levelFailedLabel.remove();
            skipButton2.remove();
        }

        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {}

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
