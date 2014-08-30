package com.cthnic001.hue.components;

import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.data.ColourVector;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireComponent extends PoolableComponent {

    public final ColourVector toNexus[];
    public final ColourVector fromNexus[];

    public WireComponent() {
        toNexus = new ColourVector[8];

        fromNexus = new ColourVector[8];
        for (int i = 0; i < 8; i++) {
            toNexus[i] = new ColourVector();
            fromNexus[i] = new ColourVector();
        }

    }

    public Colour getOutgoingNexusColour() {
        Colour finalColour = new Colour();
        for (int i = 0; i < toNexus.length; i++) {
            ColourVector vector = toNexus[i];
            Colour colour = vector.line.get(vector.line.size() - 1).colour;
            finalColour.add(colour);

        }
        return finalColour;
    }

    public void advance(float delta) {
        for (int i = 0; i < toNexus.length; i++) {

        }

        for (int i = 0; i < fromNexus.length; i++) {

        }
    }

    @Override
    public void reset() {
        for (ColourVector vector : toNexus) {
            vector.reset();
        }
        for (ColourVector vector : fromNexus) {
            vector.reset();
        }
    }
}
