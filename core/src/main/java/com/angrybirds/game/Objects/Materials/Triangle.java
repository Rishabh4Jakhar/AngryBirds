package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Triangle extends Material {
    public Triangle(String type, Texture texture, int x, int y, int width, int height) {
        super(type, 80, new TextureRegion(texture, x, y, width, height));
    }
}
