package co.za.cuthbert.three;

import co.za.cuthbert.three.components.*;
import co.za.cuthbert.three.components.interfaces.ADVector3;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.utils.Bits;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum TileType {

    WIRE(0, PortComponent.class, WireComponent.class),
    INVERTER(1),
    TERMINATOR(2),
    EXIT(3),
    TURN_TABLE(4),
    SLIDER(5),
    POWER_SOURCE(6),
    ISOLATOR(7),
    TRANSISTOR(8),
    GROUND(9),
    MOMENTARY_SWITCH(10),
    SWITCH(11),
    VOID(13, VoidComponent.class),
    FILTER(14, ColourComponent.class);

    TileType(int code, Class<? extends Component>... compulsoryComponents) {
        this.code = code;
        this.family=Family.getFor(ComponentType.getBitsFor(compulsoryComponents), ComponentType.getBitsFor(ADVector3.class),new Bits());
        this.requiredComponents=compulsoryComponents;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public final Family family;
    public final Class<? extends Component> [] requiredComponents;
    public static TileType findMatching(int code) {
        for (TileType type : TileType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return VOID;
    }

    public  boolean conforms(Entity entity){
        if(this==VOID || isTile(entity)){
            return family.matches(entity);
        }
        return false;
    }

    private static final ComponentMapper<TileTypeComponent> tileTypeMapper=ComponentMapper.getFor(TileTypeComponent.class);

    public static boolean isTile(Entity entity){
        if(entity!=null && !VOID.family.matches(entity)){
            return tileTypeMapper.has(entity);
        }
        return false;
    }
}