package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Material extends Sprite {
    protected String type;
    protected Body body;
    protected int health;
    protected int maxHealth;
    protected TextureRegion normalTexture, damagedTexture, criticalTexture;
    protected World world;

    public Material(String type, int health, TextureRegion textureRegion) {
        super(textureRegion);
        this.type = type;
        this.health = health;
        this.maxHealth = health;
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void die() {
        System.out.println("Block destroyed! (not null)" + this.type);
        if (body != null) {
            world.destroyBody(body);
            body = null; // Remove the physics body
            System.out.println("Block destroyed! (test)" + this.type);
        }
    }

    public void takeDamage(World world, int damage) {
        this.world = world;
        health -= damage;
        // Check if body is null for debug
        if (body == null) {
            System.out.println("Block destroyed! (null)" + this.type);
        }
        if (health <= 0 && body != null) {
            System.out.println("Block destroyed! " + this.type);
            die();
        } else {
            updateTextureBasedOnHealth();
        }
    }


    private void updateTextureBasedOnHealth() {
        float healthPercentage = (float) health / maxHealth;

        if (healthPercentage > 0.5f) {
            // High health, normal texture
            System.out.println("High health " + healthPercentage);
            setRegion(new TextureRegion(normalTexture));
        } else if (healthPercentage > 0.2f) {
            System.out.println("Medium health " + healthPercentage);
            // Medium health, damaged texture
            setRegion(new TextureRegion(damagedTexture));
        } else {
            System.out.println("Low health " + healthPercentage);
            // Low health, critical texture
            setRegion(new TextureRegion(criticalTexture));
        }
    }

    public Body getBody() {
        return body;
    }
}
