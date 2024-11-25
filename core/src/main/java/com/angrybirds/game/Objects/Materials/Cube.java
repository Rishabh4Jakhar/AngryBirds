package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Cube extends Material {
    private static final long serialVersionUID = 1L;
    protected Body body;
    protected static final float PPM = 100f;
    protected static final float RECTANGLE_DENSITY = 1.0f;
    protected static final float RECTANGLE_FRICTION = 0.5f;
    protected static final float RECTANGLE_RESTITUTION = 0.2f;
    protected Vector2 originalPosition;

    public Cube(String type, int health, Texture texture, int x, int y, int width, int height) {
        super(type, health, new TextureRegion(texture, x, y, width, height));
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

    public int getHealth() {
        return health;
    }
}
