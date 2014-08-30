package com.cthnic001.hue.entities;


import com.cthnic001.hue.components.PoolableComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level {

    public Tile[][][] getLevel() {
        return level;
    }

    public Tile get(int x, int y, int z) {
        if (x < width && y < height && depth < z) {
            return level[z][y][x];
        } else {
            throw new IllegalArgumentException("You are attempting to assign a tile to a posiiton which is beyond the extent of the map. Consider resizing");
        }
    }

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

    public List<PoolableComponent> getComponentFromTiles(Class<? extends PoolableComponent> component) {
        ArrayList<PoolableComponent> components = new ArrayList<PoolableComponent>();
        for (int k = 0; k < depth; k++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (level[k][j][i].contains(component)) {
                        components.add(level[k][j][i].getComponent(component));
                    }
                }
            }
        }
        return components;
    }

    public List<Tile> getAllTilesContainingComponent(Class<? extends PoolableComponent> component) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (int k = 0; k < depth; k++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (level[k][j][i].contains(component)) {
                        tiles.add(level[k][j][i]);
                    }
                }
            }
        }
        return tiles;
    }

    public List<Tile> getTileFamily(TileType type) {
        return getTileFamily(type.compulsoryComponents);
    }

    public List<Tile> getTileFamily(List<Class<? extends PoolableComponent>> compulsoryComponents) {
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


    public List<Tile> getTileFamily(Class<? extends PoolableComponent>... compulsoryComponents) {
        ArrayList<Class<? extends PoolableComponent>> components = new ArrayList<Class<? extends PoolableComponent>>();
        Collections.addAll(components, compulsoryComponents);
        return getTileFamily(components);
    }

    public void setTile(Tile tile, int x, int y, int z) {
        if (x < width && y < height && depth < z) {
            level[z][y][x] = tile;
        } else {
            throw new IllegalArgumentException("You are attempting to assign a tile to a posiiton which is beyond the extent of the map. Consider resizing");
        }
    }

    public void growMap(int width, int height, int depth) {
        if (width > this.width && height > this.height && depth > this.depth) {
            Tile[][][] level = new Tile[depth][height][width];
            this.width = width;
            this.height = height;
            this.depth = depth;
            for (int k = 0; k < depth; k++) {
                for (int j = 0; j < height; j++) {
                    System.arraycopy(this.level[k][j], 0, level[k][j], 0, width);
                }
            }
        } else {
            throw new IllegalArgumentException("You cannot make a level smaller, only larger");
        }
    }

}
