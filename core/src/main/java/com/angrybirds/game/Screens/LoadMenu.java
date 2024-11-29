package com.angrybirds.game.Screens;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.GameState.GameState;
import com.angrybirds.game.Screens.Levels.Level1;
import com.angrybirds.game.Screens.Levels.Level2;
import com.angrybirds.game.Screens.Levels.Level3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadMenu implements Screen {
    private final AngryBirds game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture moreUiTexture;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    public LoadMenu(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture moreUiTexture) {
        this.game = game;
        this.gameCam = gameCam;
        this.gamePort = gamePort;
        this.moreUiTexture = moreUiTexture;
        viewport = new StretchViewport(AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create table for save files
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // List available save files
        File saveDir = new File("."); // Current directory
        File[] saveFiles = saveDir.listFiles((dir, name) -> name.matches("save_game_\\d+\\.dat"));

        if (saveFiles != null && saveFiles.length > 0) {
            for (File saveFile : saveFiles) {
                String fileName = saveFile.getName();
                TextButton saveButton = new TextButton(fileName, skin);

                saveButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        loadGame(fileName); // Load the selected save file
                    }
                });

                table.add(saveButton).pad(10);
                table.row();
            }
        } else {
            // Display message if no save files found
            TextButton noSavesButton = new TextButton("No Saves Found", skin);
            noSavesButton.setDisabled(true);
            table.add(noSavesButton).pad(10).row();
        }

        // Reset button
        TextButton resetButton = new TextButton("Reset Game", skin);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetGame(); // Reset game progress
            }
        });
        table.add(resetButton).pad(10).row();

        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        table.add(backButton).pad(10);
    }

    private void loadGame(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            GameState gameSaveState = (GameState) ois.readObject();
            System.out.println("Loaded game from: " + fileName);

            // Restore game state
            AngryBirds.currentLevel = gameSaveState.getLevel();
            AngryBirds.nowLevel = gameSaveState.getCurrentLevel();
            AngryBirds.isLoadedFirstTime = true;
            System.out.println("Loaded level: " + AngryBirds.currentLevel);
            loadGameState(gameSaveState);
            //game.setScreen(new LevelSelectScreen(game)); // Return to level select after loading
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
    }

    private void loadGameState(GameState gameSaveState) {
        // Check which level and set screen to that level
        AngryBirds.noOfBirdsInLevel = gameSaveState.getNoOfBirds();
        AngryBirds.noOfPigsInLevel = gameSaveState.getNoOfPigs();
        AngryBirds.redBirdsLeft = gameSaveState.getRedBirds();
        AngryBirds.yellowBirdsLeft = gameSaveState.getYellowBirds();
        AngryBirds.blueBirdsLeft = gameSaveState.getBlueBirds();
        AngryBirds.norPigsLeft = gameSaveState.getNorPigs();
        AngryBirds.kingPigsLeft = gameSaveState.getKingPigs();
        AngryBirds.helmetPigsLeft = gameSaveState.getHelmetPigs();
        if (AngryBirds.nowLevel == 1) {
            AngryBirds.nowLevel = 1;
            game.setScreen(new Level1(game, gameCam, gamePort, moreUiTexture));
        } else if (AngryBirds.nowLevel == 2) {
            AngryBirds.nowLevel = 2;
            game.setScreen(new Level2(game, gameCam, gamePort, moreUiTexture));
        } else if (AngryBirds.nowLevel == 3) {
            AngryBirds.nowLevel = 3;
            game.setScreen(new Level3(game, gameCam, gamePort, moreUiTexture));
        }

    }

    private void resetGame() {
        AngryBirds.currentLevel = 1; // Reset progress
        AngryBirds.nowLevel = 1;
        AngryBirds.noOfBirdsInLevel = 2;
        AngryBirds.noOfPigsInLevel = 2;
        AngryBirds.redBirdsLeft = 2;
        AngryBirds.yellowBirdsLeft = 0;
        AngryBirds.blueBirdsLeft = 0;
        AngryBirds.norPigsLeft = 2;
        AngryBirds.kingPigsLeft = 0;
        AngryBirds.helmetPigsLeft = 0;
        game.setScreen(new LevelSelectScreen(game)); // Return to level select
        System.out.println("Game reset to initial state.");
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
