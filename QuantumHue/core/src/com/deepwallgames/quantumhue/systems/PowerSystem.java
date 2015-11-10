package com.deepwallgames.quantumhue.systems;

import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.SwitchComponent;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.components.ColourComponent;
import com.deepwallgames.quantumhue.components.PortComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PowerSystem extends EntitySystem implements LevelChangeListener {
    private final Family powerSourceFamily;

    public PowerSystem() {
        priority = 0;
        powerSourceFamily = (EntityType.POWER_SOURCE.family);
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
        if (level != null && level.stepping()) {
            for (Entity entity : level) {
                if (entity != null && powerSourceFamily.matches(entity)) {
                    updatePowerPort(entity);
                }
            }
        }
    }


    public void updatePowerPort(Entity powerPort) {
        PortComponent port = portMapper.get(powerPort);
        Colour colour = colourMapper.get(powerPort).colour();
        port.outgoingPortColours(colour);
    }
}
