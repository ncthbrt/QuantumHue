package com.cthnic001.hue.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.cthnic001.hue.TileType;

/**
 * Created by CTHNI_000 on 2014-08-29.
 */
public class TileIdComponent extends Component implements Pool.Poolable{
    public TileType type;

    public TileIdComponent(TileType type){
        this.type=type;
    }

    public TileIdComponent(){
        reset();
    }

    public void set(TileType type){
        this.type=type;
    }

    @Override
    public void reset() {
        type=TileType.VOID;
    }
}
