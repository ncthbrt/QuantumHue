package com.deepwallgames.quantumhue.value_objects;


import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Colour implements Pool.Poolable {
    private int colour;

    public int toRGBA() {
        return colour;
    }

    public HSVColour toHSV(){
        float h=0,s,v;

        float a=alpha()/255f;

        float r=red()/255f;
        float g=green()/255f;
        float b=blue()/255f;

        float min=Math.min(Math.min(r,g),b);
        float max=Math.max(Math.max(r, g), b);
        float delta=max-min;

        v=max;
        if(delta==0){
            h=0;
            s=0;
        }else{
            s=delta/max;

            float deltaR = ((( max - r )/6)+ (max/2))/max;
            float deltaG= ((( max - g)/6)+ (max/2))/max;
            float deltaB= ((( max - b)/6)+ (max/2))/max;

            if(r==max){
                h=deltaB-deltaG;
            }else if(g==max){
                h=(1/3f)+deltaR-deltaB;
            }else if(b==max){
                h=(2/3f)+deltaG-deltaR;
            }

            if(h<0){
                h+=1f;
            }else if(h>1){
                h-=1f;
            }

        }
        return new HSVColour(h,s,v,a);
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

    public Colour(HSVColour colour){
        this.colour=colour.toRGB().colour;
    }

    public Colour(int rbga) {
        set(rbga);
    }

    public Colour(int r, int g, int b, int a) {
        set(r, g, b, a);
    }


    public void add(Colour colour) {
        int red=Math.min(255,this.red()+colour.red());
        int green=Math.min(255,this.green()+colour.green());
        int blue=Math.min(255,this.blue()+colour.blue());
        int alpha=Math.min(255,this.alpha()+colour.alpha());
        this.colour=red<<24|green<<16|blue<<8|alpha;

//        this.colour += colour.colour;
//        if(this.colour<0) {
//            this.colour=0xFFFFFFFF;
//        }
    }

    public void subtract(Colour colour) {
        this.colour -= colour.colour;
        if(this.colour<0) {
            this.colour = 0;
        }
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
        colour = rgba;
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
            return otherColour.colour == this.colour;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return colour;
    }
}
