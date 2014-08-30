package com.cthnic001.hue;

/**A class to store discrete coordinates in three dimensions. Especially useful for tile based games
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector3 {
    public int x, y, z;
    public DVector3(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public DVector3(){
        reset();
    }

    public void set(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public void reset() {
        this.x=0;
        this.y=0;
        this.z=0;
    }
}
