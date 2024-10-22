package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bird extends Sprite {
    protected String name = "Bird";
    protected int health = 100;
    protected float speed = 0.0f;
    protected int textureWidth;
    protected int textureHeight;
    public Bird(Texture birdSheet, int x, int y, int width, int height) {
        super(new TextureRegion(birdSheet, x, y, width, height));
    }
}
