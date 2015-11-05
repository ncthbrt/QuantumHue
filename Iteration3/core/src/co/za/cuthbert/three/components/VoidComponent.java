package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class VoidComponent implements Pool.Poolable, Component {
    @Override
    public void reset() {
    }
}
