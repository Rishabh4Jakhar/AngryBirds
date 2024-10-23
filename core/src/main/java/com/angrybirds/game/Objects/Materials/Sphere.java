package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sphere extends Material {
    public Sphere(String type, int health, Texture texture, int x, int y, int width, int height) {
        super(type, health, new TextureRegion(texture, x, y, width, height));
    }
}
