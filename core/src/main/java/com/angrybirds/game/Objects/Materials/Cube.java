package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cube extends Material {
    public Cube(String type, Texture texture, int x, int y, int width, int height) {
        super(type, 100, new TextureRegion(texture, x, y, width, height));
    }
}
