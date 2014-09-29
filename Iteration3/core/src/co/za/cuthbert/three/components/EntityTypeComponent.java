package co.za.cuthbert.three.components;

import co.za.cuthbert.three.EntityType;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class EntityTypeComponent extends Component implements Pool.Poolable {

    public EntityTypeComponent(EntityType type){
        this.entityType =type;
    }
    public EntityTypeComponent(){
        reset();
    }

    private EntityType entityType;

    public void tileType(EntityType entityType) {
        this.entityType = entityType;
    }
    public EntityType tileType(){
        return entityType;
    }
    @Override
    public void reset() {
        entityType = EntityType.VOID;
    }

    public static final String TYPE_NAME="tile-type";

    public String getComponentName() {
        return TYPE_NAME;
    }
}
