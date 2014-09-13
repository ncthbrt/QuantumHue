package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.WireComponent;
import co.za.cuthbert.three.components.interfaces.ADVector3;
import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireSystem extends EntitySystem {
    private Level level;
    private static final ComponentMapper<PortComponent> portMapper=ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<WireComponent> wireMapper=ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<DVector3> positionMapper=ComponentMapper.getFor(DVector3.class);
    private ImmutableArray<Entity> wires;
    private Family wireSystemFamily;
    private Engine engine;
    public WireSystem()
    {
        wireSystemFamily=(Family.getFor(PortComponent.class, WireComponent.class, DVector3.class));
    }


    @Override
    public void addedToEngine(Engine engine) {
        this.engine=engine;
    }

    public void setLevel(Level level){
        this.level=level;
    }



    @Override
    public void update(float deltaTime) {
        if(level!=null) {
            final float advance = deltaTime * level.advancementRate;

            //Update colours
            for (Entity entity : level) {
                if(entity!=null && wireSystemFamily.matches(entity)) {
                    synchronise(entity);
                }
            }
            //Cull colours
            for (Entity entity : level) {
                if(entity!=null && wireSystemFamily.matches(entity)) {
                    cull(entity);
                }
            }

            //Sanitise colours
            for (Entity entity : level) {
                if(entity!=null && wireSystemFamily.matches(entity)) {
                    sanitise(entity);
                }
            }

            //Advance
            for (Entity entity : level) {
                if(entity!=null && wireSystemFamily.matches(entity)) {
                    advance(entity, advance);
                }
            }
        }
    }


    private void synchronise(Entity entity) {
        WireComponent wire = wireMapper.get(entity);
        PortComponent port = portMapper.get(entity);
        ADVector3 position=positionMapper.get(entity);
        port.setNeighboringPorts(level, position.x(), position.y(), position.z());
        port.setOutgoingPortColours(wire.getOutgoingPortColours());
        wire.setIncomingPortColours(port.getIncomingPortColours());
    }

    private void sanitise(Entity entity) {
        PortComponent port = portMapper.get(entity);
        port.sanitisePorts();
    }
    private void cull(Entity entity) {
        PortComponent port = portMapper.get(entity);
        port.cullPorts();
    }


    private void advance(Entity entity, float advance) {
        WireComponent wire = wireMapper.get(entity);
        wire.advance(advance);
    }

}
