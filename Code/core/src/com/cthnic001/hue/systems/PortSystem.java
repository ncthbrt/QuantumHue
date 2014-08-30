package com.cthnic001.hue.systems;

import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.components.PortComponent;
import com.cthnic001.hue.entities.Level;
import com.cthnic001.hue.entities.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PortSystem implements System {
    private Level level;
    private List<Tile> portTiles;

    public PortSystem() {
    }

    @Override
    public void added(Level level) {
        this.level = level;
        portTiles = level.getAllTilesContainingComponent(PortComponent.class);
    }

    @Override
    public void removed() {
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public boolean checkProcessing() {
        return true;
    }
}
