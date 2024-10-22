package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Level implements Screen {
    protected AngryBirds game;
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Texture background;
    protected Stage stage;

    public Level(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        this.game = game;
        this.gameCam = gameCam;
        this.gamePort = gamePort;
        this.background = background;
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
