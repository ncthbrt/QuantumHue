package com.deepwallgames.quantumhue.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * A class to store discrete coordinates in three dimensions. Especially useful for tile based games
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector2 implements Pool.Poolable, Component {
    public static final String TYPE_NAME="discrete-position";
    public String getComponentName() {
        return TYPE_NAME;
    }
    private int x, y;

    public DVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public DVector2() {
        reset();
    }


    public int x() {
        return x;
    }


    public int y() {
        return y;
    }


    public void x(int x) {
        this.x=x;
    }


    public void y(int y) {
        this.y=y;
    }


    public void set(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public void reset() {
        this.x = 0;
        this.y = 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DVector2) {
            DVector2 other = (DVector2) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }

    public static DVector2 delta(DVector2 vector1, DVector2 vector2){
        return new DVector2(vector1.x-vector2.x,vector1.y-vector2.y);
    }

    public static int chebyshevDistance(DVector2 delta){
        return delta.x()>delta.y()? delta.x(): delta.y();
    }

    @Override
    public int hashCode() {
        return x + y * 17;
    }

    @Override
    public String toString() {
        return "("+x()+", "+y()+")";
    }
}