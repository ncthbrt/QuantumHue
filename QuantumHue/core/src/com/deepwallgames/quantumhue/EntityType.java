package com.deepwallgames.quantumhue;

import com.badlogic.ashley.core.*;
import com.deepwallgames.quantumhue.components.PortComponent;

import com.deepwallgames.quantumhue.components.ColourComponent;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.DigitallyTraversable;
import com.deepwallgames.quantumhue.components.EntityTypeComponent;
import com.deepwallgames.quantumhue.components.SwitchComponent;
import com.deepwallgames.quantumhue.components.VoidComponent;
import com.deepwallgames.quantumhue.components.WireComponent;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum EntityType {

    WIRE(0, PortComponent.class, WireComponent.class, DigitallyTraversable.class,EntityTypeComponent.class),
    INVERTER(1,EntityTypeComponent.class),
    TERMINATOR(2,EntityTypeComponent.class),
    EXIT(3,EntityTypeComponent.class),
    TURN_TABLE(4,EntityTypeComponent.class),
    SLIDER(5,EntityTypeComponent.class),
    POWER_SOURCE(6, ColourComponent.class, PortComponent.class, DigitallyTraversable.class, SwitchComponent.class),
    ISOLATOR(7,EntityTypeComponent.class),
    TRANSISTOR(8,EntityTypeComponent.class),
    GROUND(9, ColourComponent.class,PortComponent.class,DigitallyTraversable.class,EntityTypeComponent.class),
    MOMENTARY_SWITCH(10,EntityTypeComponent.class),
    SWITCH(11, SwitchComponent.class,EntityTypeComponent.class),
    VOID(13, VoidComponent.class,EntityTypeComponent.class),
    FILTER(14, ColourComponent.class,EntityTypeComponent.class);

    EntityType(int code, Class<? extends Component>... compulsoryComponents) {
        this.code = code;
        this.family=Family.all(compulsoryComponents).one(DVector2.class).get();
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
