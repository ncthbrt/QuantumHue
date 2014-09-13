package com.cthnic001.hue;

/**
 * Created by CTHNI_000 on 2014-09-03.
 */
public class Interpolation {
    static public final float lerp(float start, float stop, float amt) {
        return start + (stop-start) * amt;
    }
}
