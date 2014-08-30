package com.cthnic001.hue.level;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class TileFactory {
    public Tile manufactureWire(int x, int y, int z) {
        return new Tile(x, y, z);
    }
}
