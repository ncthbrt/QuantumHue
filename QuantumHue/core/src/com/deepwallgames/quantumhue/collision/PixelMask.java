package com.deepwallgames.quantumhue.collision;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PixelMask {
    public final boolean mask[][];
    public final int width, height;
    public int x=0,y=0;
    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }
    public PixelMask(int width, int height){
        this.width=width;
        this.height=height;
        mask=new boolean[height][width];
    }

    public void set(int i, int j, boolean value){
        mask[j][i]=value;
    }

    public boolean isAt(int i, int j){
        if(i-x>=0 && i-x<width && height-(j-y)>=0 && height-(j-y)<height) {
            i=i-x;
            j=height-(j-y);
            return mask[j][i];
        }
        return false;
    }
}
