package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Rectangle extends Material {
    public Rectangle(String type, Texture texture, int x, int y, int width, int height) {
        super(type, 120, new TextureRegion(texture, x, y, width, height));
    }
}
