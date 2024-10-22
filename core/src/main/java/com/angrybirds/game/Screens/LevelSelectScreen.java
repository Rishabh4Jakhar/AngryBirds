package com.angrybirds.game.Screens;

import com.angrybirds.game.AngryBirds;
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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class LevelSelectScreen implements Screen {
    private final AngryBirds game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture moreUiTexture;
    private Stage stage;
    private Skin skin;
    private ImageButton level1Button;
    private ImageButton level2Button;
    private ImageButton level3Button;

    // Flags to track level completion


    private boolean isLevel1Completed = false;
    private boolean isLevel2Completed = false;

    public LevelSelectScreen(AngryBirds game) {
        this.game = game;
        moreUiTexture = new Texture(Gdx.files.internal("SpriteSheet/MoreUI.png"));
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT, gameCam);
        stage = new Stage(gamePort, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Create a NinePatchDrawable for the button outline
        Texture outlineTexture = new Texture(Gdx.files.internal("SpriteSheet/outline.png"));
        //NinePatch outlinePatch = new NinePatch(outlineTexture, 1, 1, 1, 1);
        //NinePatchDrawable outlineDrawable = new NinePatchDrawable(outlinePatch);

        // Level 1 button
        TextureRegion level1Region = new TextureRegion(moreUiTexture, 777, 593, 60, 77);
        skin.add("level1Button", level1Region);
        ImageButton.ImageButtonStyle style1 = new ImageButton.ImageButtonStyle();
        style1.imageUp = skin.getDrawable("level1Button");
        //Texture outlineTexture = new Texture(Gdx.files.internal("SpriteSheet/outline.png"));
        //NinePatch outlinePatch = new NinePatch(level1Region, 1, 1, 1, 1);
        //NinePatchDrawable outlineDrawable = new NinePatchDrawable(outlinePatch);

        //style1.up = outlineDrawable; // Set the outline as the background
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
        TextureRegion level2Region = new TextureRegion(moreUiTexture, 777, 593, 60, 77);
        skin.add("level2Button", level2Region);
        ImageButton.ImageButtonStyle style2 = new ImageButton.ImageButtonStyle();
        style2.imageUp = skin.getDrawable("level2Button");
        //style2.up = outlineDrawable; // Set the outline as the background
        level2Button = new ImageButton(style2);
        level2Button.setSize(120, 154); // 2x scaling
        level2Button.getImage().setScale(2f); // 2x scaling
        level2Button.setPosition(AngryBirds.V_WIDTH * 0.4f, AngryBirds.V_HEIGHT * 0.4f);
        level2Button.setDisabled(true); // Initially disabled
        //level2Button.getImage().setColor(Color.GRAY); // Greyed out
        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isLevel1Completed) {
                    game.setScreen(new Level2(game, gameCam, gamePort, moreUiTexture));
                }
            }
        });

        // Level 3 button
        TextureRegion level3Region = new TextureRegion(moreUiTexture, 777, 593, 60, 77);
        skin.add("level3Button", level3Region);
        ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle();
        style3.imageUp = skin.getDrawable("level3Button");
        //style3.up = outlineDrawable; // Set the outline as the background
        level3Button = new ImageButton(style3);
        level3Button.setSize(120, 154); // 2x scaling
        level3Button.getImage().setScale(2f); // 2x scaling
        level3Button.setPosition(AngryBirds.V_WIDTH * 0.7f, AngryBirds.V_HEIGHT * 0.4f);
        level3Button.setDisabled(true); // Initially disabled
        //level3Button.getImage().setColor(Color.GRAY); // Greyed out
        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isLevel2Completed) {
                    game.setScreen(new Level3(game, gameCam, gamePort, moreUiTexture));
                }
            }
        });

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
        level2Label.setPosition(level2Button.getX() + level2Button.getWidth() * 0.55f, level2Button.getY() + level2Button.getHeight() * 0.47f);

        // Level 3 Label
        Label level3Label = new Label("3", labelStyle);
        level3Label.setPosition(level3Button.getX() + level3Button.getWidth() * 0.55f, level3Button.getY() + level3Button.getHeight() * 0.47f);

        // Title Label
        Label titleLabel = new Label("Select Level", labelStyle);
        titleLabel.setPosition(AngryBirds.V_WIDTH * 0.35f, AngryBirds.V_HEIGHT * 0.85f);

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
