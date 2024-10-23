package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sphere extends Material {
    public Sphere(String type, Texture texture) {
        super(type, 90, new TextureRegion(texture, 1055, 776, 84, 84));
    }
}
