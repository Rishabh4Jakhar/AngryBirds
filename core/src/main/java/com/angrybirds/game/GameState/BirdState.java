package com.angrybirds.game.GameState;

import java.io.Serializable;

public class BirdState implements Serializable {
    private static final long serialVersionUID = 1L;

    private float x, y; // Position
    private float velocityX, velocityY; // Velocity
    private boolean isSelected;
    private boolean isShot;
    private boolean isDead;
    private String name;

    public BirdState(float x, float y, float velocityX, float velocityY, boolean isSelected, boolean isShot, boolean isDead, String name) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.isSelected = isSelected;
        this.isShot = isShot;
        this.isDead = isDead;
        this.name = name;
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

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean shot) {
        isShot = shot;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
