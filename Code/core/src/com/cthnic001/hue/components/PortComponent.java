package com.cthnic001.hue.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by CTHNI_000 on 2014-08-29.
 */
public class PortComponent extends Component implements Pool.Poolable{
    public int depth;

    public PortComponent(){reset();}

    public PortComponent(int depth){
        this.depth=depth;
    }

    @Override
    public void reset() {
        depth=0;
    }
}
