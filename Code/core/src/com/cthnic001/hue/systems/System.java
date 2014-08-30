package com.cthnic001.hue.systems;

import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.entities.Level;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public abstract class System implements Poolable {
    public abstract void added(Level level);

    public abstract void removed();

    public abstract void update(float deltaTime);

    public abstract boolean checkProcessing();
}
