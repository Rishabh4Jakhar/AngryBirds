package com.angrybirds.game.GameState;

import java.io.Serializable;

public class MaterialState implements Serializable {
    private static final long serialVersionUID = 1L;

    private float x, y; // Position
    private float angle; // Angle
    private boolean isDead;
    private int health;
    private String type;
    private boolean grounded;

    public MaterialState(float x, float y, float angle, boolean isDead, int health, String type, boolean grounded) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.isDead = isDead;
        this.health = health;
        this.type = type;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }
}
