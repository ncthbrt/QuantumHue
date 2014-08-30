package com.cthnic001.hue.data;

import com.cthnic001.hue.components.Poolable;

/**
 * A class to store discrete coordinates in three dimensions. Especially useful for tile based games
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector3 implements Poolable {
    public int x, y, z;

    public DVector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DVector3() {
        reset();
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DVector3) {
            DVector3 other = (DVector3) obj;
            return x == other.x && y == other.y && z == other.z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x + y * 17 + z * 23;
    }
}
