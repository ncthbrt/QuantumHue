package com.deepwallgames.quantumhue;

import com.deepwallgames.quantumhue.components.ColourComponent;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.DigitallyTraversable;
import com.deepwallgames.quantumhue.components.EntityTypeComponent;
import com.deepwallgames.quantumhue.components.SwitchComponent;
import com.deepwallgames.quantumhue.components.VoidComponent;
import com.deepwallgames.quantumhue.components.WireComponent;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;
import com.deepwallgames.quantumhue.components.PortComponent;

import com.badlogic.ashley.core.Entity;

import java.awt.Color;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class EntityFactory {

    public static Entity createEntity(Level level, int x, int y, EntityType type, DiscreteColour currentColour) {
        Entity entity;
        if (type == EntityType.WIRE) {
            entity=createWire(level, x, y);
        } else if (type == EntityType.POWER_SOURCE) {
            entity=createPowerSource(level, x, y, currentColour);
        }
        else if (type == EntityType.GROUND) {
            entity=createGround(level, x, y, currentColour);
        }
        else {
            entity=createVoid(level, x, y);
        }
        EntityTypeComponent tileTypeComponent = level.engine().createComponent(EntityTypeComponent.class);
        tileTypeComponent.tileType(type);
        entity.add(tileTypeComponent);
        return entity;
    }

    public static Entity createWire(Level level, int x, int y) {
        Entity wire = level.engine().createEntity();


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

        SwitchComponent switchComponent=level.engine().createComponent(SwitchComponent.class);
        switchComponent.on=true;
        powerSource.add(switchComponent);

        return powerSource;
    }

    public static Entity createGround(Level level, int x, int y, DiscreteColour colour) {
        Entity powerSource = level.engine().createEntity();
        EntityTypeComponent tileTypeComponent = level.engine().createComponent(EntityTypeComponent.class);
        tileTypeComponent.tileType(EntityType.GROUND);
        powerSource.add(tileTypeComponent);

        DVector2 position = level.engine().createComponent(DVector2.class);
        position.set(x, y);
        powerSource.add(position);

        PortComponent portComponent = level.engine().createComponent(PortComponent.class);
        powerSource.add(portComponent);

        ColourComponent colourComponent = level.engine().createComponent(ColourComponent.class);
        colourComponent.colour(new Colour((int)(255*0.3f),(int)(255*0.3f),(int)(255*0.3f),255));
        powerSource.add(colourComponent);

        DigitallyTraversable digitallyTraversable=level.engine().createComponent(DigitallyTraversable.class);
        digitallyTraversable.traversable=true;
        digitallyTraversable.traversalCost =1;

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
