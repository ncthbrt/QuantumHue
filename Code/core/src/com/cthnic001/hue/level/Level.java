package com.cthnic001.hue.level;


import com.cthnic001.hue.components.PoolableComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level {
    private Tile[][][] level;
    private int width;
    private int height;
    private int depth;

    public Level(int width, int height, int depth) {
        level = new Tile[depth][height][width];
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public ArrayList<Tile> getTileFamily(TileType type) {
        return getTileFamily(type.compulsoryComponents);
    }

    public ArrayList<Tile> getTileFamily(List<Class<? extends PoolableComponent>> compulsoryComponents) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (int k = 0; k < depth; k++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (level[k][j][i].conforms(compulsoryComponents)) {
                        tiles.add(level[k][j][i]);
                    }
                }
            }
        }
        return tiles;
    }


    public ArrayList<Tile> getTileFamily(Class<? extends PoolableComponent>... compulsoryComponents) {
        ArrayList<Class<? extends PoolableComponent>> components = new ArrayList<Class<? extends PoolableComponent>>();
        Collections.addAll(components, compulsoryComponents);
        return getTileFamily(components);
    }

    public void setTile(Tile tile, int x, int y, int z) {
        if (x < width && y < height && depth < z) {
            level[z][y][x] = tile;
        } else {
            throw new IllegalArgumentException("You are attempting to assign a tile to a posiiton which is beyond the extent of the map");
        }
    }

    public void growMap(int width, int height, int depth) {
        if (width > this.width && height > this.height && depth > this.depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;

            Tile[][][] resizedLevel = new Tile[depth][height][width];
            for (int k = 0; k < depth; k++) {
                for (int j = 0; j < height; j++) {
                    System.arraycopy(level[k][j], 0, resizedLevel[k][j], 0, width);
                }
            }
        } else {
            throw new IllegalArgumentException("You cannot make a level smaller, only larger");
        }
    }

}
