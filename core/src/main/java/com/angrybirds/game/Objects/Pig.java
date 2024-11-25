package com.angrybirds.game.Objects;

import com.angrybirds.game.AngryBirds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Pig extends Sprite implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Body body;
    protected String name = "Pig";
    protected int health = 100;
    protected int textureWidth;
    protected int textureHeight;

    // Physics constants
    protected static final float PPM = 100f;
    protected static final float PIG_DENSITY = 1.0f;
    protected static final float PIG_FRICTION = 0.3f;
    protected static final float PIG_RESTITUTION = 0.5f;
    protected static final float PIG_RADIUS = 28f / PPM;

    // Original position of the pig
    protected Vector2 originalPosition;

    public Pig(Texture pigSheet, int x, int y, int width, int height) {
        super(new TextureRegion(pigSheet, x, y, width, height));
        this.textureWidth = width;
        this.textureHeight = height;
        originalPosition = new Vector2(x, y);
        setSize(60, 60);
        setOrigin(getWidth() / 2, getHeight() / 2);
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
        bodyDef.position.set(v / PPM, (v1 + 100) / PPM);

        // Create body in world
        body = world.createBody(bodyDef);

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
    }

    public void update() {
        if (body!=null) {
            Vector2 position = body.getPosition();
            //setPosition(position.x * PPM - 30, position.y * PPM - 30);
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            setRotation((float) Math.toDegrees(body.getAngle()));
            if (isOutOfBounds()) {
                health = 0;
            }
        }
    }

    private boolean isOutOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x * PPM > AngryBirds.V_WIDTH || body.getPosition().y < 0 || body.getPosition().y * PPM > AngryBirds.V_HEIGHT;
    }
}


