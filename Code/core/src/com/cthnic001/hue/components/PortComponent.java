package com.cthnic001.hue.components;

import com.cthnic001.hue.Config;
import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.entities.Level;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PortComponent extends PoolableComponent {

    public static final int TOP_LEFT = 0;
    public static final int TOP_CENTRE = 1;
    public static final int TOP_RIGHT = 2;
    public static final int MID_LEFT = 3;
    public static final int MID_RIGHT = 4;
    public static final int BOTTOM_LEFT = 5;
    public static final int BOTTOM_CENTRE = 6;
    public static final int BOTTOM_RIGHT = 7;

    private final PortComponent[] neighboringPorts;
    private final Colour[] portColours;
    private final boolean[] ports;

    public PortComponent() {
        neighboringPorts = new PortComponent[8];
        portColours = new Colour[8];
        ports = new boolean[8];
        reset();
    }


    public PortComponent(Level level, int x, int y, int z) {
        this();
        setNeighboringPorts(level, x, y, z);
    }


    public boolean[] getPortMap() {
        boolean[] map = new boolean[8];
        System.arraycopy(ports, 0, map, 0, 8);
        return map;
    }


    public Colour getIncomingPortColour(int port) {
        if (ports[port]) {
            return neighboringPorts[port].portColours[7 - port];
        } else if (Config.DEBUG) {
            throw new IllegalArgumentException("You're trying to access a non-existent port. Please check first");
        }
        return null;
    }

    public Colour getOutgoingPortColour(int port) {
        if (ports[port]) {
            return portColours[port];
        } else if (Config.DEBUG) {
            throw new IllegalArgumentException("You're trying to access a non-existent port. Please check first");
        }
        return null;
    }


    public void setOutgoingPortColours(Colour[] portColours) {
        for (int i = 0; i < 8; i++) {
            this.portColours[i] = portColours[i];
        }
    }

    public void setPortColour(int port, Colour colour) {
        if (Config.DEBUG && port < 0 || port > 7) {
            throw new IllegalArgumentException("There are only 8 possible ports.\nYou can't set the colour for a non-existent port");
        }
        portColours[port] = colour;
    }

    public void setNeighboringPorts(Level level, int x, int y, int z) {
        int count = 0;
        for (int j = Math.max(0, y - 1); j <= y + 1; j++) {
            for (int i = Math.max(0, x - 1); i < x + 1; i++) {
                if (j != y || i != x) {
                    if (level.getLevel()[z][j][i].contains(PortComponent.class)) {
                        PortComponent port = (PortComponent) level.getLevel()[z][j][i].getComponent(PortComponent.class);
                        neighboringPorts[count] = port;
                        ports[count] = true;
                    } else {
                        ports[count] = false;
                        neighboringPorts[count] = null;
                    }
                    count++;
                }
            }
        }
    }


    @Override
    public void reset() {
        for (int i = 0; i < 8; i++) {
            this.neighboringPorts[i] = null;
            this.portColours[i] = new Colour();
            this.ports[i] = false;
        }
    }
}
