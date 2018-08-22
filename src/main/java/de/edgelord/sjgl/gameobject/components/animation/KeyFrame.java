/*
 * Copyright (c) by Malte Dostal
 * Germany, 8.2018
 * All rights reserved
 */

package de.edgelord.sjgl.gameobject.components.animation;

public class KeyFrame {

    private int timing;
    private float key;

    public KeyFrame(int timing, float key) {
        this.timing = timing;
        this.key = key;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public float getKey() {
        return key;
    }

    public void setKey(float key) {
        this.key = key;
    }
}
