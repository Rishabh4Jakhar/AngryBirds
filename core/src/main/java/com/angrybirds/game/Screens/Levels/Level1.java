package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Objects.Bird;
import com.angrybirds.game.Objects.Materials.Cube;
import com.angrybirds.game.Objects.Materials.Triangle;
import com.angrybirds.game.Objects.Pig;
import com.angrybirds.game.Objects.RedBird;
import com.angrybirds.game.Objects.Slingshot;
import com.angrybirds.game.Screens.LevelSelectScreen;
import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

    // For loop to create 5 cube objects

   //Box2D
    private Bird currentBird;
    private Vector2 dragStart, dragEnd;
    private OrthographicCamera b2dCam;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Cube> cubes;
    private Triangle wood_triangle;

    // Constants
    private static final float SLINGSHOT_X = AngryBirds.V_WIDTH * 15.5f; // 20% from left
    private static final float SLINGSHOT_Y = AngryBirds.V_HEIGHT * 27f; // 25% from bottom

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
        //addDummyButtons();
        shapeRenderer = new ShapeRenderer();
        initializeLevel();
    }

    private void initializeLevel() {

        // Create ground
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);
        body = world.createBody(bdef);

        shape.setAsBox(AngryBirds.V_WIDTH, 1);
        fdef.shape = shape;
        body.createFixture(fdef);

        shape.setAsBox(1, AngryBirds.V_HEIGHT);
        fdef.shape = shape;
        body.createFixture(fdef);

        // Sprite batch rendering for level design is done in render method
        // Now create bodies for the objects (birds, pigs, structures here)
        System.out.println("Slingshot Position (Pixels): " + SLINGSHOT_X + ", " + SLINGSHOT_Y);
        // Create birds
        redBird1.createBody(world, SLINGSHOT_X/PPM, SLINGSHOT_Y/PPM);
        pig1.createBody(world, AngryBirds.V_WIDTH * 0.673f, AngryBirds.V_HEIGHT * 0.34f);
        pig2.createBody(world, AngryBirds.V_WIDTH * 0.743f, AngryBirds.V_HEIGHT * 0.42f);
        currentBird = redBird1;
        cubes = new ArrayList<Cube>();
        for (int i = 0; i < 2; i++) {
            Cube cube = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
            cube.createBody(world, AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.139f + (i * 0.081f * AngryBirds.V_HEIGHT), 57, 57, false);
            cubes.add(cube);
        }
        for (int i = 2; i < 5; i++) {
            Cube cube = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
            cube.createBody(world, AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.139f + ((i - 2) * 0.081f * AngryBirds.V_HEIGHT), 57, 57, false);
            cubes.add(cube);
        }

        // Input processor
        Gdx.input.setInputProcessor(new InputAdapter() {
                                        @Override
                                        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                                            Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY));
                                            System.out.println("Touch Down at: " + touchPoint); // Debug print
                                            float dragAreaRadius = 20f;
                                            if (currentBird != null && !currentBird.isShot() &&
                                                touchPoint.dst(new Vector2(SLINGSHOT_X / PPM, SLINGSHOT_Y / PPM )) < dragAreaRadius) {
                                                currentBird.setSelected(true);
                                                dragStart = touchPoint;
                                            }
                                            return true;
                                        }

                                        @Override
                                        public boolean touchDragged(int screenX, int screenY, int pointer) {
                                            if (currentBird != null && currentBird.isSelected()) {
                                                //System.out.println("Dragging Bird " + dragStart.x + ", " + dragStart.y);
                                                Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY)).scl(1/PPM);
                                                // Limit drag distance
                                                // Optionally add a check to see if the bird is being dragged in the right direction
                                                // If bird is dragged beyond a certain distance, set it to the max distance
                                                float distance = touchPoint.dst(dragStart)/PPM;
                                                System.out.println("Distance: " + distance);
                                                float maxDrag = 2.5f;
                                                if (distance > maxDrag) {
                                                    System.out.println("Dragging beyond max distance " + distance);
                                                    Vector2 direction = touchPoint.sub(dragStart).nor();
                                                    //touchPoint = gamePort.unproject(new Vector2(screenX, screenY)).scl(1/PPM).add(direction);
                                                    touchPoint = new Vector2(dragStart).add(direction.scl(maxDrag)).scl(1/PPM);
                                                    //touchPoint = touchPoint.scl(1/PPM);
                                                    System.out.println("Current Distance: " + distance);
                                                    System.out.println("Clamped Position: " + touchPoint);
                                                }
                                                // Right direction check
                                                currentBird.dragTo(touchPoint.x, touchPoint.y);
                                                System.out.println("Dragging Bird to: " + touchPoint);
                                            }
                                            return true;
                                        }

                                        @Override
                                        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                                            if (currentBird != null && currentBird.isSelected()) {
                                                Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY));
                                                Vector2 dragVector = dragStart.sub(touchPoint);
                                                currentBird.shoot(dragVector);
                                                System.out.println("Shooting Bird with vector: " + dragVector);
                                                // Optionally prepare next bird
                                                // prepareNextBird();
                                            }
                                            return true;
                                        }
                                    }
        );

        // Create camera for debug rendering
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, AngryBirds.V_WIDTH / PPM, AngryBirds.V_HEIGHT / PPM);

    }

    /*
    Made for static GUI buttons
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
    */
    private void renderTrajectory(Vector2 startPosition, Vector2 dragVector) {
        float timeStep = 0.1f; // Time step for simulation (seconds)
        int maxSteps = 30;     // Number of dots to draw

        // Adjust the drag vector to match the scaled force applied during shoot
        Vector2 force = dragVector.scl((2.0f - 0.2f) / PPM);

        // Apply max limit to force as per shoot logic
        if (force.x > 0.95f || force.y > 0.95f) {
            force = force.scl(0.6f);
        }

        // Initial velocity based on force
        Vector2 velocity = new Vector2(force).scl(1 / Bird.getBirdDensity()); // Adjust based on mass/density

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        Vector2 currentPosition = new Vector2(startPosition);

        for (int i = 0; i < maxSteps; i++) {
            // Draw a dot at the current position
            shapeRenderer.circle(currentPosition.x * PPM, currentPosition.y * PPM, 2); // Convert to pixels

            // Update position using basic physics
            currentPosition.add(velocity.x * timeStep, velocity.y * timeStep);
            velocity.add(0, -4f * timeStep); // Apply gravity (adjust if world gravity is different)
        }

        shapeRenderer.end();
    }

    @Override
    public void render(float delta) {

        world.step(1/60f, 6, 2);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // TESTING MIGHT BREAK
        currentBird.update();
        pig1.update();
        pig2.update();

        game.batch.begin();

        wood_triangle = new Triangle("Wood Triangle", 100, angryBirdSheet, 887, 776, 84, 84);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);

        // Level Designing
        for (int i = 0; i < 2; i++) {
            cubes.get(i).setSize(60,60);
            cubes.get(i).setPosition(AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.139f + (i * 0.081f * AngryBirds.V_HEIGHT));
            cubes.get(i).draw(game.batch);
        }
        for (int i = 2; i < 5; i++) {
            cubes.get(i).setSize(60,60);
            cubes.get(i).setPosition(AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.139f + ((i - 2) * 0.081f * AngryBirds.V_HEIGHT));
            cubes.get(i).draw(game.batch);
        }
        wood_triangle.setPosition(AngryBirds.V_WIDTH * 0.6f, AngryBirds.V_HEIGHT * 0.139f);
        wood_triangle.setSize(60, 60);
        wood_triangle.draw(game.batch);


        // Sprites
        //redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.13f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.setSize(60, 60);
        pig2.setSize(60, 60);
        pig1.setPosition(AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.3f);
        pig2.setPosition(AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.38f);
        pig1.draw(game.batch);
        pig2.draw(game.batch);
        //redBird1.draw(game.batch);
        currentBird.draw(game.batch);
        redBird2.draw(game.batch);
        slingshot.draw(game.batch);
        game.batch.end();
        /*
        if (currentBird != null && currentBird.isSelected()) {
            Vector2 startPosition = currentBird.getBody().getPosition(); // Bird's current position in world units
            renderTrajectory(startPosition, dragStart.sub(startPosition)); // Drag vector
        }
        */
        // Draw draggable area outline
        shapeRenderer.setProjectionMatrix(gameCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        // Convert slingshot position to meters
        float slingshotX = SLINGSHOT_X / PPM;
        float slingshotY = SLINGSHOT_Y / PPM;
        float maxDragRadius = 20f; // In meters (adjust as needed)

        // Draw circle for draggable area
        shapeRenderer.circle(slingshotX, slingshotY, maxDragRadius, 100); // 100 segments for a smooth circle

        shapeRenderer.end();


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
        stage.act();
        stage.draw();

        b2dCam.update();
        b2dr.render(world, b2dCam.combined);
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

        world.dispose();
        b2dr.dispose();
    }
}
