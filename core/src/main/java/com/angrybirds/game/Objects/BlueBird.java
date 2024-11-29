package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public class BlueBird extends Bird {
    private boolean abilityUsed = false;
    public BlueBird(Texture birdSheet) {
        super(birdSheet, 10, 246, 33, 35);
        this.name = "Blue Bird";
        this.health = 100;
        this.speed = 0.0f;
        this.textureWidth = 33;
        this.textureHeight = 35;
    }

    public void split(World world, List<Bird> birds, Texture birdTexture) {
        if (abilityUsed || body == null) return; // Prevent multiple splits
        abilityUsed = true;

        Vector2 originalVelocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();


        Vector2 velocity1 = rotateVector(originalVelocity, 15);  // Clockwise
        Vector2 velocity2 = rotateVector(originalVelocity, -15); // Counter-clockwise


        Bird bird1 = new BlueBird(birdTexture);
        bird1.createBody(world, position.x * PPM, position.y * PPM, true);
        copyBodyProperties(body, bird1.getBody());
        bird1.getBody().setLinearVelocity(velocity1);
        bird1.setHealth(getHealth());
        bird1.isShot = true;

        Bird bird2 = new BlueBird(birdTexture);
        bird2.createBody(world, position.x * PPM, position.y * PPM, true);
        copyBodyProperties(body, bird2.getBody());
        bird2.getBody().setLinearVelocity(velocity2);
        bird2.setHealth(getHealth());
        bird2.isShot = true;

        birds.add(bird1);
        birds.add(bird2);

}

    private Vector2 rotateVector(Vector2 vector, float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        float x = vector.x * cos - vector.y * sin;
        float y = vector.x * sin + vector.y * cos;

        return new Vector2(x, y);
    }

    private void copyBodyProperties(Body originalBody, Body newBody) {
        // Copy properties like gravity scale, damping, etc.
        newBody.setGravityScale(originalBody.getGravityScale());
        newBody.setLinearDamping(originalBody.getLinearDamping());
        newBody.setAngularDamping(originalBody.getAngularDamping());
        newBody.setFixedRotation(originalBody.isFixedRotation());

        // Copy mass and inertia if necessary
        MassData massData = originalBody.getMassData();
        newBody.setMassData(massData);
    }

}
