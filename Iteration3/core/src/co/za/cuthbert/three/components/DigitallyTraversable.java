package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class DigitallyTraversable extends Component implements Pool.Poolable{
    public boolean traversable;
    public float traversalCost;
    public DigitallyTraversable(){
        reset();
    }

    @Override
    public void reset() {
        traversable=false;
        traversalCost =1;
    }
}
