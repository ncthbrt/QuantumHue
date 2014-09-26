package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.SwitchComponent;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.Level;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PowerSystem extends EntitySystem implements LevelChangeListener {
    private final Family powerPortFamily;

    public PowerSystem() {
        powerPortFamily = (TileType.POWER_SOURCE.family);
    }

    private Level level;

    public void level(Level level) {
        this.level = level;
    }

    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);
    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<SwitchComponent> switchComponentMapper = ComponentMapper.getFor(SwitchComponent.class);

    @Override
    public void update(float deltaTime) {
        if (level != null) {
            for (Entity entity : level) {
                if (entity != null && powerPortFamily.matches(entity)) {
                    updatePowerPort(entity);
                }
            }
        }
    }


    public void updatePowerPort(Entity powerPort) {
        boolean on = true;
        if (switchComponentMapper.has(powerPort)) {
            on = switchComponentMapper.get(powerPort).on;
        }
        PortComponent port = portMapper.get(powerPort);
        DVector2 position = discretePositionMapper.get(powerPort);
        Colour colour = colourMapper.get(powerPort).colour();

        if (on) {
            port.setOutgoingPortColours(colour);
        } else {
            port.setOutgoingPortColours(new Colour());
        }
        port.setNeighboringPorts(level, position.x(), position.y());
    }
}
