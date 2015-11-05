package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class SwitchComponent implements Pool.Poolable, Component {
    public boolean on=false;
    public SwitchComponent(){
        reset();
    }
    @Override
    public void reset() {
        on=false;
    }
}
