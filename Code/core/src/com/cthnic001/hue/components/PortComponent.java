package com.cthnic001.hue.components;

import com.cthnic001.hue.level.Tile;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PortComponent extends PoolableComponent {
    public final boolean[] ports;

    public PortComponent() {
        ports = new boolean[8];
        reset();
    }

    public PortComponent(boolean ports[]) {
        this();
        set(ports);
    }

    public PortComponent(Tile[][][] level, int x, int y, int z) {
        this();
        set(level, x, y, z);
    }

    public void set(boolean ports[]) {
        System.arraycopy(ports, 0, this.ports, 0, 8);
    }

    public void set(Tile[][][] level, int x, int y, int z) {
        int count = 0;
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i < x + 1; i++) {
                if (j != y || i != x) {
                    if (level[z][j][i].contains(PortComponent.class)) {
                        ports[count] = true;
                    } else {
                        ports[count] = false;
                    }
                    count++;
                }
            }
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < 8; i++) {
            this.ports[i] = false;
        }
    }
}
