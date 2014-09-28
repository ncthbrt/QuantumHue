package co.za.cuthbert.three.components;

import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.ColourVector;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class WireComponent extends Component implements Pool.Poolable {

    private final ColourVector toNexus[];
    private final ColourVector fromNexus[];
    private Colour[] incomingPortColours;

    public WireComponent() {
        toNexus = new ColourVector[9];
        fromNexus = new ColourVector[9];
        incomingPortColours = new Colour[9];
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                toNexus[i] = new ColourVector();
                fromNexus[i] = new ColourVector();
                incomingPortColours[i] = new Colour();
            }
        }
    }

    @Override
    public void reset() {
        for (ColourVector vector : toNexus) {
            if (vector != null)
                vector.reset();
        }
        for (ColourVector vector : fromNexus) {
            if (vector != null)
                vector.reset();
        }
        for (Colour colour : incomingPortColours) {
            if (colour != null) {
                colour.reset();
            }
        }
    }

    public void incomingPortColours(Colour[] colours) {
        for (int i = 0; i < 9; ++i) {
            if (i != 4 && colours[i] != null) {
                incomingPortColours[i].set(colours[i]);
            }
        }
    }

    public void incomingPortColour(int port, Colour colour) {
        incomingPortColours[port].set(colour);
    }

    public Colour outgoingColour(int port) {
        return fromNexus[port].last().colour;

    }

    public Colour[] outgoingColours() {
        Colour[] out = new Colour[9];
        for (int i = 0; i < 9; ++i) {
            if (i != 4) {
                out[i] = outgoingColour(i);
            }
        }
        return out;
    }

    public void advance(float delta) {
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                toNexus[i].advance(delta, incomingPortColours[i]);
            }
        }

        Colour outgoingNexusColour = outgoingNexusColour();
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                Colour feedColour = Colour.subtract(outgoingNexusColour, (toNexus[i].last().colour));
                fromNexus[i].advance(delta, feedColour);
            }
        }
    }

    public Colour outgoingNexusColour() {
        Colour finalColour = new Colour();
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                Colour colour = toNexus[i].last().colour;
                finalColour.add(colour);
            }
        }
        return finalColour;
    }


    public ColourVector resultantVector(int port) {
        ColourVector from = fromNexus[port];
        ColourVector to = toNexus[port];
        return ColourVector.mergeVectors(from, to);
    }

    public Colour[] incomingPortColours() {
        return incomingPortColours;
    }
}
