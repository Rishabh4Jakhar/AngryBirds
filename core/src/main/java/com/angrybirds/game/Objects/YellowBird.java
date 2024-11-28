package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class YellowBird extends Bird {
    private boolean speedBoostUsed = false; // Prevent multiple boosts

    public YellowBird(Texture birdSheet) {
        super(birdSheet, 10, 75, 64, 60);
        this.name = "Yellow Bird";
        this.health = 100;
        this.speed = 0.0f;
        this.textureWidth = 64;
        this.textureHeight = 60;
    }

    public void applySpeedBoost() {
        if (!speedBoostUsed && body != null) {
            Vector2 velocity = body.getLinearVelocity();
            // Make bird move entirely horizontally
            float boostedSpeed = Math.abs(velocity.x)*2f;
            if (boostedSpeed > 10f) {
                boostedSpeed = 10f; // Cap speed at 10
            } else if (boostedSpeed < 5f) {
                boostedSpeed = 5f; // Minimum speed of 5
            }


            // Determine direction based on current x-velocity
            float direction = Math.signum(velocity.x); // +1 for right, -1 for left
            body.setLinearVelocity(boostedSpeed * direction, 0); // Horizontal motion only
            speedBoostUsed = true; // Prevent further boosts
            System.out.println("Yellow bird speed boosted!");
        }
    }

}
