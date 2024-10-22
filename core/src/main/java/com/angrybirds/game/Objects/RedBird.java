package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;

public class RedBird extends Bird {
    public RedBird(Texture birdSheet) {
        super(birdSheet, 10, 16, 45, 44);
        this.name = "Red Bird";
        this.health = 150;
        this.speed = 0.0f;
        this.textureWidth = 45;
        this.textureHeight = 44;
    }
}
