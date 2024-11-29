package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.GameState.BirdState;
import com.angrybirds.game.GameState.GameState;
import com.angrybirds.game.GameState.MaterialState;
import com.angrybirds.game.GameState.PigState;
import com.angrybirds.game.Objects.*;
import com.angrybirds.game.Objects.Materials.Cube;
import com.angrybirds.game.Objects.Materials.Material;
import com.angrybirds.game.Objects.Materials.Triangle;
import com.angrybirds.game.Screens.LevelSelectScreen;
import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    private Label pauseLabel, levelClearedLabel, levelFailedLabel, scoreLabel, highScoreLabel, scoreLabel2;
    private ImageButton resumeButton, homeButton, skipButton, skipButton2, greenButton, redButton;
    private List<Body> bodiesToDestroy = new ArrayList<>();
    private int score = 0;
    // For loop to create 5 cube objects

   //Box2D
    private Bird currentBird;
    private Vector2 dragStart, dragEnd;
    private OrthographicCamera b2dCam;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Cube> cubes;
    private Triangle wood_triangle;
    private Body ground;
    private InputMultiplexer inputMultiplexer;
    private float levelEndTimer = 0; // Tracks elapsed time after the last bird is used
    private boolean waitingForLevelEnd = false; // Indicates if we are waiting for pigs to die

    // Constants
    private static final float SLINGSHOT_X = AngryBirds.V_WIDTH * 15.5f; // 20% from left
    private static final float SLINGSHOT_Y = AngryBirds.V_HEIGHT * 27f; // 25% from bottom

    public Level1(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        super(game, gameCam, gamePort, background);
        stage = new Stage(gamePort, game.batch);
        inputMultiplexer = new InputMultiplexer();
        //Gdx.input.setInputProcessor(stage);
        inputMultiplexer.addProcessor(stage);
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

        TextureRegion replayButtonRegion = new TextureRegion(uiTexture, 9, 580, 100, 100);
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
                //replayLevel();
            }
        });
        // Labels for level clear level fail
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(); // Use default font
        labelStyle2.font = new BitmapFont();
        // Increase font size
        labelStyle2.font.getData().setScale(3);
        levelClearedLabel = new Label("Level Cleared", labelStyle2);
        levelClearedLabel.setPosition((AngryBirds.V_WIDTH - levelClearedPopUp.getRegionWidth()) / 2 - 60 , (AngryBirds.V_HEIGHT - levelClearedPopUp.getRegionHeight()) / 2 + 200);
        scoreLabel = new Label(String.valueOf(score), labelStyle2);
        scoreLabel.setPosition((AngryBirds.V_WIDTH - levelClearedPopUp.getRegionWidth()) / 2 - 72, (AngryBirds.V_HEIGHT - levelClearedPopUp.getRegionHeight()) / 2 - 50);
        levelFailedLabel = new Label("Level Failed", labelStyle2);
        levelFailedLabel.setPosition((AngryBirds.V_WIDTH - levelFailedPopUp.getRegionWidth()) / 2 - 60, (AngryBirds.V_HEIGHT - levelFailedPopUp.getRegionHeight()) / 2 + 200);
        scoreLabel2 = new Label(String.valueOf(score), labelStyle2);
        scoreLabel2.setPosition((AngryBirds.V_WIDTH - levelFailedPopUp.getRegionWidth()) / 2 - 72, (AngryBirds.V_HEIGHT - levelFailedPopUp.getRegionHeight()) / 2 - 50);


        // Add dummy buttons
        //addDummyButtons();
        shapeRenderer = new ShapeRenderer();
        initializeLevel();
        handleLoading();
    }

    private void handleLoading() {
        // Load global variables
        System.out.println("isLoadedFirstTime: " + AngryBirds.isLoadedFirstTime);
        if (!AngryBirds.isLoadedFirstTime) { // False
            return;
        }
        AngryBirds.isLoadedFirstTime = false;
        if (AngryBirds.nowLevel == 1) {
            if (AngryBirds.redBirdsLeft == 0) {
                redBird1.reset();
                redBird2.reset();
            } else if (AngryBirds.redBirdsLeft == 1) {
                redBird2.reset();
                birdBodies.remove(redBird2);
            }
            if (AngryBirds.norPigsLeft == 0) {
                pig1.die(bodiesToDestroy);
                pig2.die(bodiesToDestroy);
            } else if (AngryBirds.norPigsLeft == 1) {
                pig2.die(bodiesToDestroy);
            }
        }
    }

    private void initializeLevel() {

        // Create ground
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        //Body ground;

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);
        ground = world.createBody(bdef);

        shape.setAsBox(AngryBirds.V_WIDTH, 1);
        fdef.shape = shape;
        ground.createFixture(fdef);

        shape.setAsBox(1, AngryBirds.V_HEIGHT);
        fdef.shape = shape;
        ground.createFixture(fdef);
        ground.setUserData("Ground");
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleCollision(contact); // Delegate to a method to process the collision
            }


            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                handleCollisionGround(contact, impulse);
            }
        });


        // Sprite batch rendering for level design is done in render method
        // Now create bodies for the objects (birds, pigs, structures here)
        System.out.println("Slingshot Position (Pixels): " + SLINGSHOT_X + ", " + SLINGSHOT_Y);
        // Create birds
        redBird1.createBody(world, SLINGSHOT_X/PPM, SLINGSHOT_Y/PPM, false);
        redBird2.createBody(world, (SLINGSHOT_X/ PPM)-70, (SLINGSHOT_Y/ PPM)-72, false);
        birdBodies.add(redBird1);
        birdBodies.add(redBird2);
        pig1.createBody(world, AngryBirds.V_WIDTH * 0.673f, AngryBirds.V_HEIGHT * 0.34f);
        pig2.createBody(world, AngryBirds.V_WIDTH * 0.743f, AngryBirds.V_HEIGHT * 0.4f);
        this.pigBodies.add(pig1);
        this.pigBodies.add(pig2);
        currentBird = birdBodies.get(0);
        cubes = new ArrayList<Cube>();
        for (int i = 0; i < 2; i++) {
            Cube cube = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
            cube.createBody(world, AngryBirds.V_WIDTH * 0.65f, AngryBirds.V_HEIGHT * 0.139f + (i * 0.081f * AngryBirds.V_HEIGHT), 57, 57, false);
            cubes.add(cube);
            // Add the cube to the list of block bodies
            blockBodies.add(cube);
        }
        for (int i = 2; i < 5; i++) {
            Cube cube = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
            cube.createBody(world, AngryBirds.V_WIDTH * 0.72f, AngryBirds.V_HEIGHT * 0.139f + ((i - 2) * 0.081f * AngryBirds.V_HEIGHT), 57, 57, false);
            cubes.add(cube);
            // Add the cube to the list of block bodies
            blockBodies.add(cube);
        }
        wood_triangle = new Triangle("Wood Triangle", 100, angryBirdSheet, 887, 776, 84, 84);
        wood_triangle.createBody(world, AngryBirds.V_WIDTH * 0.6f, AngryBirds.V_HEIGHT * 0.139f, 57, 57, false);
        blockBodies.add(wood_triangle);
        //System.out.println("Triangle body created: " + wood_triangle.getBody());
        // Input processor
        InputAdapter keyboardInputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.I) { // Save game
                    saveGame("save_game_" + getNextSaveFileNumber() + ".dat");
                    System.out.println("Game saved.");
                    System.out.println("Birds: " + birdBodies);
                    System.out.println("Pigs: " + pigBodies);
                    System.out.println("Blocks: " + blockBodies);
                    return true; // Event consumed
                }

                if (keycode == Input.Keys.L) { // Load game
                    //loadGame("savefile.dat");
                    System.out.println("Game loaded.");
                    return true; // Event consumed
                }

                return false; // Let other processors handle if not consumed
            }
        };

        InputAdapter birdInputProcessor = new InputAdapter() {
                                        @Override
                                        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                                            Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY));
                                            //System.out.println("Touch Down at: " + touchPoint); // Debug print
                                            // Check if the click is on any bird
                                            for (Bird bird : birdBodies) {
                                                //System.out.println("Bird: " + bird);
                                                //System.out.println("Bird bounds: " + bird.getBounds());
                                                //System.out.println("Touch Point: " + touchPoint);
                                                if (!bird.isDead() && bird != currentBird &&bird.getBounds().contains(touchPoint.x/PPM, touchPoint.y/PPM)) {
                                                    System.out.println("Bird clicked: " + bird);

                                                    // If there is already a bird on the slingshot, return it to the list
                                                    if (currentBird != null && currentBird.getBody() != null) {
                                                        // Check if the currentbird is in birdbodies, if not add to bird bodies
                                                        if (!birdBodies.contains(currentBird)) {
                                                            birdBodies.add(currentBird);
                                                        }
                                                        // Place the current bird to the positions of the bird replacing it
                                                        System.out.println("Placing current bird back to the coordinates of the clicked bird " + bird + " at " + bird.getBody().getPosition() + " from " + currentBird.getBody().getPosition());
                                                        currentBird.getBody().setTransform(bird.getBody().getPosition().x, bird.getBody().getPosition().y, 0);
                                                        //birdBodies.add(currentBird); // Add the current bird back to the list
                                                    }

                                                    // Set the clicked bird as the current bird and remove it from the list
                                                    currentBird = bird;
                                                    //birds.remove(bird);

                                                    // Place the bird on the slingshot
                                                    currentBird.getBody().setTransform((SLINGSHOT_X / PPM)/ PPM, (SLINGSHOT_Y / PPM) / PPM, 0);
                                                    currentBird.update();
                                                    // Print coordinates of bird sprite
                                                    System.out.println("Bird body position: " + currentBird.getBody().getPosition());
                                                    System.out.println("Bird sprite position: " + currentBird.getX() + ", " + currentBird.getY());
                                                    currentBird.setSelected(false);
                                                    return true;
                                                }
                                            }


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
                                                //System.out.println("Distance: " + distance);
                                                float maxDrag = 2.5f;
                                                if (distance > maxDrag) {
                                                    //System.out.println("Dragging beyond max distance " + distance);
                                                    Vector2 direction = touchPoint.sub(dragStart).nor();
                                                    //touchPoint = gamePort.unproject(new Vector2(screenX, screenY)).scl(1/PPM).add(direction);
                                                    touchPoint = new Vector2(dragStart).add(direction.scl(maxDrag)).scl(1/PPM);
                                                    //touchPoint = touchPoint.scl(1/PPM);
                                                    //System.out.println("Current Distance: " + distance);
                                                    //System.out.println("Clamped Position: " + touchPoint);
                                                }
                                                // Right direction check
                                                currentBird.dragTo(touchPoint.x, touchPoint.y);
                                                //System.out.println("Dragging Bird to: " + touchPoint);
                                            }
                                            return true;
                                        }

                                        @Override
                                        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                                            if (currentBird != null && currentBird.isSelected()) {
                                                Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY));
                                                Vector2 dragVector = dragStart.sub(touchPoint);
                                                currentBird.shoot(dragVector);
                                                birdBodies.remove(currentBird);
                                                //System.out.println("Shooting Bird with vector: " + dragVector);
                                                // Optionally prepare next bird
                                                // prepareNextBird();
                                                //if (!birdBodies.isEmpty()) {
                                                //    System.out.println("Preparing next bird");
                                                //    currentBird = birdBodies.get(0);
                                                //    currentBird.getBody().setTransform(SLINGSHOT_X / PPM, SLINGSHOT_Y / PPM, 0); // Position at slingshot
                                                //    currentBird.setSelected(false);
                                                //} else {
                                                //    System.out.println("No more birds");
                                                //    currentBird = null; // No more birds
                                                //}
                                                // Add the bird to the active list
                                                birdsInAction.add(currentBird);
                                                currentBird = null;
                                            }
                                            return true;
                                        }
                                    };
        inputMultiplexer.addProcessor(birdInputProcessor);
        inputMultiplexer.addProcessor(keyboardInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Create camera for debug rendering
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, AngryBirds.V_WIDTH / PPM, AngryBirds.V_HEIGHT / PPM);

    }
    private int getNextSaveFileNumber() {
        File counterFile = new File("save_counter.dat");
        int counter = 1; // Default to 1 if the file doesn't exist

        if (counterFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(counterFile))) {
                counter = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.err.println("Failed to read save counter. Defaulting to 1.");
            }
        }

        // Increment and save the updated counter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(counterFile))) {
            writer.write(String.valueOf(counter + 1));
        } catch (IOException e) {
            System.err.println("Failed to update save counter.");
        }

        return counter;
    }
    private void handleCollision(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();
        // Check if bird collides with a pig or block
        if (fixtureA.getBody().getUserData() instanceof Bird && fixtureB.getBody().getUserData() instanceof Pig) {
            Pig pig = (Pig) fixtureB.getBody().getUserData();
            if (pig.getHealth() - 50 <= 0) {
                updateScore(80);
            }
            pig.takeDamage(50, bodiesToDestroy); // Adjust damage value
        } else if (fixtureA.getBody().getUserData() instanceof Pig && fixtureB.getBody().getUserData() instanceof Bird) {
            Pig pig = (Pig) fixtureA.getBody().getUserData();
            if (pig.getHealth() - 50 <= 0) {
                updateScore(80);
            }
            pig.takeDamage(50, bodiesToDestroy); // Adjust damage value
        }

        if (fixtureA.getBody().getUserData() instanceof Bird && fixtureB.getBody().getUserData() instanceof Material) {
            Material block = (Material) fixtureB.getBody().getUserData();
            updateScore(30);
            block.takeDamage(world,25, bodiesToDestroy, blockBodies); // Adjust damage value
        } else if (fixtureA.getBody().getUserData() instanceof Material && fixtureB.getBody().getUserData() instanceof Bird) {
            Material block = (Material) fixtureA.getBody().getUserData();
            updateScore(30);
            block.takeDamage(world,25, bodiesToDestroy, blockBodies); // Adjust damage value
        }

    }

    private void handleCollisionGround(Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();
        if ("Ground".equals(dataA)) {
            processGroundCollision(fixtureB.getBody(), impulse);
            updateScore(10);
        } else if ("Ground".equals(dataB)) {
            processGroundCollision(fixtureA.getBody(), impulse);
            updateScore(10);
        }
    }
    private void processGroundCollision(Body body, ContactImpulse impulse) {
        if (body.getUserData() instanceof Material) {
            Material material = (Material) body.getUserData();
            handleGroundCollision(material, impulse);
        } else if (body.getUserData() instanceof Pig) {
            Pig pig = (Pig) body.getUserData();
            handleGroundCollision(pig, impulse);
        }
    }

    private void handleGroundCollision(Material material, ContactImpulse impulse) {
        if (material.isGrounded()) {
            return; // Don't apply damage to blocks that started on the ground
        }

        // Calculate total impulse magnitude
        float totalImpulse = 0;
        for (float normalImpulse : impulse.getNormalImpulses()) {
            totalImpulse += normalImpulse;
        }

        // Apply damage based on impulse strength
        int damage = (int) (totalImpulse * DAMAGE_SCALING_FACTOR); // Adjust scaling factor as needed
        if (damage > MINIMUM_DAMAGE_THRESHOLD) { // Ignore small impacts
            material.takeDamage(world, damage, bodiesToDestroy, blockBodies);
        }

        // Mark the object as grounded after the first collision with the ground
        material.setGrounded(true);
    }
    private void handleGroundCollision(Pig pig, ContactImpulse impulse) {
        if (pig.isGrounded()) {
            return; // Don't apply damage to blocks that started on the ground
        }

        // Calculate total impulse magnitude
        float totalImpulse = 0;
        for (float normalImpulse : impulse.getNormalImpulses()) {
            totalImpulse += normalImpulse;
        }

        // Apply damage based on impulse strength
        int damage = (int) (totalImpulse * DAMAGE_SCALING_FACTOR); // Adjust scaling factor as needed
        if (damage > MINIMUM_DAMAGE_THRESHOLD) { // Ignore small impacts
            pig.takeDamage(damage, bodiesToDestroy);
        }

        // Mark the object as grounded after the first collision with the ground
        pig.setGrounded(true);
    }

    public void updateScore(int points) {
        score += points; // Update the score
        scoreLabel.setText(String.valueOf(score));// Update the label
        scoreLabel2.setText(String.valueOf(score));// Update the label
    }
    private void recreateStaticObjects() {
        // Create ground
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        //Body ground;

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);
        ground = world.createBody(bdef);

        shape.setAsBox(AngryBirds.V_WIDTH, 1);
        fdef.shape = shape;
        ground.createFixture(fdef);

        shape.setAsBox(1, AngryBirds.V_HEIGHT);
        fdef.shape = shape;
        ground.createFixture(fdef);
        ground.setUserData("Ground");
        // Add other static objects (like walls) here if needed
        System.out.println("Static objects recreated.");
    }
    private void clearLevel() {
        // Destroy all bodies in the world
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        // Convert to a standard Java array if needed
        // Convert to a standard Java ArrayList
        //List<Body> bodyList = new ArrayList<>((Collection) bodies);

        // Get all world bodies and store them in an array, and run a for loop to destroy them

        for (Object body : bodies) {
            System.out.println("Destroying body: " + body.toString());
            world.destroyBody((Body) body);
        }

        // Clear game object lists
        birdBodies.clear();
        pigBodies.clear();
        blockBodies.clear();
        birdsInAction.clear();

        // Clear the current bird reference
        currentBird = null;

        // Recreate static objects
        recreateStaticObjects();
    }

    public void saveGame(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            GameState gameState = captureGameState();
            oos.writeObject(gameState);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameState captureGameState() {
        ArrayList<BirdState> birdStates = new ArrayList<>();
        for (Bird bird : birdBodies) {
            birdStates.add(new BirdState(
                bird.getBody().getPosition().x, bird.getBody().getPosition().y,
                bird.getBody().getLinearVelocity().x,
                bird.getBody().getLinearVelocity().y,
                bird.isSelected(), bird.isShot(), bird.isDead(), bird.getName()
            ));
        }

        ArrayList<PigState> pigStates = new ArrayList<>();
        for (Pig pig : pigBodies) {
            pigStates.add(new PigState(pig.getBody().getPosition().x, pig.getBody().getPosition().y, pig.isDead(), pig.getHealth(), pig.isGrounded(), pig.getType()));
        }

        ArrayList<MaterialState> blockStates = new ArrayList<>();
        for (Material block : blockBodies) {
            // if its a triangle, print
            if (block.getType().equals("Wood Triangle")) {
                System.out.println("Saving Wood Triangle");
            }
            blockStates.add(new MaterialState(block.getBody().getPosition().x, block.getBody().getPosition().y, block.getBody().getAngle(), block.isDead(), block.getHealth(), block.getType(), block.isGrounded()));
        }

        BirdState currentBirdState = (currentBird != null) ? new BirdState(
            currentBird.getBody().getPosition().x, currentBird.getBody().getPosition().y,
            currentBird.getBody().getLinearVelocity().x,
            currentBird.getBody().getLinearVelocity().y,
            currentBird.isSelected(), currentBird.isShot(), currentBird.isDead(), currentBird.getName()
        ) : null;

        return new GameState(AngryBirds.currentLevel, 1, birdStates, pigStates, blockStates, score, currentBirdState);
    }

    public void loadGame(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            GameState gameState = (GameState) ois.readObject();
            // Clear the current level
            clearLevel();
            // Restore the game state
            restoreGameState(gameState);
            System.out.println("Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void restoreGameState(GameState gameState) {
        // Clear existing game objects
        birdBodies.clear();
        pigBodies.clear();
        blockBodies.clear();
        // Update the current level
        AngryBirds.currentLevel = gameState.getLevel();
        // Restore birds
        for (BirdState birdState : gameState.getBirds()) {
            //Bird bird = new RedBird(birdTexture); // Recreate bird object
            // Check bird name to check which type of bird it is from red, blue and yellow
            // If bird is dead, then skip
            if (birdState.isDead()) {
                continue;
            }
            String birdName = birdState.getName();
            Bird bird;
            if (birdName.equals("Red Bird") ) {
                bird = new RedBird(birdSheet);
            } else if (birdName.equals("Blue Bird")) {
                bird = new BlueBird(birdSheet);
            } else {
                bird = new YellowBird(birdSheet);
            }
            bird.createBody(world, birdState.getX() / PPM, birdState.getY() / PPM, false);
            bird.getBody().setLinearVelocity(birdState.getVelocityX(), birdState.getVelocityY());
            bird.setPosition(birdState.getX(), birdState.getY());
            bird.update();
            bird.setSelected(birdState.isSelected());
            bird.setShot(birdState.isShot());
            birdBodies.add(bird);
        }

        // Restore pigs
        for (PigState pigState : gameState.getPigs()) {
            if (pigState.isDead()) {
                continue;
            }
            Pig pig = new Pig(angryBirdSheet,2843, 7, 103, 103);
            pig.createBody(world, pigState.getX() / PPM, pigState.getY() / PPM);
            pig.setPosition(pigState.getX(), pigState.getY());
            pig.update();
            pig.setDead(pigState.isDead());
            pig.setHealth(pigState.getHealth());
            pig.setGrounded(pigState.isGrounded());
            pigBodies.add(pig);
        }

        // Restore blocks
        for (MaterialState blockState : gameState.getBlocks()) {
            if (blockState.isDead()) {
                continue;
            }
            //Material block;
            if (blockState.getType().equals("Wood Cube")) {
                System.out.println("Creating Wood Cube from restore");
                Cube block;
                block = new Cube("Wood Cube",100,  angryBirdSheet, 803, 776, 84, 84);
                block.createBody(world, blockState.getX() / PPM, blockState.getY() / PPM, 57, 57, false);
                block.setPosition(blockState.getX(), blockState.getY());
                block.getBody().setTransform(blockState.getX() / PPM, blockState.getY() / PPM, blockState.getAngle());
                block.update();
                block.setDead(blockState.isDead());
                block.setHealth(blockState.getHealth());
                block.setGrounded(blockState.isGrounded());
                blockBodies.add(block);
                // Setting angle

            } else {
                System.out.println("Creating Wood Triangle from restore");
                Triangle block;
                block = new Triangle("Wood Triangle", 100, angryBirdSheet, 887, 776, 84, 84);
                block.createBody(world, blockState.getX(), blockState.getY(), 57, 57, false);
                block.update();
                block.setDead(blockState.isDead());
                block.setHealth(blockState.getHealth());
                block.setGrounded(blockState.isGrounded());
                blockBodies.add(block);
            }
            //Material block = new (blockTexture, world, blockState.getX(), blockState.getY(), blockState.getHealth());
            // Restore properties and add to block
            //block.createBody(world, blockState.getX(), blockState.getY(), 57, 57, false);
            //blockBodies.add(block);
        }

        // Restore current bird
        if (gameState.getCurrentBird() != null) {
            BirdState birdState = gameState.getCurrentBird();
            //currentBird = new Bird(birdTexture);
            // Current Bird would be one of the birds from birdBodies, check which has same positions and set it as current bird
            for (Bird bird : birdBodies) {
                if (bird.getX() == birdState.getX() && bird.getY() == birdState.getY()) {
                    currentBird = bird;
                    break;
                }
            }
            // If no current bird found, set it to birdBodies[0]
            if (currentBird == null) {
                currentBird = birdBodies.get(0);
            }
            //currentBird.createBody(world, birdState.getX(), birdState.getY());
            //currentBird.getBody().setLinearVelocity(birdState.getVelocityX(), birdState.getVelocityY());
            //currentBird.setSelected(birdState.isSelected());
            //currentBird.setShot(birdState.isShot());
        } else {
            currentBird = null;
        }

        // Restore score
        score = gameState.getScore();
        scoreLabel.setText(String.valueOf(score));
        scoreLabel2.setText(String.valueOf(score));
    }




    private void replayLevel() {
        // Reset all game variables
        isLevelCleared = false;
        isLevelFailed = false;
        waitingForLevelEnd = false;
        levelEndTimer = 0;
        score = 0;

        // Clear lists
        pigBodies.clear();
        birdBodies.clear();
        birdsInAction.clear();

        // Reinitialize the level
        initializeLevel();

        // Hide the replay button
        //skipButton2.setVisible(false);
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
            velocity.add(0, -6f * timeStep); // Apply gravity (adjust if world gravity is different)
        }

        shapeRenderer.end();
    }


    private void destroyBodies() {
        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
            System.out.println("Body destroyed: " + body);
        }
        bodiesToDestroy.clear(); // Clear the list after destruction
    }


    @Override
    public void render(float delta) {

        world.step(1/60f, 6, 2);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        destroyBodies();
        // TESTING MIGHT BREAK
        //redBird1.update(delta);
        //redBird2.update(delta);
        if (redBird1.getBody() != null) {
            redBird1.update(delta);
        }
        if (redBird2.getBody() != null) {
            redBird2.update(delta);
        }

        Iterator<Pig> pigIterator = pigBodies.iterator();
        while (pigIterator.hasNext()) {
            Pig pig = pigIterator.next();
            // If pigs is out of bounds remove it and destroy the body
            if (pig.getBody() != null && pig.isOutOfBounds()) {
                pig.die(bodiesToDestroy);
                pigIterator.remove();
                System.out.println("Pig out of bounds");
                //world.destroyBody(pig.getBody());
            } else if (pig.getBody() == null) {
                // Remove the pig from the list if it has been destroyed
                //pigBodies.remove(pig);
                pigIterator.remove();
                System.out.println("Pig removed");
            } else {
                pig.update(delta, bodiesToDestroy);
            }
        }

        game.batch.begin();

        //wood_triangle = new Triangle("Wood Triangle", 100, angryBirdSheet, 887, 776, 84, 84);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);

        // Level Designing
        for (int i = 0; i < cubes.size(); i++) {
            cubes.get(i).setSize(60,60);
            cubes.get(i).update();
            if (cubes.get(i).getHealth() <= 0) {
                //world.destroyBody(cubes.get(i).getBody());
                cubes.remove(i);
            } else {
                cubes.get(i).draw(game.batch);
            }
        }
        /*
        for (int i = 2; i < 5; i++) {
            cubes.get(i).setSize(60,60);
            cubes.get(i).update();
            if (cubes.get(i).getHealth() <= 0) {
                //world.destroyBody(cubes.get(i).getBody());
                cubes.remove(i);
            } else {
                cubes.get(i).draw(game.batch);
            }
        }
        */
        //wood_triangle.setPosition(AngryBirds.V_WIDTH * 0.6f, AngryBirds.V_HEIGHT * 0.139f);
       // wood_triangle.setSize(60, 60);
        if (wood_triangle.getHealth() <= 0) {
            //world.destroyBody(wood_triangle.getBody());
            wood_triangle.update();
        } else {
            wood_triangle.update();
            wood_triangle.draw(game.batch);
        }

        // Sprites
        //redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        //redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.13f, AngryBirds.V_HEIGHT * 0.139f);
        //pig1.setSize(60, 60);
        //pig1.setOrigin(pig1.getWidth() / 2, pig1.getHeight() / 2);
        //pig2.setSize(60, 60);
        //redBird1.draw(game.batch);


        //for (Pig pig : pigBodies) {
        //    System.out.println("Pig Position: " + pig.getBody().getPosition());
        //    pig.render(game.batch);
        //}
        if (pig1.getBody()!=null) {
            pig1.render(game.batch);
        }
        if (pig2.getBody()!=null) {
            pig2.render(game.batch);
        }
        if (currentBird != null) {
            currentBird.draw(game.batch);
        }
        //currentBird.draw(game.batch);
        // Check birds in action
        Iterator<Bird> iterator = birdsInAction.iterator();
        while (iterator.hasNext()) {
            Bird bird = iterator.next();
            if (bird.getBody() == null) {
                iterator.remove();
                System.out.println("Bird removed");
            } else {
                bird.draw(game.batch);
                //System.out.println("Bird Position: " + bird.getBody().getPosition());
            }
            // Check if the bird is out of bounds or has stopped moving
            if (bird.isOutOfBounds() || bird.isStopped()) {
                bird.reset(); // Auto-die the bird
                iterator.remove(); // Remove it from the active list

                // Switch to the next bird if available
                if (!birdBodies.isEmpty() && currentBird == null) {
                    currentBird = birdBodies.remove(0); // Get the next bird
                    currentBird.getBody().setTransform(SLINGSHOT_X / PPM, SLINGSHOT_Y / PPM, 0);
                }
            }
        }
        // Also render birds not in birdsInAction list
        for (Bird bird : birdBodies) {
            // If bird not in birdsinaction
            if (!birdsInAction.contains(bird)) {
                bird.draw(game.batch);
            }
        }
        //redBird1.draw(game.batch);
        //redBird2.draw(game.batch);
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
            stage.addActor(scoreLabel);
        } else if (isLevelFailed) { // Add button to retry level in level fail pop up
            stage.addActor(levelFailedLabel);
            stage.addActor(skipButton2);
            stage.addActor(scoreLabel2);
        } else {
            levelClearedLabel.remove();
            levelFailedLabel.remove();
            skipButton2.remove();
            scoreLabel.remove();
            scoreLabel2.remove();
        }
        if (pigBodies.isEmpty() && !isLevelCleared) {
            isLevelCleared = true;
            increaseLevelNumber(1);
            System.out.println("Level Cleared!");
            System.out.println("Score: " + score);
        } else if (birdBodies.isEmpty() && birdsInAction.isEmpty() && currentBird == null && !waitingForLevelEnd && !pigBodies.isEmpty()) {
            //isLevelFailed = true;
            waitingForLevelEnd = true;
            levelEndTimer = 0;
            System.out.println("Waiting for level end");
            System.out.println("Score: " + score);
        }
        if (waitingForLevelEnd) {
            levelEndTimer += delta; // Increment the timer

            if (levelEndTimer >= 2.0f) { // 4 seconds delay
                waitingForLevelEnd = false;

                if (pigBodies.isEmpty()) {
                    isLevelCleared = true; // All pigs are dead, level is cleared
                    increaseLevelNumber(1);
                    System.out.println("Level Cleared!");
                } else {
                    isLevelFailed = true; // Pigs remain, level is failed
                    System.out.println("Level Failed!");
                }
            }
        }
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
