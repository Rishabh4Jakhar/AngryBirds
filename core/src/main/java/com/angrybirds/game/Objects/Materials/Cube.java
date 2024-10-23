package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cube extends Material {
    public Cube(String type, Texture texture) {
        super(type, 100, new TextureRegion(texture, 803, 776, 84, 84));
    }
}
