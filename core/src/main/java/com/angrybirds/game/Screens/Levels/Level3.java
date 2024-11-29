package com.angrybirds.game.Screens.Levels;

import com.angrybirds.game.AngryBirds;
import com.angrybirds.game.Objects.*;
import com.angrybirds.game.Objects.Materials.*;
import com.angrybirds.game.Screens.LevelSelectScreen;
import com.angrybirds.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Level3 extends Level {
    private Stage stage;
    private Texture angryBirdSheet, uiTexture, moreUITexture;
    private Pig pig1, pig2, pig3, pig4, pig5;
    private Texture birdSheet;
    private Texture blockSheet;
    private RedBird redBird1, redBird2;
    private YellowBird yellowBird;
    private BlueBird blueBird;
    private Slingshot slingshot;
    private TNT tnt;
    private Skin skin;
    private boolean isPaused = false;
    private TextureRegion pausePopUp;
    private Label pauseLabel, levelClearedLabel, levelFailedLabel, scoreLabel, scoreLabel2;
    private ImageButton resumeButton, homeButton, skipButton, skipButton2;

    private List<Body> bodiesToDestroy = new ArrayList<>();
    private int score = 0;
    private Bird currentBird;
    private Vector2 dragStart, dragEnd;
    private OrthographicCamera b2dCam;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Rectangle> rods;
    private Triangle wood_triangle;
    private Body ground;
    private InputMultiplexer inputMultiplexer;
    private float levelEndTimer = 0; // Tracks elapsed time after the last bird is used
    private boolean waitingForLevelEnd = false; // Indicates if we are waiting for pigs to die

    //Constants
    private static final float SLINGSHOT_X = AngryBirds.V_WIDTH * 21.5f; // 20% from left
    private static final float SLINGSHOT_Y = AngryBirds.V_HEIGHT * 27f; // 25% from bottom

    public Level3(AngryBirds game, OrthographicCamera gameCam, Viewport gamePort, Texture background) {
        super(game, gameCam, gamePort, background);
        stage = new Stage(gamePort, game.batch);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        //Gdx.input.setInputProcessor(stage);
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
        yellowBird = new YellowBird(birdSheet);
        blueBird = new BlueBird(birdSheet);
        slingshot = new Slingshot(blockSheet);
        angryBirdSheet = new Texture(Gdx.files.internal("SpriteSheet/AngryBirds.png"));
        pig1 = new Pig(angryBirdSheet, 2843, 8, 103, 103);
        pig2 = new Pig(angryBirdSheet, 2843, 8, 103, 103);
        pig3 = new Pig(angryBirdSheet, 2843, 708, 107, 93, 2);
        pig4 = new Pig(angryBirdSheet, 2843, 708, 107, 93, 2);
        pig5 = new Pig(moreUITexture, 643, 312, 120, 126, 3);

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
        Label titleLabel = new Label("Level 3", labelStyle);
        titleLabel.setPosition(AngryBirds.V_WIDTH*0.35f, AngryBirds.V_HEIGHT*0.85f);
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
        //resumeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 50, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
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
        //homeButton.setPosition((AngryBirds.V_WIDTH - pausePopUp.getRegionWidth()) / 2 + 250, (AngryBirds.V_HEIGHT - pausePopUp.getRegionHeight()) / 2 + 50);
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

        //addDummyButtons();
        shapeRenderer = new ShapeRenderer();
        initializeLevel3();

    }

    private void initializeLevel3() {

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
        yellowBird.createBody(world, SLINGSHOT_X/PPM, SLINGSHOT_Y/PPM, false);
        redBird1.createBody(world, (SLINGSHOT_X/ PPM)-120, (SLINGSHOT_Y/ PPM)-72, false);
        redBird2.createBody(world, (SLINGSHOT_X/ PPM)-180, (SLINGSHOT_Y/ PPM)-72, false);
        blueBird.createBody(world, (SLINGSHOT_X/PPM)-60, (SLINGSHOT_Y/PPM)-72, false);
        birdBodies.add(yellowBird);
        birdBodies.add(redBird1);
        birdBodies.add(redBird2);
        birdBodies.add(blueBird);
        pig1.createBody(world, AngryBirds.V_WIDTH * 0.5f, AngryBirds.V_HEIGHT * 0.15f); // Normal
        pig2.createBody(world, AngryBirds.V_WIDTH * 0.76f, AngryBirds.V_HEIGHT * 0.15f); // Normal
        pig3.createBody(world, AngryBirds.V_WIDTH * 0.58f, AngryBirds.V_HEIGHT * 0.29f, 2); // Helmet
        pig4.createBody(world, AngryBirds.V_WIDTH * 0.67f, AngryBirds.V_HEIGHT * 0.29f, 2); // Helmet
        pig5.createBody(world, AngryBirds.V_WIDTH * 0.633f, AngryBirds.V_HEIGHT * 0.55f, 3); // King
        this.pigBodies.add(pig1);
        this.pigBodies.add(pig2);
        this.pigBodies.add(pig3);
        this.pigBodies.add(pig4);
        this.pigBodies.add(pig5);
        currentBird = birdBodies.get(0);
        rods = new ArrayList<>();
        //15 rods structures
        //rod=new rectangle wala fix for everything maine bas statement dali hai
        for (int i = 0; i < 15; i++) {
            Rectangle rod;
            //7 wood rods
            if(i<7) {

                //4 vertical wood rods(indices 3,4,5,6)
                if(i>=3){
                    rod = new Rectangle("Wood Rod", 100, angryBirdSheet, 1138, 895, 205, 21, 147, 21, true);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.815f - 0.132f * (i-3)), AngryBirds.V_HEIGHT * 0.14f, 21, 147, false);
                }
                //3 horizontal support wood rods(indices 0,1,2)
                else{
                    rod = new Rectangle("Wood Rod", 100, angryBirdSheet, 1138, 895, 205, 21, (int) (AngryBirds.V_WIDTH*0.139f), 21, false);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.137f + (4-i)*0.139f), AngryBirds.V_HEIGHT * 0.344f, (int) (AngryBirds.V_WIDTH *  0.139f), 21, false);
                }
                blockBodies.add(rod);
                rods.add(rod);
                System.out.println("Rod body: " + rod.getBody());
            }
            //5 ice rods
            if(i>=7 && i<12){

                //3 vertical ice rods(indices 9,10,11)
                if(i>=9){
                    rod = new Rectangle("Ice Rod", 100, angryBirdSheet, 1090, 1344, 205, 21, 133, 21, true, 2);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.73f - 0.1135f * (i-9)), AngryBirds.V_HEIGHT * 0.376f, 21, 133, false);
                } else {
                    System.out.println("Ice Rod created");
                    rod = new Rectangle("Ice Rod", 100, angryBirdSheet, 1090, 1344, 205, 21, (int) (AngryBirds.V_WIDTH * 0.122f), 21, false, 2);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.626f + 0.122f* (7-i)), AngryBirds.V_HEIGHT * 0.563f, (int) (AngryBirds.V_WIDTH *  0.122f), 21, false);
                }
                blockBodies.add(rod);
                rods.add(rod);
                System.out.println("Rod body: " + rod.getBody());
            }
            //3 stone rods
            if(i>=12){

                //2 vertical stone rods
                if(i>12){
                    rod = new Rectangle("Stone Rod", 200, angryBirdSheet, 1099, 1740, 205, 21, 134, 21, true, 3);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.675f - 0.106f * (i-13)), AngryBirds.V_HEIGHT * 0.591f, 21, 134, false);
                }
                //1 horziontal support stone rod
                else{
                    rod = new Rectangle("Stone Rod", 200, angryBirdSheet, 1099, 1740, 205, 21, (int) (AngryBirds.V_WIDTH*0.122f), 21, false, 3);
                    rod.createBody(world, AngryBirds.V_WIDTH * (0.493f) + 100, AngryBirds.V_HEIGHT * 0.358f + 301, (int) (AngryBirds.V_WIDTH * 0.122f), 21, false);
                }
                blockBodies.add(rod);
                rods.add(rod);
                System.out.println("Rod body: " + rod.getBody());
            }
            //System.out.println("Rod " + i + " created");
        }
        // length of rods
        //System.out.println("Rods size: " + rods.size());
        tnt = new TNT(angryBirdSheet,472, 901, 71, 68);
        tnt.createBody(world, AngryBirds.V_WIDTH * 0.62f, AngryBirds.V_HEIGHT * 0.187f, 60,60);
        blockBodies.add(tnt);
        InputAdapter birdInputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (currentBird != null) {
                    System.out.println("Current Bird Is Shot " + currentBird.isShot());
                }
                if (yellowBird.getBody()!= null && yellowBird.isShot()) {
                    System.out.println("Applying speed boost to Yellow Bird");
                    yellowBird.applySpeedBoost();
                    return true;
                }
                if (blueBird.getBody()!= null && blueBird.isShot()) {
                    System.out.println("Blue Bird Splitting");
                    blueBird.split(world, birdBodies, birdSheet); // Pass the list of birds
                    return true;
                }
                Vector2 touchPoint = gamePort.unproject(new Vector2(screenX, screenY));
                //System.out.println("Touch Down at: " + touchPoint); // Debug print
                // Check if the click is on any bird
                for (Bird bird : birdBodies) {
                    //System.out.println("Bird: " + bird);
                    //System.out.println("Bird bounds: " + bird.getBounds());
                    //System.out.println("Touch Point: " + touchPoint);
                    if (!bird.isDead() && bird != currentBird &&bird.getBounds().contains(touchPoint.x/PPM, touchPoint.y/PPM)) {
                        System.out.println("Bird clicked: " + bird);
                        // If Instance of blue bird, and blue bird already shot return
                        if (bird instanceof BlueBird && blueBird.isShot()) {
                            System.out.println("Blue Bird already shot");
                            return true;
                        }

                        // If there is already a bird on the slingshot, return it to the list
                        if (currentBird != null) {
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
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Create camera for debug rendering
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, AngryBirds.V_WIDTH / PPM, AngryBirds.V_HEIGHT / PPM);

    }

    private void handleCollision(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();
        // Check if bird collides with a pig or block
        // Print .getBody of 1 rod from rods to check if its null or not

        if (dataA instanceof TNT) {
            //System.out.println("Rod 1 body: " + rods.get(0).getBody());
            ((TNT) dataA).explode(world, bodiesToDestroy, rods, pigBodies, blockBodies);// Pass the world and objects list
            tnt.renderRadius(shapeRenderer);
        } else if (dataB instanceof TNT) {
            //System.out.println("Rod 1 body: " + rods.get(0).getBody());
            ((TNT) dataB).explode(world, bodiesToDestroy, rods, pigBodies, blockBodies); // Pass the world and objects list
            tnt.renderRadius(shapeRenderer);
        }
        if (fixtureA.getBody().getUserData() instanceof Bird && fixtureB.getBody().getUserData() instanceof Pig) {
            Pig pig = (Pig) fixtureB.getBody().getUserData();
            if (pig.getHealth() - 50 <= 0) {
                updateScore(80);
            }
            // If bird is not selected, dont apply damage
            Bird bird = (Bird) fixtureA.getBody().getUserData();
            if (!bird.isShot()) {
                return;
            }
            pig.takeDamage(50, bodiesToDestroy); // Adjust damage value
        } else if (fixtureA.getBody().getUserData() instanceof Pig && fixtureB.getBody().getUserData() instanceof Bird) {
            Pig pig = (Pig) fixtureA.getBody().getUserData();
            if (pig.getHealth() - 50 <= 0) {
                updateScore(80);
            }
            // If bird is not selected, dont apply damage
            Bird bird = (Bird) fixtureB.getBody().getUserData();
            if (!bird.isShot()) {
                return;
            }
            pig.takeDamage(50, bodiesToDestroy); // Adjust damage value
        }

        if (fixtureA.getBody().getUserData() instanceof Bird && fixtureB.getBody().getUserData() instanceof Material) {
            Material block = (Material) fixtureB.getBody().getUserData();
            updateScore(30);
            block.takeDamage(world,70, bodiesToDestroy, blockBodies); // Adjust damage value
        } else if (fixtureA.getBody().getUserData() instanceof Material && fixtureB.getBody().getUserData() instanceof Bird) {
            Material block = (Material) fixtureA.getBody().getUserData();
            updateScore(30);
            block.takeDamage(world,70, bodiesToDestroy, blockBodies); // Adjust damage value
        }

        if (dataA instanceof Pig && dataB instanceof Material) {
            applyDamage((Pig) dataA, fixtureB.getBody());
        } else if (dataB instanceof Pig && dataA instanceof Material) {
            applyDamage((Pig) dataB, fixtureA.getBody());
        }
    }
    private void applyDamage(Pig pig, Body blockBody) {
        Vector2 velocity = blockBody.getLinearVelocity();
        float speed = velocity.len(); // Calculate speed (magnitude of velocity)

        float damage = speed * blockBody.getMass()*8f; // Example damage formula
        pig.takeDamage((int) damage, bodiesToDestroy);

        //System.out.println("Pig took damage: " + damage + ", Remaining Health: " + pig.getHealth());
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

    @Override
    public void render(float delta) {
        world.step(1/60f, 6, 2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        destroyBodies(delta);

        if (redBird1.getBody() != null) {
            redBird1.update(delta);
        }
        if (redBird2.getBody() != null) {
            redBird2.update(delta);
        }
        if (yellowBird.getBody() != null) {
            yellowBird.update(delta);
        }
        if (blueBird.getBody() != null) {
            blueBird.update(delta);
        }
        for (Bird bird : birdBodies) {
            // If bird is instance of BlueBird, update the state of the bird
            if (bird instanceof BlueBird) {
                // Update state if bird != blueBird
                if (bird != blueBird) {
                    //System.out.println("Updating Blue Bird Childs");
                    ((BlueBird) bird).update(delta);
                }
            }
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
        //15 rods (7 wood + 5 ice + 3 stone)
        /*
        for (int i = 0; i < 7; i++) {
            Rectangle rod = new Rectangle("Wood Rod", 100, angryBirdSheet, 1138, 895, 205, 21, 205, 21, true);
            rods.add(rod); //indices 0-6
        }
        for (int i = 0; i < 5; i++) {
            Rectangle rod = new Rectangle("Ice Rod", 150, angryBirdSheet, 1090, 1344, 205, 21, 205, 21, true);
            rods.add(rod); //indices 7-11
        }
        for (int i = 0; i < 3; i++) {
            Rectangle rod = new Rectangle("Stone Rod", 200, angryBirdSheet, 1098, 1739, 205, 21, 205, 21, true);
            rods.add(rod); //indices 12-14
        }

         */
        Sphere stone_ball1 = new Sphere("Stone Sphere", 200, angryBirdSheet, 975, 1703, 78, 78);
        Sphere stone_ball2 = new Sphere("Stone Sphere", 200, angryBirdSheet, 975, 1703, 78, 78);
        TextureRegion backgroundL = new TextureRegion(background, 1027, 2, (1538 - 1027), 207);
        //TextureRegion tnt = new TextureRegion(angryBirdSheet, 472, 901, 71, 68);

        game.batch.draw(backgroundL, 0, 0, AngryBirds.V_WIDTH, AngryBirds.V_HEIGHT);
        //game.batch.draw(tnt, AngryBirds.V_WIDTH * 0.68f, AngryBirds.V_HEIGHT * 0.139f, 60, 60);
        //game.batch.draw(stone_ball1, AngryBirds.V_WIDTH * 0.535f, AngryBirds.V_HEIGHT * 0.367f, 60, 60);
        //game.batch.draw(stone_ball2, AngryBirds.V_WIDTH * 0.823f, AngryBirds.V_HEIGHT * 0.367f, 60, 60);

        //support rods (6)
        /*
        for (int i = 0; i < 6; i++) {
            if (i<3) {
                Rectangle rod = rods.get(i); //rods with indices 0,1,2
                rod.setSize(AngryBirds.V_WIDTH * 0.139f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.49f + 0.1375f*i), AngryBirds.V_HEIGHT * 0.342f);
                rod.draw(game.batch);
            }
            else if (i<5) {
                Rectangle rod = rods.get(i+4); //rods with indices 7,8
                rod.setSize(AngryBirds.V_WIDTH *  0.122f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.503f + 0.121f * (4-i)) + 100, AngryBirds.V_HEIGHT * 0.342f + 150);
                rod.draw(game.batch);
            }
            else {
                Rectangle rod = rods.get(i + 7); //rod with index 12
                rod.setSize(AngryBirds.V_WIDTH * 0.122f, 21);
                rod.setPosition(AngryBirds.V_WIDTH * (0.503f + 0.06f) + 100, AngryBirds.V_HEIGHT * 0.342f + 301);
                rod.draw(game.batch);
            }
        }

        //Vertical rods(4+3+2=9)
        //Stone rods vertical(4)
        for (int i = 3; i < 7; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(147, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.815f - 0.132f * (i-3)), AngryBirds.V_HEIGHT * 0.494f - 164);
            rod.setRotation(90);
            rod.draw(game.batch);
        }

        //Ice rods vertical(3)
        for (int i = 9; i < 12; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(133, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.735f - 0.1135f * (i-9)), AngryBirds.V_HEIGHT * 0.494f);
            rod.setRotation(90);
            rod.draw(game.batch);
        }

        //Wood rods vertical(2)
        for (int i = 13; i < 15; i++) {
            Rectangle rod = rods.get(i);
            rod.setSize(134, 21);
            rod.setPosition(AngryBirds.V_WIDTH * (0.675f - 0.106f * (i-13)), AngryBirds.V_HEIGHT * 0.494f + 150);
            rod.setRotation(90);
            rod.draw(game.batch);
        }


         */
        for (Rectangle rod : rods) {
            // Sync the box2d body with the sprite position
            //System.out.println("Rod position: " + rod.getX() + ", " + rod.getY());
            rod.update();
            if (rod.getHealth() > 0) {
                //System.out.println("Rod teesting " + rod.getBody());
                rod.draw(game.batch);
            }
            //rod.draw(game.batch);
        }
        if (tnt.getBody() != null) {
            tnt.update(delta);

            //System.out.println("TNT Position: " + tnt.getX() + ", " + tnt.getY());
            tnt.draw(game.batch);
            tnt.renderRadius(shapeRenderer);
        }

        // Sprites
        //redBird1.setPosition(AngryBirds.V_WIDTH * 0.05f, AngryBirds.V_HEIGHT * 0.139f);
        //redBird2.setPosition(AngryBirds.V_WIDTH * 0.1f, AngryBirds.V_HEIGHT * 0.139f);
        //yellowBird.setPosition(AngryBirds.V_WIDTH * 0.15f, AngryBirds.V_HEIGHT * 0.137f);
        slingshot.setPosition(AngryBirds.V_WIDTH * 0.2f, AngryBirds.V_HEIGHT * 0.139f);
        pig1.setSize(60, 60);
        pig2.setSize(60, 60);
        pig3.setSize(80, 80);
        pig4.setSize(80, 80);
        pig5.setSize(100, 100);
        //pig1.setPosition(AngryBirds.V_WIDTH * 0.54f, AngryBirds.V_HEIGHT * 0.139f);
        //pig2.setPosition(AngryBirds.V_WIDTH * 0.8f, AngryBirds.V_HEIGHT * 0.139f);
        //pig3.setPosition(AngryBirds.V_WIDTH * 0.62f, AngryBirds.V_HEIGHT * 0.365f);
        //pig4.setPosition(AngryBirds.V_WIDTH * 0.735f, AngryBirds.V_HEIGHT * 0.365f);
        //pig5.setPosition(AngryBirds.V_WIDTH * 0.666f, AngryBirds.V_HEIGHT * 0.577f);
        if (pig1.getBody() != null) {
            pig1.draw(game.batch);
        }
        if (pig2.getBody() != null) {
            pig2.draw(game.batch);
        }
        if (pig3.getBody() != null) {
            pig3.draw(game.batch);
        }
        if (pig4.getBody() != null) {
            pig4.draw(game.batch);
        }
        if (pig5.getBody() != null) {
            //System.out.println("Pig 5 position: " + pig5.getBody().getPosition());
            pig5.draw(game.batch);
        }
        //pig1.draw(game.batch);
        //pig2.draw(game.batch);
        //pig3.draw(game.batch);
        //pig4.draw(game.batch);
        //pig5.draw(game.batch);
        /*
        if (redBird1.getBody() != null) {
        redBird1.draw(game.batch);
        }
        if (redBird2.getBody() != null) {
            redBird2.draw(game.batch);
        }
        if (yellowBird.getBody() != null) {
            yellowBird.draw(game.batch);
        }

         */
        //redBird2.draw(game.batch);
        //yellowBird.draw(game.batch);
        slingshot.draw(game.batch);
        Iterator<Bird> iterator = birdsInAction.iterator();
        while (iterator.hasNext()) {
            Bird bird = iterator.next();
            if (bird.getBody() == null) {
                iterator.remove();
                System.out.println("Bird removed");
            } else {
                //bird.update();
                bird.draw(game.batch);
            }

            if (bird.isOutOfBounds() || bird.isStopped()) {
                bird.reset();
                iterator.remove();

                if (!birdBodies.isEmpty() && currentBird == null) {
                    currentBird = birdBodies.remove(0);
                    currentBird.getBody().setTransform(SLINGSHOT_X / PPM, SLINGSHOT_Y / PPM, 0); // Position at slingshot
                }
            }
        }
        for (Bird bird : birdBodies) {
            if (!birdsInAction.contains(bird)) {
                if (bird.getBody() != null) {
                    bird.draw(game.batch);
                }
            }
        }
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
            increaseLevelNumber();
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
                    increaseLevelNumber();
                    System.out.println("Level Cleared!");
                } else {
                    isLevelFailed = true; // Pigs remain, level is failed
                    System.out.println("Level Failed!");
                }
            }
        }
        game.batch.end();
        stage.act();
        stage.draw();

        // Debug rendering
        b2dCam.update();
        b2dr.render(world, b2dCam.combined);
    }

    private void destroyBodies(float delta) {
        for (Body body : bodiesToDestroy) {
            // If body is of instance tnt, do explosion first
            world.destroyBody(body);
            System.out.println("Body destroyed: " + body);
        }
        bodiesToDestroy.clear(); // Clear the list after destruction
    }

    @Override
    public void show() {
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
    public void dispose() {
        stage.dispose();
        uiTexture.dispose();
        moreUITexture.dispose();
    }
}


