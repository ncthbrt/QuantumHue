package co.za.cuthbert.three.data;


import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Colour implements Pool.Poolable {
    private int colour;

    public int toRGBA() {
        return colour;
    }

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

    public Colour() {
        reset();
    }

    public Colour(int rbga) {
        set(rbga);
    }

    public Colour(int r, int g, int b, int a) {
        set(r, g, b, a);
    }


    public void add(Colour colour) {
        this.colour += colour.colour;
        this.colour = Math.min(this.colour, 0xFFFFFFFF);
    }

    public void subtract(Colour colour) {
        this.colour -= colour.colour;
        this.colour = Math.max(this.colour, 0);
    }


    public static Colour subtract(Colour colour1, Colour colour2) {
        Colour colour3 = new Colour(colour1);
        colour3.subtract(colour2);
        return colour3;
    }

    public static Colour add(Colour colour1, Colour colour2) {
        Colour colour3 = new Colour(colour1);
        colour3.add(colour2);
        return colour3;
    }

    public Colour(Colour colour) {
        this.colour = colour.colour;
    }


    public static int pack(int r, int g, int b, int a) {
        return ((0xFF & r) << 24) | ((0xFF & g) << 16) | ((0xFF & b) << 8) | (0xFF & a);
    }

    public void set(int rgba) {
        this.colour = rgba;
    }

    public void set(int r, int g, int b, int a) {
        colour = pack(r, g, b, a);
    }

    public void set(Colour colour) {
        this.colour = colour.colour;
    }


    @Override
    public void reset() {
        colour = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Colour) {
            Colour otherColour = (Colour) obj;
            return otherColour.colour == colour;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return colour;
    }
}
