package com.cthnic001.hue.components;

import com.badlogic.ashley.core.Component;
import com.cthnic001.hue.DVector3;

/**
 * Created by CTHNI_000 on 2014-08-29.
 */
public class DPositionComponent extends Component implements com.badlogic.gdx.utils.Pool.Poolable {
    public final DVector3 position;

    public DPositionComponent(){
        position=new DVector3();
    }

    public void set(int x, int y, int z){
        position.set(x,y,z);
    }

    @Override
    public void reset() {
        position.reset();
    }
}
