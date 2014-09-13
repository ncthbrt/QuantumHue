package co.za.cuthbert.three.components;

import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.interfaces.ATileTypeComponent;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class TileTypeComponent  extends ATileTypeComponent implements Pool.Poolable {

    public TileTypeComponent(TileType type){
        this.tileType=type;
    }
    public TileTypeComponent(){
        reset();
    }

    private TileType tileType;

    public void tileType(TileType tileType) {
        this.tileType = tileType;
    }
    public TileType tileType(){
        return tileType;
    }
    @Override
    public void reset() {
        tileType=TileType.VOID;
    }

    public static final String TYPE_NAME="tile-type";
    @Override
    public String getComponentName() {
        return TYPE_NAME;
    }
}
