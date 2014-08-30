package com.cthnic001.hue.components;


import com.cthnic001.hue.DVector3;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class DPositionComponent extends PoolableComponent {
    public final DVector3 position;

    public DPositionComponent() {
        position = new DVector3();
    }

    public void set(int x, int y, int z) {
        position.set(x, y, z);
    }

    @Override
    public void reset() {
        position.reset();
    }
}
