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
            Vector2 currentVelocity = body.getLinearVelocity();
            body.setLinearVelocity(currentVelocity.scl(2f)); // Increase speed by 1.5x
            speedBoostUsed = true; // Prevent further boosts
            System.out.println("Yellow bird speed boosted!");
        }
    }

}
