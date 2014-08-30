package com.cthnic001.hue.components;

import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.data.ColourVector;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireComponent extends PoolableComponent {

    public final ColourVector toNexus[];
    public final ColourVector fromNexus[];
    private Colour[] incomingPortColours;
    public WireComponent() {
        toNexus = new ColourVector[8];
        incomingPortColours = new Colour[8];
        fromNexus = new ColourVector[8];
        for (int i = 0; i < 8; i++) {
            toNexus[i] = new ColourVector();
            fromNexus[i] = new ColourVector();
            incomingPortColours[i] = new Colour();
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

    public Colour[] getOutgoingPortColours() {
        Colour[] outgoingPortColours = new Colour[8];
        for (int i = 0; i < fromNexus.length; i++) {
            outgoingPortColours[i] = fromNexus[i].line.get(fromNexus[i].line.size() - 1).colour;
        }
        return outgoingPortColours;
    }

    public void advance(float delta) {

        for (int i = 0; i < toNexus.length; i++) {

        }

        for (int i = 0; i < fromNexus.length; i++) {

        }
    }

    public void setIncomingPortColours(Colour[] colours) {
        for (int i = 0; i < incomingPortColours.length; i++) {
            incomingPortColours[i] = colours[i];
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

        for (Colour incomingPortColour : incomingPortColours) {
            incomingPortColour.reset();
        }
    }
}
