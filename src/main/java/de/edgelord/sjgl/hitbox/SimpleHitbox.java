/*
 * Copyright (c) by Malte Dostal
 * Germany, 8.2018
 * All rights reserved
 */

package de.edgelord.sjgl.hitbox;

import de.edgelord.sjgl.gameobject.GameObject;
import de.edgelord.sjgl.location.Vector2f;

public class SimpleHitbox implements Hitbox {

    private final GameObject parent;
    private Vector2f position;
    private float offsetX, offsetY;
    private int width, height;

    public SimpleHitbox(final GameObject parent, final int width, final int height, final float offsetX, final float offsetY) {

        this.parent = parent;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        final float parentXPos = parent.getPosition().getX();
        final float parentYPos = parent.getPosition().getY();

        position = new Vector2f(parentXPos + offsetX, parentYPos + offsetY);

    }

    public void recalculate() {

        position = new Vector2f(parent.getPosition().getX() + offsetX, parent.getPosition().getY() + offsetY);
    }

    public boolean isLeft(final GameObject other) {

        return position.getX() + getWidth() < other.getHitbox().getPosition().getX();
    }

    public boolean isRight(final GameObject other) {

        return position.getX() > other.getHitbox().getPosition().getX() + other.getHitbox().getWidth();
    }

    public boolean isBelow(final GameObject other) {

        return position.getY() > other.getHitbox().getPosition().getY() + other.getHitbox().getHeight();
    }

    public boolean isAbove(final GameObject other) {

        return position.getY() + getHeight() < other.getHitbox().getPosition().getY();
    }

    @Override
    public boolean collides(final GameObject other) {

        if (isBelow(other)) {
            return false;
        } else if (isAbove(other)) {
            return false;
        } else if (isLeft(other)) {
            return false;
        } else {
            return !isRight(other);
        }

    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(final float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(final float offsetY) {
        this.offsetY = offsetY;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(final Vector2f position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public GameObject getParent() {
        return parent;
    }
}