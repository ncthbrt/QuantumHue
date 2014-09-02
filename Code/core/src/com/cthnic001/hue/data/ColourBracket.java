package com.cthnic001.hue.data;

import com.cthnic001.hue.components.Poolable;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourBracket implements Poolable {
    public final Colour colour;
    private float position;

    public float position() {
        return position;
    }

    public ColourBracket() {
        colour = new Colour();
        position = 0;
    }

    public ColourBracket(Colour colour) {
        this();
        colour.set(colour);
    }

    public ColourBracket(Colour colour, float position) {
        this();
        colour.set(colour);
        this.position=position;
    }

    @Override
    public void reset() {
        colour.reset();
        position = 0;
    }

    public void advance(float delta) {
        position += delta;
    }
}
