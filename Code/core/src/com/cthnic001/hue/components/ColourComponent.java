package com.cthnic001.hue.components;

import com.cthnic001.hue.data.Colour;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent extends PoolableComponent {
    public final Colour colour;

    public ColourComponent() {
        colour = new Colour();
    }

    @Override
    public void reset() {
        colour.reset();
    }
}

