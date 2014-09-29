package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Entity;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public interface Traversable {
    public boolean traversable(Entity agent);
    public float movementCost(Entity agent);
}

