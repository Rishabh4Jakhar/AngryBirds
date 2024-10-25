package com.angrybirds.game.Screens;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Screen for loading saved games in the Angry Birds game.
 * Currently only displays dummy saved games and a back button and no background.
 */
public class LoadMenu implements Screen {
    private final AngryBirds game;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    /**
     * Constructor for LoadMenu.
     * Initializes the screen with buttons for saved games and a back button.
     *
     * @param game The main game instance.
     */
    public LoadMenu(AngryBirds game) {
        this.game = game;
        viewport = new StretchViewport(AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create table for saved games
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add dummy saved games
        for (int i = 1; i <= 5; i++) {
            TextButton savedGameButton = new TextButton("Saved Game " + i, skin);
            table.add(savedGameButton).pad(10);
            table.row();
        }

        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        table.add(backButton).pad(10).colspan(2);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        skin.dispose();
    }
}
