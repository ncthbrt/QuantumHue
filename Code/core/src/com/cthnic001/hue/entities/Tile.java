package com.cthnic001.hue.entities;

import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.components.PoolableComponent;
import com.cthnic001.hue.data.DVector3;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Tile implements Poolable {
    public final DVector3 position;
    private final HashMap<Class<? extends PoolableComponent>, PoolableComponent> components;


    public Tile() {
        position = new DVector3();
        components = new HashMap<Class<? extends PoolableComponent>, PoolableComponent>();
    }

    public Tile(int x, int y, int z) {
        this();
        position.set(x, y, z);
    }

    public boolean conforms(TileType type) {
        if (type == TileType.VOID || !conforms(TileType.VOID)) {
        /*If this tile conforms to the void pattern, it is automatically precluded from
        matching any other pattern*/
            return conforms(type.compulsoryComponents);
        }
        return false;
    }

    public boolean conforms(List<Class<? extends PoolableComponent>> componentRequirements) {
        for (Class<? extends PoolableComponent> componentType : componentRequirements) {
            if (!components.containsKey(componentType)) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        for (PoolableComponent pc : components.values()) {
            pc.reset();
        }
        components.clear();
        this.position.reset();
    }

    public void addComponent(PoolableComponent component) {
        components.put(component.getClass(), component);
    }

    public boolean contains(Class<? extends PoolableComponent> type) {
        return components.containsKey(type);
    }

    public PoolableComponent getComponent(Class<? extends PoolableComponent> type) {
        return components.get(type);
    }
}
