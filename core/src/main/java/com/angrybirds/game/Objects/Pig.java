package com.angrybirds.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pig extends Sprite {
    public Pig(Texture pigSheet, int x, int y, int width, int height) {
        super(new TextureRegion(pigSheet, x, y, width, height));
    }
}
