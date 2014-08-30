package com.cthnic001.hue.systems;

import com.cthnic001.hue.entities.Level;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public interface System {
    public void added(Level level);

    public void removed();

    public void update(float deltaTime);

    public boolean checkProcessing();
}
