package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentComponent extends Component implements Pool.Poolable {
    public final Vector2 position;

    public AgentComponent(){
        position=new Vector2();
    }

    @Override
    public void reset() {
        position.set(0,0);
    }
}
