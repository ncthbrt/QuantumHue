package com.cthnic001.hue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.cthnic001.hue.components.DPositionComponent;
import com.cthnic001.hue.components.TileIdComponent;

/**
 * Created by CTHNI_000 on 2014-08-29.
 */
public class TileFactory {
    private final PooledEngine engine;
    public TileFactory(PooledEngine engine){
        this.engine=engine;
    }

    public Entity manufactureWire(int x, int y, int z){
        Entity wire=engine.createEntity();
        DPositionComponent position=engine.createComponent(DPositionComponent.class);
        position.set(x,y,z);
        wire.add(position);
        TileIdComponent tileType=engine.createComponent(TileIdComponent.class);
        tileType.set(TileType.WIRE);
        return wire;
    }
}
