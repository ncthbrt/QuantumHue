package co.za.cuthbert.three;

import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.TileTypeComponent;
import co.za.cuthbert.three.components.WireComponent;
import com.badlogic.ashley.core.Entity;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class TileFactory {

    public static Entity createWire(Level level, int x, int y) {
        Entity wire = level.engine().createEntity();
        TileTypeComponent tileTypeComponent = level.engine().createComponent(TileTypeComponent.class);
        tileTypeComponent.tileType(TileType.WIRE);
        wire.add(tileTypeComponent);

        DVector2 position = level.engine().createComponent(DVector2.class);
        position.set(x, y);
        wire.add(position);

        PortComponent portComponent = level.engine().createComponent(PortComponent.class);
        wire.add(portComponent);

        WireComponent wireComponent = level.engine().createComponent(WireComponent.class);
        wire.add(wireComponent);
        return wire;

    }
}
