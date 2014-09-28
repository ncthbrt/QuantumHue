package co.za.cuthbert.three.components;

import co.za.cuthbert.three.value_objects.Colour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PortComponent extends Component implements Pool.Poolable {
    private final Colour[] incomingPortColours;
    private final Colour[] outgoingPortColours;
    private final boolean[] portMask;

    private boolean cullable = true;


    public static int portNumber(int relativeX, int relativeY) {
        return (3 * (relativeY + 1) + (relativeX + 1));
    }

    public static int adjacentPortNumber(int portNumber) {
        return 8 - portNumber;
    }


    public PortComponent() {
        incomingPortColours = new Colour[9];
        outgoingPortColours = new Colour[9];
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
                incomingPortColours[i].reset();
            }
        }
    }


    public void mask(int port, boolean active) {
        this.portMask[port] = active;
        if (!portMask[port]) {
            incomingPortColours[port].reset();
        }
    }

    public Colour[] incomingPortColours() {
        return incomingPortColours;
    }

    public void incomingPortColour(int port, Colour colour) {
        portMask[port] = true;
        this.incomingPortColours[port].set(colour);
    }

    public Colour incomingPortColour(int port) {
        return incomingPortColours[port];
    }

    public void incomingPortColours(Colour[] outgoingPortColours) {
        for (int i = 0; i < 9; ++i) {
            this.incomingPortColours[i] = outgoingPortColours[i];
        }
    }

    public Colour[] outgoingPortColours() {
        return outgoingPortColours;
    }

    public void outgoingPortColour(int port, Colour colour) {
        this.outgoingPortColours[port].set(colour);
    }

    public Colour outgoingPortColour(int port) {
        return outgoingPortColours[port];
    }

    public void outgoingPortColours(Colour[] outgoingPortColours) {
        for (int i = 0; i < 9; ++i) {
            this.outgoingPortColours[i] = outgoingPortColours[i];
        }
    }

    public void outgoingPortColours(Colour colour) {
        for (int i = 0; i < 9; ++i) {
            this.outgoingPortColours[i] = colour;
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < 9; i++) {
            this.incomingPortColours[i] = new Colour();
            this.outgoingPortColours[i] = new Colour();
            this.portMask[i] = false;
        }
    }
}
