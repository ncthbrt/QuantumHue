package com.deepwallgames.quantumhue;

import com.badlogic.ashley.core.*;
import com.deepwallgames.quantumhue.components.*;

import com.deepwallgames.quantumhue.value_objects.DRotation;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum EntityType {

    WIRE(0, PortComponent.class, WireComponent.class, DigitallyTraversable.class,EntityTypeComponent.class),
    INVERTER(1,EntityTypeComponent.class,DVector2.class, DRotationComponent.class),
    TERMINATOR(2,EntityTypeComponent.class,DVector2.class),
    EXIT(3,EntityTypeComponent.class,DVector2.class),
    TURN_TABLE(4,EntityTypeComponent.class,DVector2.class),
    SLIDER(5,EntityTypeComponent.class,DVector2.class),
    POWER_SOURCE(6, ColourComponent.class, PortComponent.class, DigitallyTraversable.class, SwitchComponent.class,DVector2.class),
    ISOLATOR(7,EntityTypeComponent.class,DVector2.class),
    TRANSISTOR(8,EntityTypeComponent.class,DVector2.class),
    GROUND(9, ColourComponent.class,PortComponent.class,DigitallyTraversable.class,EntityTypeComponent.class,DVector2.class),
    MOMENTARY_SWITCH(10,EntityTypeComponent.class,DVector2.class),
    SWITCH(11, SwitchComponent.class,EntityTypeComponent.class,DVector2.class),
    VOID(13, VoidComponent.class,EntityTypeComponent.class,DVector2.class),
    FILTER(14, ColourComponent.class,EntityTypeComponent.class,DVector2.class);

    EntityType(int code, Class<? extends Component>... compulsoryComponents) {
        this.code = code;
        this.family=Family.all(compulsoryComponents).get();
        this.requiredComponents=compulsoryComponents;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public final Family family;
    public final Class<? extends Component> [] requiredComponents;
    public static EntityType findMatching(int code) {
        for (EntityType type : EntityType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return VOID;
    }

    public  boolean conforms(Entity entity){
        return (this == VOID || isTile(entity)) && family.matches(entity);
    }

    private static final ComponentMapper<EntityTypeComponent> tileTypeMapper=ComponentMapper.getFor(EntityTypeComponent.class);

    public static boolean isTile(Entity entity){
        return entity != null && !VOID.family.matches(entity) && tileTypeMapper.has(entity);
    }
}
