package com.cthnic001.hue;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;

/**
 * Created by CTHNI_000 on 2014-08-26.
 */
public class Level {

    private Tile[][][] level;
    private ArrayList<Agent> agents;
    private final int width;
    private final int height;
    private final int depth;

    public Level(int width, int height, int depth){
        level=new Tile[depth][height][width];
        agents=new ArrayList<Agent>();
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

    public void addAgent(Agent agent){
        agents.add(agent);
    }

    public void growMap(int width, int height, int depth){
        if (width > this.width && height > this.height && depth > this.depth) {
            Tile[][][] resizedLevel = new Tile[depth][height][width];
            for (int k = 0; k < depth; k++) {
                for (int j = 0; j < height; j++) {
                    for (int i = 0; i < width; i++) {
                        resizedLevel[k][j][i] = level[k][j][i];
                    }
                }
            }
        }else{
            throw new IllegalArgumentException("You cannot make a level smaller, only larger");
        }
    }

}
