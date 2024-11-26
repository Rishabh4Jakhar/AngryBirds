package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public abstract class Material extends Sprite {
    protected String type;
    private Body body;
    protected int health;
    protected int maxHealth;
    protected TextureRegion normalTexture, damagedTexture, criticalTexture;
    protected World world;
    private boolean isDead = false;

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

    public void die(List<Body> bodiesToDestroy) {
        System.out.println("Body state in die: " + body);
        System.out.println("Block destroyed! (not null)" + this.type);
        if (isDead) {
            System.out.println("Block already destroyed! " + this.type);
            return;
        }
        isDead = true;
        if (body != null) {
            //world.destroyBody(body);
            bodiesToDestroy.add(body);
            body = null; // Remove the physics body
            System.out.println("Block destroyed! (test)" + this.type);
        }
    }

    public void takeDamage(World world, int damage, List<Body> bodiesToDestroy) {
        System.out.println("Body state in takeDamage: " + body);
        this.world = world;
        health -= damage;
        System.out.println("Health: " + health + ", Body: " + body + ", Type: " + this.type);
        // Check if body is null for debug
        if (body == null) {
            System.out.println("Block destroyed! (null)" + this.type);
        }
        if (health <= 0 && body != null) {
            System.out.println("Block destroyed! " + this.type);
            die(bodiesToDestroy);
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

    void setBody(Body body) {
        if (this.body == null) {
            this.body = body;
            System.out.println("Body set for: " + this.type);
        } else {
            throw new IllegalStateException("Body already set for: " + this.type);
        }
    }
}
