package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Triangle extends Material {
    public Triangle(String type, Texture texture) {
        super(type, 80, new TextureRegion(texture, 887, 776, 84, 84));
    }
}
