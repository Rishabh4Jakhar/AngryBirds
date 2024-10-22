package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slingshot extends Sprite {
    public Slingshot(Texture blockSheet) {
        super(new TextureRegion(blockSheet, 48, 215, 85, 200));
        setSize(53, 125);
    }
}
