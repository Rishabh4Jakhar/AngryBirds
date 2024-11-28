package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Rectangle extends Material implements Serializable {
    private static final long serialVersionUID = 1L;
    private int textureHeight;
    private int no = 1;
    private int textureWidth;
    private boolean isVertical;
    protected Body body;
    protected static final float PPM = 100f;
    protected static final float RECTANGLE_DENSITY = 1.0f;
    protected static final float RECTANGLE_FRICTION = 0.5f;
    protected static final float RECTANGLE_RESTITUTION = 0.2f;
    protected Vector2 originalPosition;


    public Rectangle(String type, int health, Texture texture, int x, int y, int width, int height, int setWidth, int setHeight, boolean isVertical) {
        super(type, health, new TextureRegion(texture, x, y, width, height));
        this.textureWidth=width;
        this.textureHeight=height;
        setSize(setWidth, setHeight);
        setOrigin(getWidth() / 2, getHeight() / 2);
        this.normalTexture = new TextureRegion(texture, x, 860, width, height);
        this.damagedTexture = new TextureRegion(texture, x, 944, width, height);
        this.criticalTexture = new TextureRegion(texture, x, 1028, width, height);
        this.isVertical = isVertical;
        // Rotate the sprite by 90 degrees if it's vertical
        if (isVertical) {
            setRotation(90); // Rotate sprite to vertical position
        }

    }

    public Rectangle(String type, int health, Texture texture, int x, int y, int width, int height, int setWidth, int setHeight, boolean isVertical, int no) {
        super(type, health, new TextureRegion(texture, x, y, width, height));
        this.textureWidth=width;
        this.textureHeight=height;
        setSize(setWidth, setHeight);
        setOrigin(getWidth() / 2, getHeight() / 2);
        if (no == 3) {
        this.normalTexture = new TextureRegion(texture, x, 860, width, height);
        this.damagedTexture = new TextureRegion(texture, x, 944, width, height);
        this.criticalTexture = new TextureRegion(texture, x, 1028, width, height);
        } else if (no == 2) {
            this.normalTexture = new TextureRegion(texture, x, 1323, width, height);
            this.damagedTexture = new TextureRegion(texture, x, 1301, width, height);
            this.criticalTexture = new TextureRegion(texture, x, 1367, width, height);
        }
        this.isVertical = isVertical;
        // Rotate the sprite by 90 degrees if it's vertical
        if (isVertical) {
            setRotation(90); // Rotate sprite to vertical position
        }

    }

    public void createBody(World world, float x, float y, float width, float height, boolean isStatic) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x + width / 2) / PPM, (y + height / 2) / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = RECTANGLE_DENSITY;
        fixtureDef.friction = RECTANGLE_FRICTION;
        fixtureDef.restitution = RECTANGLE_RESTITUTION;
        body.createFixture(fixtureDef);

        shape.dispose();

        originalPosition = new Vector2(x, y);
        body.setUserData(this); // Link this object with the body
        setBody(body);
        if (Math.abs(y - GROUND_Y) < 1.0f) { // GROUND_Y is the ground level in world coordinates
            setGrounded(true);
        }
    }

    public void update() {
        if (body!=null) {
            Vector2 position = body.getPosition();
            setOriginCenter();
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            if (isVertical) {
                setRotation(90 + (float) Math.toDegrees(body.getAngle()));
            } else {
                setRotation((float) Math.toDegrees(body.getAngle()));
            }
        }
    }

    public void reduceHP(int amount) {
        health -= amount;
        if (health <= 0) {
            onDestroy();
        }
    }

    private void onDestroy() {
        body.getWorld().destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    public int getHealth() {
        return health;
    }
}

