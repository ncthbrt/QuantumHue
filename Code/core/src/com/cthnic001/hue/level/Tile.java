package com.cthnic001.hue.level;

import com.cthnic001.hue.components.DPositionComponent;
import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.components.PoolableComponent;

import java.util.HashMap;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Tile implements Poolable {
    public final DPositionComponent position;
    private final HashMap<Class<? extends PoolableComponent>, PoolableComponent> components;


    public Tile() {
        position = new DPositionComponent();
        this.components = new HashMap<Class<? extends PoolableComponent>, PoolableComponent>();
    }

    public Tile(int x, int y, int z) {
        this();
        position.set(x, y, z);
    }

    public boolean conforms(TileType type) {
        if (type == TileType.VOID || !conforms(TileType.VOID)) {
        /*If this tile conforms to the void pattern, it is automatically precluded from
        matching any other pattern*/
            for (Class<? extends PoolableComponent> componentType : type.compulsoryComponents) {
                if (!components.containsKey(componentType)) {
                    return false;
                }
            }
            return true;
        }
        return false;
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
        return (components.get(type));
    }
}
