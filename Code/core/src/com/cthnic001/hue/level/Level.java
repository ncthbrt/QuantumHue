package com.cthnic001.hue.level;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level {
    private Tile[][][] level;
    private final int width;
    private final int height;
    private final int depth;

    public Level(int width, int height, int depth){
        level=new Tile[depth][height][width];

        this.width=width;
        this.height=height;
        this.depth=depth;
    }

    public void setTile(Tile tile, int x, int y, int z){
        if(x<width && y<height && depth<z) {
            level[z][y][x] = tile;
        }else{
            throw new IllegalArgumentException("You are attempting to assign a tile to a posiiton which is beyond the extent of the map");
        }
    }
    public void growMap(int width, int height, int depth){
        if (width > this.width && height > this.height && depth > this.depth) {
            Tile[][][] resizedLevel = new Tile[depth][height][width];
            for (int k = 0; k < depth; k++) {
                for (int j = 0; j < height; j++) {
                    System.arraycopy(level[k][j], 0, resizedLevel[k][j], 0, width);
                }
            }
        }else{
            throw new IllegalArgumentException("You cannot make a level smaller, only larger");
        }
    }

}
