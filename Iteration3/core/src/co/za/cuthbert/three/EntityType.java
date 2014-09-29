package co.za.cuthbert.three;

import co.za.cuthbert.three.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.utils.Bits;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum EntityType {

    WIRE(0, PortComponent.class, WireComponent.class),
    INVERTER(1),
    TERMINATOR(2),
    EXIT(3),
    TURN_TABLE(4),
    SLIDER(5),
    POWER_SOURCE(6, ColourComponent.class, PortComponent.class),
    ISOLATOR(7),
    TRANSISTOR(8),
    GROUND(9),
    MOMENTARY_SWITCH(10),
    SWITCH(11, SwitchComponent.class),
    VOID(13, VoidComponent.class),
    FILTER(14, ColourComponent.class),
    AGENT(15, ColourComponent.class, AgentComponent.class);

    EntityType(int code, Class<? extends Component>... compulsoryComponents) {
        this.code = code;
        this.family=Family.getFor(ComponentType.getBitsFor(compulsoryComponents), ComponentType.getBitsFor(DVector2.class),new Bits());
        this.requiredComponents=compulsoryComponents;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public final Family family;
    public final Class<? extends Component> [] requiredComponents;
    public static EntityType findMatching(int code) {
        for (EntityType type : EntityType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return VOID;
    }

    public  boolean conforms(Entity entity){
        return (this == VOID || isTile(entity)) && family.matches(entity);
    }

    private static final ComponentMapper<EntityTypeComponent> tileTypeMapper=ComponentMapper.getFor(EntityTypeComponent.class);

    public static boolean isTile(Entity entity){
        return entity != null && !VOID.family.matches(entity) && tileTypeMapper.has(entity);
    }
}
