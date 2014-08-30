package com.cthnic001.hue.components;

import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.entities.Level;

/** The wire port component lets
 * Copyright Nick Cuthbert, 2014.
 */
public class PortComponent extends PoolableComponent {

    private final PortComponent[] neighboringPorts;
    private final Colour[] portColours;
    private final boolean[] ports;

    public PortComponent() {
        neighboringPorts = new PortComponent[8];
        portColours = new Colour[8];
        ports = new boolean[8];
        for (int i = 0; i < 8; i++) {
            portColours[i] = new Colour();
            ports[i] = false;
            neighboringPorts[i] = null;
        }
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


    public Colour[] getIncomingPortColours() {
        Colour[] incoming = new Colour[8];
        for (int port = 0; port < 8; port++) {
            incoming[port] = getIncomingPortColour(port);
        }
        return incoming;
    }

    public Colour getIncomingPortColour(int port) {
        if (ports[port]) {
            return neighboringPorts[port].portColours[7 - port];
        }
        return new Colour();
    }


    public void setOutgoingPortColours(Colour[] portColours) {
        System.arraycopy(portColours, 0, this.portColours, 0, 8);
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
