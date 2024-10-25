package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Level implements Screen {
    protected final AngryBirds game;
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Texture background;
    protected Stage stage;
    protected Skin skin;
    protected Texture uiTexture;
    protected Texture moreUITexture;
    protected TextureRegion levelClearedPopUp;
    protected TextureRegion levelFailedPopUp;
    protected boolean isLevelCleared = false;
    protected boolean isLevelFailed = false;

    public Level(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        this.game = game;
        this.gameCam = gameCam;
        this.gamePort = gamePort;
        this.background = background;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        uiTexture = new Texture(Gdx.files.internal("SpriteSheet/UI.png"));
        moreUITexture = new Texture(Gdx.files.internal("SpriteSheet/moreUI.png"));
        levelClearedPopUp = new TextureRegion(moreUITexture, 12, 448, 172, 165); // Adjust coordinates as needed
        levelFailedPopUp = new TextureRegion(moreUITexture, 204, 448, 175, 165); // Adjust coordinates as needed

        //addDummyButtons();
    }
    /*
    private void addDummyButtons() {
        // Green button for level cleared
        TextureRegion greenButtonRegion = new TextureRegion(uiTexture, 332, 696, 95, 95); // Adjust coordinates as needed
        skin.add("greenButton", greenButtonRegion);
        ImageButton.ImageButtonStyle greenStyle = new ImageButton.ImageButtonStyle();
        greenStyle.imageUp = skin.getDrawable("greenButton");
        ImageButton greenButton = new ImageButton(greenStyle);
        greenButton.setSize(50, 50);
        greenButton.setPosition(AngryBirds.V_WIDTH - 110, AngryBirds.V_HEIGHT - 60);
        greenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isLevelCleared = true;
            }
        });

        // Red button for level failed
        TextureRegion redButtonRegion = new TextureRegion(uiTexture, 114, 1032, 92, 86); // Adjust coordinates as needed
        skin.add("redButton", redButtonRegion);
        ImageButton.ImageButtonStyle redStyle = new ImageButton.ImageButtonStyle();
        redStyle.imageUp = skin.getDrawable("redButton");
        ImageButton redButton = new ImageButton(redStyle);
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

     */
    protected void renderPopUps() {
        if (isLevelCleared) {
            game.batch.draw(levelClearedPopUp, AngryBirds.V_WIDTH*0.35f, AngryBirds.V_HEIGHT*0.3f, levelClearedPopUp.getRegionWidth()*2f, levelClearedPopUp.getRegionHeight()*2f);
        } else if (isLevelFailed) {
            game.batch.draw(levelFailedPopUp, AngryBirds.V_WIDTH*0.35f, AngryBirds.V_HEIGHT*0.3f, levelFailedPopUp.getRegionWidth()*2f, levelFailedPopUp.getRegionHeight()*2f);
        }
    }
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
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
    public void dispose() {}
}
