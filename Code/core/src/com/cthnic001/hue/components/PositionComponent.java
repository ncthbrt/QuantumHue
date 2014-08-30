package com.cthnic001.hue.components;


import com.badlogic.gdx.math.Vector2;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PositionComponent extends PoolableComponent {
    public final Vector2 position;

    public PositionComponent() {
        position = new Vector2();

    }

    public PositionComponent(float x, float y) {
        position = new Vector2(x, y);
    }

    public PositionComponent(Vector2 position) {
        this.position = new Vector2(position.x, position.y);
    }

    @Override
    public void reset() {
        position.set(0, 0);
    }
}
