package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;

public class YellowBird extends Bird {
    public YellowBird(Texture birdSheet) {
        super(birdSheet, 10, 75, 64, 60);
        this.name = "Yellow Bird";
        this.health = 100;
        this.speed = 0.0f;
        this.textureWidth = 64;
        this.textureHeight = 60;
    }
}
