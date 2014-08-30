package com.cthnic001.hue.entities;

import com.cthnic001.hue.Config;
import com.cthnic001.hue.components.PortComponent;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class TileFactory {
    public Tile manufactureWire(int x, int y, int z) {
        Tile wire = new Tile(x, y, z);
        //Adding components
        wire.addComponent(new PortComponent());

        //End Adding components

        if (Config.DEBUG) {
            if (!wire.conforms(TileType.WIRE)) {
                System.err.println("manufactureWire is creating tiles which do not meet the requirements of the wire object");
            }
        }

        return wire;
    }
}
