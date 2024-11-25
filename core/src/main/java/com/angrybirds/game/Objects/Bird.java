package com.angrybirds.game.Objects;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.lang.reflect.Field;
import java.util.Vector;

public class Bird extends Sprite {
    protected String name = "Bird";
    protected int health = 100;
    protected float speed = 0.0f;
    protected int textureWidth;
    protected int textureHeight;
    protected Body body;

    // Physics variables
    protected static final float PPM = 100f;
    protected static final float BIRD_DENSITY = 1.0f;
    protected static final float BIRD_FRICTION = 0.1f;
    protected static final float BIRD_RESTITUTION = 0.5f;
    protected static final float BIRD_RADIUS = 20f / PPM;


    // Shooting variables
    protected boolean isSelected = false;
    protected boolean isShot = false;
    protected Vector2 originalPosition;
    protected static final float MAX_DRAG_DISTANCE = 100f * PPM;
    protected static final float SHOOT_POWER_MULTIPLIER = 1f;
    private float rollingTime = 0; // Tracks how long the body has been rolling
    private static final float ROLLING_THRESHOLD = 0.8f; // Velocity below which it's considered rolling (adjust as needed)
    private static final float MAX_ROLLING_TIME = 2.0f;  // Maximum time allowed for rolling in seconds


    public Bird(Texture birdSheet, int x, int y, int width, int height) {
        super(new TextureRegion(birdSheet, x, y, width, height));
        this.textureWidth = width;
        this.textureHeight = height;
        originalPosition = new Vector2(x, y);
    }
    public void setBody(Body body) {
        this.body = body;
    }
    public Body getBody() {
        return body;
    }

    public void createBody(World world, float v, float v1) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(v / PPM, v1 / PPM);
        //originalPosition = new Vector2(v / PPM, v1 / PPM);
        // Create body in world
        body = world.createBody(bodyDef);

        // Create shape
        CircleShape shape = new CircleShape();
        shape.setRadius(BIRD_RADIUS);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = BIRD_DENSITY;
        fixtureDef.friction = BIRD_FRICTION;
        fixtureDef.restitution = BIRD_RESTITUTION;
        body.createFixture(fixtureDef);

        shape.dispose();

        originalPosition = new Vector2(v, v1);
        body.setType(BodyDef.BodyType.KinematicBody);
        body.setUserData(this);
    }

    public void update(float delta) {
        if (body!=null) {
            Vector2 velocity = body.getLinearVelocity();

            // Check if the body is rolling
            if (velocity.len() < ROLLING_THRESHOLD && velocity.len() > 0.1) {
                rollingTime += delta;
                if (rollingTime >= MAX_ROLLING_TIME) {
                    // Reset or "kill" the object
                    reset();
                    System.out.println("Resetting due to rolling too long!");
                }
            } else {
                // Reset the rolling timer if velocity exceeds the threshold
                rollingTime = 0;
            }
            Vector2 position = body.getPosition();
            setPosition(position.x * PPM - textureWidth / 2, position.y * PPM - textureHeight / 2);
            setRotation((float) Math.toDegrees(body.getAngle()));

            if (isShot && (isOutOfBounds() || isStopped())) {
                health = 0;
                reset();
            }
        }
    }

    public void dragTo(float x, float y) {
        if (!isShot && isSelected) {
            float distance = new Vector2(x, y).sub(originalPosition).len();
            System.out.println("DEBUGGING: 100f and " + distance);
            if (body.getType() != BodyDef.BodyType.KinematicBody) {
                body.setType(BodyDef.BodyType.KinematicBody);
            }

            if (distance <= AngryBirds.V_WIDTH*0.27f) {
                body.setTransform(x, y, 0);
                System.out.println("DEBUGGING: Bird is dragged to position (if): " + x + ", " + y);
            } //else {
                //System.out.println("DEBUGGING: Bird is dragged to position: " + x + ", " + y);
                //Vector2 direction = new Vector2(x, y).sub(originalPosition).nor();
                //Vector2 newPosition = direction.scl(MAX_DRAG_DISTANCE).add(originalPosition);
                //System.out.println("DEBUGGING (Actually): Bird is dragged to position: " + newPosition);
                //body.setTransform(newPosition, 0);
                //body.setTransform(x, y, 0);
            //}

            body.setLinearVelocity(0, 0);
        }
    }

        public void shoot(Vector2 dragVector) {
            if (!isShot && isSelected) {
                isSelected = false;
                body.setType(BodyDef.BodyType.DynamicBody);
                // Calculate velocity
                Vector2 force = dragVector.scl((SHOOT_POWER_MULTIPLIER-0.2f)/PPM);
                // Add max limit to force to prevent bird over speeding
                if (force.x > 0.95f || force.y > 0.95f) {
                    force = force.scl(0.6f);
                }
                //Vector2 velocity = new Vector2(originalPosition.x - body.getPosition().x, originalPosition.y - body.getPosition().y);
                //velocity.scl(SHOOT_POWER_MULTIPLIER);
                //body.setLinearVelocity(velocity);
                body.setLinearVelocity(0, 0);
                body.applyLinearImpulse(force, body.getWorldCenter(), true);
                isShot = true;
                System.out.println("DEBUGGING: Bird is shot" + force);
            }
        }

    public void reset() {
        if (body == null) {
            return;
        }
        System.out.println("DEBUGGING: Bird is reset to position: " + originalPosition);
        body.setTransform(originalPosition.x / PPM, originalPosition.y / PPM, 0);
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
        body.setType(BodyDef.BodyType.KinematicBody);
        isShot = false;
        isSelected = false;
        rollingTime = 0;
    }

    private boolean isOutOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x * PPM > AngryBirds.V_WIDTH || body.getPosition().y < 0 || body.getPosition().y * PPM > AngryBirds.V_HEIGHT;
    }

    private boolean isStopped() {
        return body.getLinearVelocity().len() < 0.1f;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public static float getBirdDensity() {
        return BIRD_DENSITY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isShot() {
        return isShot;
    }

    public Vector2 getOriginalPosition() {
        return originalPosition;
    }
}
