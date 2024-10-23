package com.angrybirds.game.Objects.Materials;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Material extends Sprite {
    protected String type;
    protected int health;

    public Material(String type, int health, TextureRegion textureRegion) {
        super(textureRegion);
        this.type = type;
        this.health = health;
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }
}
