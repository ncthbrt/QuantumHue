package com.cthnic001.hue.components;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent extends PoolableComponent {
    private int colour;

    public static int red(int colour) {
        return ((colour & 0xFF000000) >>> 24);
    }

    public int red() {
        return red(colour);
    }

    public static int green(int colour) {
        return ((colour & 0xFF0000) >>> 16);
    }

    public int green() {
        return green(colour);
    }

    public static int blue(int colour) {
        return ((colour & 0xFF00) >>> 8);
    }

    public int blue() {
        return blue(colour);
    }

    public static int alpha(int colour) {
        return (colour & 0xFF);
    }

    public int alpha() {
        return alpha(colour);
    }

    public ColourComponent() {
        reset();
    }

    public ColourComponent(int rbga) {
        this.colour = rbga;
    }

    public ColourComponent(int r, int g, int b, int a) {
        this(pack(r, g, b, a));
    }


    public static int pack(int r, int g, int b, int a) {
        return ((0xFF & r) << 24) | ((0xFF & g) << 16) | ((0xFF & b) << 8) | (0xFF & a);
    }

    @Override
    public void reset() {
        colour = 0;
    }
}
