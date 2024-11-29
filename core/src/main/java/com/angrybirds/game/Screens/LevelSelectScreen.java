package com.angrybirds.game.Screens;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Screens.Levels.Level;
import com.angrybirds.game.Screens.Levels.Level1;
import com.angrybirds.game.Screens.Levels.Level2;
import com.angrybirds.game.Screens.Levels.Level3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * Screen for selecting levels in the Angry Birds game.
 */
public class LevelSelectScreen implements Screen {
    private final AngryBirds game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture moreUiTexture, uiTexture;
    private Stage stage;
    private Skin skin;
    private ImageButton level1Button, level2Button, level3Button, homeButton;

    /**
     * Constructor for LevelSelectScreen.
     * Initializes the screen with buttons for each level and a home button.
     *
     * @param game The main game instance.
     */
    public LevelSelectScreen(AngryBirds game) {
        this.game = game;
        moreUiTexture = new Texture(Gdx.files.internal("SpriteSheet/MoreUI.png"));
        uiTexture = new Texture(Gdx.files.internal("SpriteSheet/UI.png"));
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT, gameCam);
        stage = new Stage(gamePort, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Register a TextButtonStyle
        BitmapFont font = new BitmapFont();
        skin.add("default", font);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.font.getData().setScale(2f);
        skin.add("default", textButtonStyle);

        // Home button
        TextureRegion homeButtonRegion = new TextureRegion(uiTexture, 108, 1419, 95, 95);
        skin.add("homeButton", homeButtonRegion);
        ImageButton.ImageButtonStyle homeStyle = new ImageButton.ImageButtonStyle();
        homeStyle.imageUp = skin.getDrawable("homeButton");
        homeButton = new ImageButton(homeStyle);
        homeButton.setSize(125, 125);
        homeButton.setPosition(20, AngryBirds.V_HEIGHT - 120);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

        // Level 1 button
        TextureRegion level1Region = new TextureRegion(moreUiTexture, 777, 593, 62, 77);
        TextureRegion levelLockedRegion = new TextureRegion(moreUiTexture, 956, 122, 64, 64);
        skin.add("level1Button", level1Region);
        ImageButton.ImageButtonStyle style1 = new ImageButton.ImageButtonStyle();
        style1.imageUp = skin.getDrawable("level1Button");
        level1Button = new ImageButton(style1);
        level1Button.setSize(120, 154); // 2x scaling
        level1Button.getImage().setScale(2f); // 2x scaling
        level1Button.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.4f);
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level1(game, gameCam, gamePort, moreUiTexture));
            }
        });

        // Level 2 button
        TextureRegion level2Region;
        if (AngryBirds.currentLevel >= 2) {
            level2Region = new TextureRegion(moreUiTexture, 777, 593, 62, 77);
        } else {
            level2Region = levelLockedRegion;
        }
        //level2Region = new TextureRegion(moreUiTexture, 777, 593, 60, 77);
        skin.add("level2Button", level2Region);
        ImageButton.ImageButtonStyle style2 = new ImageButton.ImageButtonStyle();
        style2.imageUp = skin.getDrawable("level2Button");
        level2Button = new ImageButton(style2);
        level2Button.setSize(120, 154); // 2x scaling
        level2Button.getImage().setScale(2f); // 2x scaling
        level2Button.setPosition(AngryBirds.V_WIDTH * 0.4f, AngryBirds.V_HEIGHT * 0.4f);
        level2Button.setDisabled(true); // Initially disabled
        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (AngryBirds.currentLevel >= 2) {
                    game.setScreen(new Level2(game, gameCam, gamePort, moreUiTexture));
                }
                //game.setScreen(new Level2(game, gameCam, gamePort, moreUiTexture));
            }
        });

        // Level 3 button
        TextureRegion level3Region;
        if (AngryBirds.currentLevel >= 3) {
            level3Region = new TextureRegion(moreUiTexture, 777, 593, 62, 77);
        } else {
            level3Region = levelLockedRegion;
        }
        skin.add("level3Button", level3Region);
        ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle();
        style3.imageUp = skin.getDrawable("level3Button");
        level3Button = new ImageButton(style3);
        level3Button.setSize(120, 154); // 2x scaling
        level3Button.getImage().setScale(2f); // 2x scaling
        level3Button.setPosition(AngryBirds.V_WIDTH * 0.7f, AngryBirds.V_HEIGHT * 0.4f);
        level3Button.setDisabled(true); // Initially disabled
        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (AngryBirds.currentLevel >= 3) {
                    game.setScreen(new Level3(game, gameCam, gamePort, moreUiTexture));
                }
                //game.setScreen(new Level3(game, gameCam, gamePort, moreUiTexture));
            }
        });

        // Load Menu button
        TextButton loadMenuButton = new TextButton("Load Game", skin);
        loadMenuButton.setSize(150, 50);
        loadMenuButton.setPosition(AngryBirds.V_WIDTH - 160, 10);
        loadMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadMenu(game));
            }
        });
        stage.addActor(loadMenuButton);

        // Load custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/angrybirds.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 100; // Set the desired font size
        parameter.borderColor = Color.BLACK; // Set the outline color
        parameter.borderWidth = 2; // Set the outline width
        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        Label level1Label = new Label("1", labelStyle);
        level1Label.setPosition(level1Button.getX() + level1Button.getWidth() * 0.55f, level1Button.getY() + level1Button.getHeight() * 0.47f);

        // Level 2 Label
        Label level2Label = new Label("2", labelStyle);
        if (AngryBirds.currentLevel >= 2) {
            level2Label.setPosition(level2Button.getX() + level2Button.getWidth() * 0.55f, level2Button.getY() + level2Button.getHeight() * 0.47f);
        } else {
            level2Label.setPosition(AngryBirds.V_WIDTH*100f, AngryBirds.V_HEIGHT*100f); //Anything outside the screen
        }
        // Level 3 Label
        Label level3Label = new Label("3", labelStyle);
        if (AngryBirds.currentLevel >= 3) {
            level3Label.setPosition(level3Button.getX() + level3Button.getWidth() * 0.55f, level3Button.getY() + level3Button.getHeight() * 0.47f);
        } else {
            level3Label.setPosition(AngryBirds.V_WIDTH*100f, AngryBirds.V_HEIGHT*100f); //Anything outside the screen
        }
        //level3Label.setPosition(level3Button.getX() + level3Button.getWidth() * 0.55f, level3Button.getY() + level3Button.getHeight() * 0.47f);

        // Title Label
        Label titleLabel = new Label("Select Level", labelStyle);
        titleLabel.setPosition(AngryBirds.V_WIDTH * 0.35f, AngryBirds.V_HEIGHT * 0.85f);

        stage.addActor(homeButton);
        stage.addActor(level1Button);
        stage.addActor(level1Label);
        stage.addActor(level2Button);
        stage.addActor(level2Label);
        stage.addActor(level3Button);
        stage.addActor(level3Label);
        stage.addActor(titleLabel);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        TextureRegion background = new TextureRegion(moreUiTexture, 1027, 2, 511, 205);
        game.batch.draw(background, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        moreUiTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
