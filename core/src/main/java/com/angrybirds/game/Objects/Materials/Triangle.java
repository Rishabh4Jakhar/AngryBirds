package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Triangle extends Material {
    private static final long serialVersionUID = 1L;
    protected Body body;
    protected int textureWidth;
    protected int textureHeight;
    protected static final float PPM = 100f;
    private static final float MATERIAL_DENSITY = 1.0f;
    private static final float MATERIAL_FRICTION = 0.5f;
    private static final float MATERIAL_RESTITUTION = 0.2f;
    private Vector2 originalPosition;

    public Triangle(String type, int health, Texture texture, int x, int y, int width, int height) {
        super(type, health, new TextureRegion(texture, x, y, width, height));
        this.textureWidth = width;
        this.textureHeight = height;
        setSize(60, 60);
        setOrigin(getWidth() / 2, getHeight() / 2);
        this.normalTexture = new TextureRegion(texture, x, 860, width, height);
        this.damagedTexture = new TextureRegion(texture, x, 944, width, height);
        this.criticalTexture = new TextureRegion(texture, x, 1028, width, height);
    }
    public void createBody(World world, float x, float y, float base, float height, boolean isStatic) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x + base / 2) / PPM, (y + height / 2) / PPM); // Center the triangle

        body = world.createBody(bodyDef);

        // Define the triangle shape
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-base / 2 / PPM, -height / 2 / PPM); // Bottom-left
        vertices[1] = new Vector2(base / 2 / PPM, -height / 2 / PPM);  // Bottom-right
        vertices[2] = new Vector2(0, height / 2 / PPM);                // Top-center
        shape.set(vertices);

        // Define fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = MATERIAL_DENSITY;
        fixtureDef.friction = MATERIAL_FRICTION;
        fixtureDef.restitution = MATERIAL_RESTITUTION;
        body.createFixture(fixtureDef);

        shape.dispose();

        originalPosition = new Vector2(x, y);
        body.setUserData(this); // Link  this object to the body
    }

    public void update() {
        // Sync game object position with Box2D body
        if (body != null) {
            Vector2 position = body.getPosition();
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getAngle() {
        return body.getAngle();
    }

    public Body getBody() {
        return body;
    }
}
