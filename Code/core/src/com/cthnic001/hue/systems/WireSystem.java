package com.cthnic001.hue.systems;

import com.cthnic001.hue.components.PortComponent;
import com.cthnic001.hue.components.WireComponent;
import com.cthnic001.hue.entities.Level;
import com.cthnic001.hue.entities.Tile;
import com.cthnic001.hue.entities.TileType;

import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireSystem extends System {
    private Level level;
    private List<Tile> wireTiles;
    private float advancementRate = 0;

    public WireSystem(float advancementRate) {
        this.advancementRate = advancementRate;
    }

    @Override
    public void added(Level level) {
        this.level = level;
        wireTiles = level.getTileFamily(TileType.WIRE);
        for (Tile portTile : wireTiles) {
            PortComponent port = (PortComponent) portTile.getComponent(PortComponent.class);
            port.setNeighboringPorts(level, portTile.position.x, portTile.position.y, portTile.position.z);
        }
    }

    @Override
    public void removed() {
        reset();
    }

    @Override
    public void update(float deltaTime) {
        float advance = deltaTime * advancementRate;

        //Update colours
        for (Tile portTile : wireTiles) {
            WireComponent wireComponent = (WireComponent) portTile.getComponent(WireComponent.class);
            PortComponent portComponent = (PortComponent) portTile.getComponent(PortComponent.class);
            portComponent.setOutgoingPortColours(wireComponent.getOutgoingPortColours());
            wireComponent.setIncomingPortColours(portComponent.getIncomingPortColours());
        }

        //Advance
        for (Tile portTile : wireTiles) {
            WireComponent wireComponent = (WireComponent) portTile.getComponent(WireComponent.class);
            wireComponent.advance(advance);
        }
    }

    @Override
    public boolean checkProcessing() {
        return true;
    }

    @Override
    public void reset() {
        level = null;
        advancementRate = 0;
        wireTiles.clear();
    }
}
