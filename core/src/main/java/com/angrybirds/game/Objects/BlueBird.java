package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;

public class BlueBird extends Bird {
    public BlueBird(Texture birdSheet) {
        super(birdSheet, 10, 246, 33, 35);
        this.name = "Blue Bird";
        this.health = 100;
        this.speed = 0.0f;
        this.textureWidth = 33;
        this.textureHeight = 35;
    }
}
