package com.deepwallgames.quantumhue;

import com.badlogic.ashley.core.PooledEngine;
import com.deepwallgames.quantumhue.components.*;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.DRotation;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;

import com.badlogic.ashley.core.Entity;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class EntityFactory {

    public static Entity createEntity(Level level, int x, int y, EntityType type, DiscreteColour currentColour, DRotation currentRotation) {
        Entity entity;
        if (type == EntityType.WIRE) {
            entity=createWire(level, x, y);
        } else if (type == EntityType.POWER_SOURCE) {
            entity=createPowerSource(level, x, y, currentColour);
        }
        else if (type == EntityType.GROUND) {
            entity=createGround(level, x, y, currentColour);
        }
        else if(type==EntityType.INVERTER){
            entity= createInverter(level, x, y, currentColour,currentRotation);
        }
        else {
            entity=createVoid(level, x, y);
        }
        EntityTypeComponent tileTypeComponent;

        if(level.engine() instanceof PooledEngine) {
            tileTypeComponent = ((PooledEngine)level.engine()).createComponent(EntityTypeComponent.class);
        }else{
            tileTypeComponent = new EntityTypeComponent();
        }
        tileTypeComponent.tileType(type);
        entity.add(tileTypeComponent);

        return entity;
    }

    public static Entity createEntity(Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createEntity();
        }else{
            Entity entity=new Entity();
            return entity;
        }
    }


    public static  DVector2 createDVector2(Class<DVector2> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new DVector2();
        }
    }



    public static DigitallyTraversable createDigitallyTraversable(Class<DigitallyTraversable> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new DigitallyTraversable();
        }
    }

    public static  ColourComponent createColourComponent(Class<ColourComponent> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new ColourComponent();
        }
    }

    public static  WireComponent createWireComponent(Class<WireComponent> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new WireComponent();
        }
    }

    public static PortComponent createPortComponent(Class<PortComponent> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new PortComponent();
        }
    }

    public static VoidComponent createVoidComponent(Class<VoidComponent > clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new VoidComponent();
        }
    }

    public static SwitchComponent createSwitchComponent(Class<SwitchComponent> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new SwitchComponent();
        }
    }

    public static EntityTypeComponent createEntityTypeComponent(Class<EntityTypeComponent> clazzy,Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(clazzy);
        }else{
            return new EntityTypeComponent();
        }
    }





    public static Entity createWire(Level level, int x, int y) {
        Entity wire = createEntity(level);

        DVector2 position = createDVector2(DVector2.class,level);
        position.set(x, y);
        wire.add(position);

        PortComponent portComponent = createPortComponent(PortComponent.class,level);
        wire.add(portComponent);

        WireComponent wireComponent = createWireComponent(WireComponent.class,level);
        wire.add(wireComponent);

        DigitallyTraversable digitallyTraversable=createDigitallyTraversable(DigitallyTraversable.class,level);
        digitallyTraversable.traversable=true;
        digitallyTraversable.traversalCost =1;

        wire.add(digitallyTraversable);
        return wire;

    }

    public static Entity createPowerSource(Level level, int x, int y, DiscreteColour colour) {
        Entity powerSource = createEntity(level);
        EntityTypeComponent tileTypeComponent = createEntityTypeComponent(EntityTypeComponent.class,level);
        tileTypeComponent.tileType(EntityType.POWER_SOURCE);
        powerSource.add(tileTypeComponent);

        DVector2 position = createDVector2(DVector2.class,level);
        position.set(x, y);
        powerSource.add(position);

        PortComponent portComponent = createPortComponent(PortComponent.class,level);
        powerSource.add(portComponent);

        ColourComponent colourComponent = createColourComponent(ColourComponent.class,level);
        colourComponent.colour(colour.toColour());
        powerSource.add(colourComponent);

        DigitallyTraversable digitallyTraversable=createDigitallyTraversable(DigitallyTraversable.class,level);
        digitallyTraversable.traversable=false;
        digitallyTraversable.traversalCost =0;

        powerSource.add(digitallyTraversable);

        SwitchComponent switchComponent=createSwitchComponent(SwitchComponent.class,level);
        switchComponent.on=true;
        powerSource.add(switchComponent);

        return powerSource;
    }

    public static Entity createInverter(Level level, int x, int y, DiscreteColour colour, DRotation rotation){
        Entity inverter=createEntity(level);

        EntityTypeComponent tileTypeComponent = createEntityTypeComponent(EntityTypeComponent.class,level);
        tileTypeComponent.tileType(EntityType.INVERTER);
        inverter.add(tileTypeComponent);

        DVector2 position = createDVector2(DVector2.class,level);
        position.set(x, y);
        inverter.add(position);

        PortComponent portComponent = createPortComponent(PortComponent.class,level);
        inverter.add(portComponent);

        ColourComponent colourComponent = createColourComponent(ColourComponent.class,level);
        colourComponent.colour(new Colour(255,255,255,255));
        inverter.add(colourComponent);

        DRotationComponent rotationComponent=createRotationComponent(level);
        rotationComponent.rotation=rotation;
        inverter.add(rotationComponent);

        return inverter;
    }

    public static DRotationComponent createRotationComponent(Level level){
        if(level.engine() instanceof PooledEngine){
            return ((PooledEngine)level.engine()).createComponent(DRotationComponent.class);
        }else{
            return new DRotationComponent();
        }
    }


    public static Entity createGround(Level level, int x, int y, DiscreteColour colour) {
        Entity powerSource = createEntity(level);
        EntityTypeComponent tileTypeComponent = createEntityTypeComponent(EntityTypeComponent.class,level);
        tileTypeComponent.tileType(EntityType.GROUND);
        powerSource.add(tileTypeComponent);

        DVector2 position = createDVector2(DVector2.class,level);
        position.set(x, y);
        powerSource.add(position);

        PortComponent portComponent = createPortComponent(PortComponent.class,level);
        powerSource.add(portComponent);

        ColourComponent colourComponent = createColourComponent(ColourComponent.class,level);
        colourComponent.colour(new Colour((int)(255*0.3f),(int)(255*0.3f),(int)(255*0.3f),255));
        powerSource.add(colourComponent);

        DigitallyTraversable digitallyTraversable=createDigitallyTraversable(DigitallyTraversable.class,level);
        digitallyTraversable.traversable=true;
        digitallyTraversable.traversalCost =1;

        powerSource.add(digitallyTraversable);
        return powerSource;
    }

    public static Entity createVoid(Level level, int x, int y) {
        Entity voidEntity = createEntity(level);
        EntityTypeComponent tileTypeComponent = createEntityTypeComponent(EntityTypeComponent.class,level);
        tileTypeComponent.tileType(EntityType.VOID);
        voidEntity.add(tileTypeComponent);

        DVector2 position = createDVector2(DVector2.class,level);
        position.set(x, y);
        voidEntity.add(position);

        voidEntity.add(createVoidComponent(VoidComponent.class,level));

        return voidEntity;
    }
}
