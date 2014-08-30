package com.cthnic001.hue.level;

import com.cthnic001.hue.components.ColourComponent;
import com.cthnic001.hue.components.PoolableComponent;
import com.cthnic001.hue.components.PortComponent;
import com.cthnic001.hue.components.VoidComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum TileType {

    WIRE(0, PortComponent.class),
    INVERTER(1),
    TERMINATOR(2),
    EXIT(3),
    TURN_TABLE(4),
    SLIDER(5),
    POWER_SOURCE(6),
    ISOLATOR(7),
    TRANSISTOR(8),
    GROUND(9),
    MOMENTARY_SWITCH(10),
    SWITCH(11),
    VOID(13, VoidComponent.class),
    DONT_CARE(14),
    FILTER(15, ColourComponent.class);


    TileType(int code, Class<? extends PoolableComponent>... compulsoryComponents) {
        this.code = code;
        this.compulsoryComponents = new ArrayList<Class<? extends PoolableComponent>>();
        Collections.addAll(this.compulsoryComponents, compulsoryComponents);
    }

    private int code;

    public int get() {
        return code;
    }

    public final List<Class<? extends PoolableComponent>> compulsoryComponents;

    public static TileType findMatching(int code) {
        for (TileType type : TileType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return VOID;
    }
}
