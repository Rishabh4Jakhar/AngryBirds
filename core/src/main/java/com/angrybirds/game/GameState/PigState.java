package com.angrybirds.game.GameState;

import java.io.Serializable;

public class PigState implements Serializable {
    private static final long serialVersionUID = 1L;

    private float x, y; // Position
    private boolean isDead;
    private int health;
    private boolean grounded;

    public PigState(float x, float y, boolean isDead, int health, boolean grounded) {
        this.x = x;
        this.y = y;
        this.isDead = isDead;
        this.health = health;
        this.grounded = grounded;
    }

    // Getters and Setters

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }
}
