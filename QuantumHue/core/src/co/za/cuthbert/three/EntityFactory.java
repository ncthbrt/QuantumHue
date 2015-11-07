package co.za.cuthbert.three;

import co.za.cuthbert.three.components.*;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Entity;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class EntityFactory {

    public static Entity createEntity(Level level, int x, int y, EntityType type, DiscreteColour currentColour) {
        if (type == EntityType.WIRE) {
            return createWire(level, x, y);
        } else if (type == EntityType.POWER_SOURCE) {
            return createPowerSource(level, x, y, currentColour);
        }
        else {
            return createVoid(level, x, y);
        }
    }

    public static Entity createWire(Level level, int x, int y) {
        Entity wire = level.engine().createEntity();
        EntityTypeComponent tileTypeComponent = level.engine().createComponent(EntityTypeComponent.class);
        tileTypeComponent.tileType(EntityType.WIRE);
        wire.add(tileTypeComponent);

        DVector2 position = level.engine().createComponent(DVector2.class);
        position.set(x, y);
        wire.add(position);

        PortComponent portComponent = level.engine().createComponent(PortComponent.class);
        wire.add(portComponent);

        WireComponent wireComponent = level.engine().createComponent(WireComponent.class);
        wire.add(wireComponent);

        DigitallyTraversable digitallyTraversable=level.engine().createComponent(DigitallyTraversable.class);
        digitallyTraversable.traversable=true;
        digitallyTraversable.traversalCost =1;

        wire.add(digitallyTraversable);
        return wire;

    }

    public static Entity createPowerSource(Level level, int x, int y, DiscreteColour colour) {
        Entity powerSource = level.engine().createEntity();
        EntityTypeComponent tileTypeComponent = level.engine().createComponent(EntityTypeComponent.class);
        tileTypeComponent.tileType(EntityType.POWER_SOURCE);
        powerSource.add(tileTypeComponent);

        DVector2 position = level.engine().createComponent(DVector2.class);
        position.set(x, y);
        powerSource.add(position);

        PortComponent portComponent = level.engine().createComponent(PortComponent.class);
        powerSource.add(portComponent);

        ColourComponent colourComponent = level.engine().createComponent(ColourComponent.class);
        colourComponent.colour(colour.toColour());
        powerSource.add(colourComponent);

        DigitallyTraversable digitallyTraversable=level.engine().createComponent(DigitallyTraversable.class);
        digitallyTraversable.traversable=false;
        digitallyTraversable.traversalCost =0;

        powerSource.add(digitallyTraversable);

        return powerSource;
    }

    public static Entity createVoid(Level level, int x, int y) {
        Entity voidEntity = level.engine().createEntity();
        EntityTypeComponent tileTypeComponent = level.engine().createComponent(EntityTypeComponent.class);
        tileTypeComponent.tileType(EntityType.VOID);
        voidEntity.add(tileTypeComponent);

        DVector2 position = level.engine().createComponent(DVector2.class);
        position.set(x, y);
        voidEntity.add(position);

        voidEntity.add(level.engine().createComponent(VoidComponent.class));

        return voidEntity;
    }
}
