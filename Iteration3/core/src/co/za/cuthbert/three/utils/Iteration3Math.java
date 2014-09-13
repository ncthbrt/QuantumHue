package co.za.cuthbert.three.utils;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Iteration3Math {
    static public final float lerp(float start, float stop, float amt) {
        return start + (stop-start) * amt;
    }

}
