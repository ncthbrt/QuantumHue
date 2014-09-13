package co.za.cuthbert.three.components;


import co.za.cuthbert.three.components.interfaces.ADVector3;
import com.badlogic.gdx.utils.Pool;

/**
 * A class to store discrete coordinates in three dimensions. Especially useful for tile based games
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector3 extends ADVector3 implements Pool.Poolable{
    public static final String TYPE_NAME="discrete-position";
    @Override
    public String getComponentName() {
        return TYPE_NAME;
    }
    private int x, y, z;

    public DVector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DVector3() {
        reset();
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int z() {
        return z;
    }

    @Override
    public void x(int x) {
        this.x=x;
    }

    @Override
    public void y(int y) {
        this.y=y;
    }

    @Override
    public void z(int z) {
        this.z=z;
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