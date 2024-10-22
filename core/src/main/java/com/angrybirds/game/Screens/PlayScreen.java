package com.angrybirds.game.Screens;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Objects.BlueBird;
import com.angrybirds.game.Objects.RedBird;
import com.angrybirds.game.Objects.Slingshot;
import com.angrybirds.game.Objects.YellowBird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen {
    private final AngryBirds game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    //private Hud hud;
    private Texture background;
    private Texture birdSheet;
    private Texture pigSheet;
    private Texture blockSheet;
    private Texture uiTexture;
    private RedBird redBird;
    private YellowBird yellowBird;
    private BlueBird blueBird;
    private Slingshot slingshot;
    private Stage stage;
    private Skin skin;
    private ImageButton startButton;


    public PlayScreen(AngryBirds game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT, gameCam);
        //hud = new Hud(game.batch);
        background = new Texture(Gdx.files.internal("SpriteSheet/Background.png"));
        // Convert all the textures to sprites
        birdSheet = new Texture(Gdx.files.internal("SpriteSheet/Birdsheet.png"));
        //birdSprite = new Sprite(birdSheet);
        pigSheet = new Texture(Gdx.files.internal("SpriteSheet/Pigsheet.png"));
        blockSheet = new Texture(Gdx.files.internal("SpriteSheet/Blocksheet.png"));
        redBird = new RedBird(birdSheet);
        yellowBird = new YellowBird(birdSheet);
        blueBird = new BlueBird(birdSheet);
        slingshot = new Slingshot(blockSheet);

        stage = new Stage(gamePort, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        uiTexture = new Texture(Gdx.files.internal("./SpriteSheet/UI.png"));
        TextureRegion startButtonRegion = new TextureRegion(uiTexture, 443, 920, 100, 100);
        skin.add("startButton", startButtonRegion);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = skin.getDrawable("startButton");
        startButton = new ImageButton(style);
        startButton.setSize(125, 125);
        startButton.getImage().setScale(1.25f);
        startButton.setPosition(AngryBirds.V_WIDTH*0.45f, AngryBirds.V_HEIGHT*0.13f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });

        stage.addActor(startButton);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);

        // Draw Angry Birds Logo from uiTexture
        TextureRegion angryBirdsLogo = new TextureRegion(uiTexture, 0, 0, 629, 136);
        game.batch.draw(angryBirdsLogo, AngryBirds.V_WIDTH * 0.25f, AngryBirds.V_HEIGHT * 0.8f);

        TextureRegion textBox = new TextureRegion(uiTexture, 207, 1027, 335, 95);

        game.batch.draw(textBox, AngryBirds.V_WIDTH * 0.35f, AngryBirds.V_HEIGHT * 0.6f);
        // Calculate relative positions
        redBird.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.1f);
        yellowBird.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.1f);
        blueBird.setPosition(AngryBirds.V_WIDTH * 0.17f, AngryBirds.V_HEIGHT * 0.1f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.21f, AngryBirds.V_HEIGHT * 0.1f);


        // Draw sprites
        redBird.draw(game.batch);
        yellowBird.draw(game.batch);
        blueBird.draw(game.batch);
        slingshot.draw(game.batch);

        game.batch.end();

        stage.act();
        stage.draw();
        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        birdSheet.dispose();
        blockSheet.dispose();
        stage.dispose();
        skin.dispose();
    }
}
