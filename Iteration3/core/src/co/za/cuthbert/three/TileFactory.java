package co.za.cuthbert.three;

import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.TileTypeComponent;
import co.za.cuthbert.three.components.WireComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class TileFactory {

    public static Entity createWire(PooledEngine engine,int x, int y, int z){
        Entity wire=engine.createEntity();
        TileTypeComponent tileTypeComponent= engine.createComponent(TileTypeComponent.class);
        tileTypeComponent.tileType(TileType.WIRE);
        wire.add(tileTypeComponent);

        DVector3 position=engine.createComponent(DVector3.class);
        position.set(x,y,z);
        wire.add(position);

        PortComponent portComponent=engine.createComponent(PortComponent.class);
        wire.add(portComponent);

        WireComponent wireComponent=engine.createComponent(WireComponent.class);
        wire.add(wireComponent);

        return wire;

    }
}
