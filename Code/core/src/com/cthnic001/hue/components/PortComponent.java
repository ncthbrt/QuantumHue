package com.cthnic001.hue.components;

import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.entities.Level;
import com.cthnic001.hue.entities.Tile;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PortComponent extends PoolableComponent {
    public final boolean[] ports;
    public final Colour[] portColours;
    private Level level;

    public PortComponent() {
        ports = new boolean[8];
        portColours = new Colour[8];
        reset();
    }


    public PortComponent(Level level, int x, int y, int z) {
        this();
        set(level, x, y, z);
    }

    public void set(Level level, int x, int y, int z) {
        this.level = level;
        int count = 0;
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i < x + 1; i++) {
                if (j != y || i != x) {
                    if (level.getLevel()[z][j][i].contains(PortComponent.class)) {
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
            portColours[i] = new Colour();
            this.ports[i] = false;
        }
        this.level = null;
    }
}
