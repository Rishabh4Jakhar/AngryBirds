package com.angrybirds.game.Objects.Materials;

import com.angrybirds.game.Objects.Materials.Material;
import com.angrybirds.game.Objects.Pig;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class TNT extends Material {
    private Body body;
    protected static final float PPM = 100f;
    private static final float EXPLOSION_RADIUS = 1.6f; // Radius in Box2D units
    private static final int EXPLOSION_DAMAGE = 90; // Damage to apply to nearby objects
    private boolean exploded = false;
    private boolean isExploding = false;
    private float currentExplosionRadius = 0f; // Radius of the current explosion animation
    private float explosionTime = 0f;         // Time elapsed since explosion
    private static final float MAX_EXPLOSION_TIME = 50f; // Explosion lasts for 0.5 seconds
    private boolean showExplosion = false; // Whether to show the explosion animation

    public TNT(Texture texture, int x, int y, int width, int height) {
        super("TNT", 10, new TextureRegion(texture, x, y, width, height)); // Initialize the sprite with the given texture
        //setRegion(new TextureRegion(texture, (int) x, (int) y, (int) width, (int) height)); // Set the texture region
        //System.out.println("TNT Texture: " + new TextureRegion(texture, x, y, width, height));
        setSize(width, height); // Set the size of the sprite
        setOriginCenter(); // Set origin to center for proper rotation and scaling
        this.normalTexture = new TextureRegion(texture, x, y, width, height);
        this.damagedTexture = new TextureRegion(texture, x, y, width, height);
        this.criticalTexture = new TextureRegion(texture, x, y, width, height);
        //createBody(world, x, y, width, height); // Create the physical body
    }

    // Create the physical body for the TNT
    public void createBody(World world, float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // TNT is static by default
        bodyDef.position.set(x / PPM, y / PPM); // Convert position to Box2D units
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM); // Set shape dimensions

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // Adjust density as needed
        fixtureDef.friction = 0.4f; // Adjust friction
        fixtureDef.restitution = 0.2f; // Adjust restitution (bounciness)

        body.createFixture(fixtureDef);
        shape.dispose();

        // Assign TNT itself as the user data for collision detection
        body.setUserData(this);
    }
    // Update the sprite position and rotation to match the body
    public void update(float delta) {
        if (body != null) {
            Vector2 position = body.getPosition();
            setPosition(position.x * PPM - getWidth() / 2, position.y * PPM - getHeight() / 2);
            //setPosition(0, 0);
            //System.out.println("TNT position: " + (position.x * PPM - getWidth()/2) + ", " + (position.y * PPM - getHeight()/2));
            // TNT Body Position
            //System.out.println("TNT body position: " + body.getPosition().x + ", " + body.getPosition().y);
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
        //System.out.println("TNT Update");
        //System.out.println("show explosion: " + showExplosion);
    }

    public void showAnimation(float delta) {
        if (isExploding) {
            System.out.println("TNT Exploding");
            explosionTime += delta;

            // Expand the explosion radius over time
            currentExplosionRadius = EXPLOSION_RADIUS * (explosionTime / MAX_EXPLOSION_TIME);

            // End explosion animation after max time
            if (explosionTime >= MAX_EXPLOSION_TIME) {
                showExplosion = false; // End the explosion animation
            }
        }
    }

    // Render the TNT sprite
    //public void render(SpriteBatch batch) {
    //    draw(batch); // Use Sprite's draw method
    //}

    // Getter for the TNT body
    public Body getBody() {
        return body;
    }

    public void explode(World world, List<Body> bodiesToDestroy, ArrayList<Rectangle> rods, List<Pig> pigBodies, List<Material> blockBodies) {
        if (exploded) return; // Prevent multiple explosions

        //System.out.println("TNT exploded!");
        exploded = true; // Mark TNT as exploded
        showExplosion = true;
        isExploding = true;
        explosionTime = 0f;
        Vector2 tntPosition = body.getPosition();

        // Damage objects within the explosion radius
        for (Material material : rods) {
            //System.out.println("Material tnt : " + material.getBody());
            if (material != this) { // Skip self
                if (material.getBody() == null) {
                    //System.out.println("Material body is null");
                    continue;
                }
                Vector2 materialPosition = material.getBody().getPosition();
                float distance = materialPosition.dst(tntPosition);
                //System.out.println("Distance for tnt explosion: " + distance);
                if (distance <= EXPLOSION_RADIUS) {
                    //System.out.println("Material Body tnt: " + material.getBody());
                    material.takeDamage(world, EXPLOSION_DAMAGE, bodiesToDestroy, blockBodies);
                    System.out.println(material.getType() + " damaged by explosion. Remaining health: " + material.getHealth());
                }
            }
        }

        // Damage pigs within the explosion radius
        for (Pig pig : pigBodies) {
            Vector2 pigPosition = pig.getBody().getPosition();
            float distance = pigPosition.dst(tntPosition);

            if (distance <= EXPLOSION_RADIUS) {
                pig.takeDamage(EXPLOSION_DAMAGE, bodiesToDestroy);
                System.out.println("Pig damaged by explosion. Remaining health: " + pig.getHealth());
            }
        }


        // Destroy the TNT body and remove it from the game world
        if (body != null) {
            bodiesToDestroy.add(body);
            //world.destroyBody(body);
            body = null;
        }
    }

    // Render the explosion animation
    public void renderRadius(ShapeRenderer shapeRenderer) {
        if (isExploding) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0.5f, 0, 0.5f); // Semi-transparent orange
            shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, currentExplosionRadius * PPM);
            shapeRenderer.end();
        }
    }
    public boolean isAnimationComplete() {
        return !isExploding; // Check if the animation has finished
    }
}

