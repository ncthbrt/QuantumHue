package co.za.cuthbert.three.components;

import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PortComponent extends Component implements Pool.Poolable {
    private final DiscreteColour[] incomingPortColours;
    private final DiscreteColour[] outgoingPortColours;
    private final boolean[] portMask;

    private boolean cullable = true;


    public static int portNumber(int relativeX, int relativeY) {
        return (3 * (relativeY + 1) + (relativeX + 1));
    }

    public static int adjacentPortNumber(int portNumber) {
        return 8 - portNumber;
    }


    public PortComponent() {
        incomingPortColours = new DiscreteColour[9];
        outgoingPortColours = new DiscreteColour[9];
        portMask = new boolean[9];
        reset();
    }

    public boolean[] portMask() {
        return portMask;
    }

    public void mask(boolean[] portMask) {
        for (int i = 0; i < 9; ++i) {
            this.portMask[i] = portMask[i];
            if (!portMask[i]) {
                incomingPortColours[i]=DiscreteColour.ALPHA;
            }
        }
    }


    public void mask(int port, boolean active) {
        this.portMask[port] = active;
        if (!portMask[port]) {
            incomingPortColours[port]=DiscreteColour.ALPHA;
        }
    }

    public DiscreteColour[] incomingPortColours() {
        return incomingPortColours;
    }

    public void incomingPortColour(int port, DiscreteColour colour) {
        portMask[port] = true;
        this.incomingPortColours[port]=(colour);
    }

    public DiscreteColour incomingPortColour(int port) {
        return incomingPortColours[port];
    }

    public void incomingPortColours(DiscreteColour[] outgoingPortColours) {
        for (int i = 0; i < 9; ++i) {
            this.incomingPortColours[i] = outgoingPortColours[i];
        }
    }

    public DiscreteColour[] outgoingPortColours() {
        return outgoingPortColours;
    }

    public void outgoingPortColour(int port, DiscreteColour colour) {
        this.outgoingPortColours[port]=(colour);
    }

    public DiscreteColour outgoingPortColour(int port) {
        return outgoingPortColours[port];
    }

    public void outgoingPortColours(DiscreteColour[] outgoingPortColours) {
        for (int i = 0; i < 9; ++i) {
            this.outgoingPortColours[i] = outgoingPortColours[i];
        }
    }

    public void outgoingPortColours(DiscreteColour colour) {
        for (int i = 0; i < 9; ++i) {
            this.outgoingPortColours[i] = colour;
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < 9; i++) {
            this.incomingPortColours[i] = DiscreteColour.ALPHA;
            this.outgoingPortColours[i] = DiscreteColour.ALPHA;
            this.portMask[i] = false;
        }
    }
}
