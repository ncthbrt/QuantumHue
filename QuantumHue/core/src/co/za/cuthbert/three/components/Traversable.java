package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Traversable implements Pool.Poolable, Component {
    public abstract boolean traversable(Entity entity);
    public abstract float traversalCost(Entity entity);
}
