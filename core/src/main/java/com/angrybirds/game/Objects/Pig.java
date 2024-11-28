package com.angrybirds.game.Objects;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;
import java.util.List;

public class Pig extends Sprite implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Body body;
    protected String name = "Pig";
    protected int health = 100;
    private int maxHealth = 100;
    private int type = 1;
    protected int textureWidth;
    protected int textureHeight;
    private TextureRegion normalTexture, damagedTexture, criticalTexture;

    // Physics constants
    protected static final float PPM = 100f;
    protected static final float PIG_DENSITY = 1.0f;
    protected static final float PIG_FRICTION = 0.3f;
    protected static final float PIG_RESTITUTION = 0.5f;
    protected static final float PIG_RADIUS = 28f / PPM;
    private static final float GROUND_Y = 0.0f;
    // Original position of the pig
    protected Vector2 originalPosition;
    private boolean grounded = false;

    private float rollingTime = 0; // Tracks how long the body has been rolling
    private static final float ROLLING_THRESHOLD = 0.7f; // Velocity below which it's considered rolling (adjust as needed)
    private static final float MAX_ROLLING_TIME = 1f;  // Maximum time allowed for rolling in seconds
    private World world;

    public Pig(Texture pigSheet, int x, int y, int width, int height) {
        super(new TextureRegion(pigSheet, x, y, width, height));
        this.textureWidth = width;
        this.textureHeight = height;
        originalPosition = new Vector2(x, y);
        setSize(60, 60);
        setOrigin(getWidth() / 2, getHeight() / 2);
        normalTexture = new TextureRegion(pigSheet, 2953, y, width, height);
        damagedTexture = new TextureRegion(pigSheet, 3063, y, width, height);
        criticalTexture = new TextureRegion(pigSheet, 3173, y, width, height);
    }

    public Pig(Texture pigSheet, int x, int y, int width, int height, int type) {
        super(new TextureRegion(pigSheet, x, y, width, height));
        this.textureWidth = width;
        this.textureHeight = height;
        originalPosition = new Vector2(x, y);
        this.type = type;
        if (type == 2) {
            health = 150;
            setSize(80, 80);
            normalTexture = new TextureRegion(pigSheet, 2956, y, width-1, height);
            damagedTexture = new TextureRegion(pigSheet, 3067, y, width, height);
            criticalTexture = new TextureRegion(pigSheet, 3291, y, width, height);
        } else if (type == 3) {
            setSize(100, 100);
            health = 200;
            normalTexture = new TextureRegion(pigSheet, x, y, width, height);
            damagedTexture = new TextureRegion(pigSheet, x, y, width, height);
            criticalTexture = new TextureRegion(pigSheet, x, y, width, height);
        } else {
            setSize(60, 60);
            normalTexture = new TextureRegion(pigSheet, 2953, y, width, height);
            damagedTexture = new TextureRegion(pigSheet, 3063, y, width, height);
            criticalTexture = new TextureRegion(pigSheet, 3173, y, width, height);
        }
        setOrigin(getWidth() / 2, getHeight() / 2);

    }



    public void setBody(Body body) {
        this.body = body;
    }
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }
    public boolean isGrounded() {
        return grounded;
    }
    public Body getBody() {
        return body;
    }
    public void createBody(World world, float v, float v1, int type) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(v / PPM, (v1 + 100) / PPM);

        // Create body in world
        body = world.createBody(bodyDef);
        this.world = world;

        // Create shape
        CircleShape shape = new CircleShape();
        if (type == 2) {
            shape.setRadius(PIG_RADIUS*1.4f);
        } else if (type == 3) {
            shape.setRadius(PIG_RADIUS*1.8f);
        } else {
            shape.setRadius(PIG_RADIUS);
        }
        //shape.setRadius(PIG_RADIUS);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = PIG_DENSITY;
        fixtureDef.friction = PIG_FRICTION;
        fixtureDef.restitution = PIG_RESTITUTION;
        body.createFixture(fixtureDef);

        shape.dispose();

        originalPosition = new Vector2(v, v1);
        body.setUserData(this);
        if (Math.abs(v1 - GROUND_Y) < 1.0f) { // GROUND_Y is the ground level in world coordinates
            setGrounded(true);
        }
    }

    public void createBody(World world, float v, float v1) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(v / PPM, (v1 + 100) / PPM);

        // Create body in world
        body = world.createBody(bodyDef);
        this.world = world;

        // Create shape
        CircleShape shape = new CircleShape();
        shape.setRadius(PIG_RADIUS);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = PIG_DENSITY;
        fixtureDef.friction = PIG_FRICTION;
        fixtureDef.restitution = PIG_RESTITUTION;
        body.createFixture(fixtureDef);

        shape.dispose();

        originalPosition = new Vector2(v, v1);
        body.setUserData(this);
        if (Math.abs(v1 - GROUND_Y) < 1.0f) { // GROUND_Y is the ground level in world coordinates
            setGrounded(true);
        }
    }

    // Overload update function with no parameter, just to sync positions with physics body
    public void update() {
        if (body != null) {
            Vector2 position = body.getPosition();
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    public void update(float delta, List<Body> bodiesToDestroy) {
        if (body!=null) {
            Vector2 velocity = body.getLinearVelocity();

            // Check if the body is rolling
            if (velocity.len() < ROLLING_THRESHOLD && velocity.len() > 0.1) {
                rollingTime += delta;
                if (rollingTime >= MAX_ROLLING_TIME) {
                    // Reset or "kill" the object
                    die(bodiesToDestroy);
                    System.out.println("Resetting due to rolling too long!");
                    return;
                }
            } else {
                // Reset the rolling timer if velocity exceeds the threshold
                rollingTime = 0;
            }

            Vector2 position = body.getPosition();
            //setPosition(position.x * PPM - 30, position.y * PPM - 30);
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            setRotation((float) Math.toDegrees(body.getAngle()));
            if (isOutOfBounds()) {
                health = 0;
                //die(bodiesToDestroy);
            }
        }
    }

    public void die(List<Body> bodiesToDestroy) {
        if (body != null) {
            bodiesToDestroy.add(body);
            //world.destroyBody(body); // Remove the body from the physics world
            body = null;
            rollingTime = 0; // Reset the rolling timer
            System.out.println("Pig has been killed!");
        }
    }
    public void render(SpriteBatch batch) {
        this.draw(batch); // Use the sprite's updated position and rotation
    }
    public boolean isOutOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x * PPM > AngryBirds.V_WIDTH || body.getPosition().y < 0 || body.getPosition().y * PPM > AngryBirds.V_HEIGHT;
    }

    public void takeDamage(int damage, List<Body> bodiesToDestroy) {
        health -= damage;
        if (health <= 0) {
            die(bodiesToDestroy); // Call the die method when health reaches zero
        } else {
            updateTextureBasedOnHealth();
        }
    }

    private void updateTextureBasedOnHealth() {
        float healthPercentage = (float) health / maxHealth;
        if (healthPercentage > 0.8f) {
            // Full health, normal texture
            //System.out.println("Pig health: " + healthPercentage);
        } else if (healthPercentage > 0.5f) {
            // High health, normal texture
            //System.out.println("Pig health: " + healthPercentage);
            setRegion(new TextureRegion(normalTexture));
        } else if (healthPercentage > 0.2f) {
            // Medium health, damaged texture
            setRegion(new TextureRegion(damagedTexture));
        } else {
            // Low health, critical texture
            setRegion(new TextureRegion(criticalTexture));
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void setDead(boolean dead) {
        if (dead) {
            health = 0;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }
}


