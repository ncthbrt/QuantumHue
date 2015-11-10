package com.deepwallgames.quantumhue.systems;

import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.WireComponent;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.components.ColourComponent;
import com.deepwallgames.quantumhue.components.PortComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class WireSystem extends EntitySystem implements LevelChangeListener {
    private Level level;
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<WireComponent> wireMapper = ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<DVector2> positionMapper = ComponentMapper.getFor(DVector2.class);
    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    private Family wireSystemFamily;

    public WireSystem() {
        priority = 1;
        wireSystemFamily = EntityType.WIRE.family;
    }

    @Override
    public void update(float deltaTime) {
        if (level != null && level.stepping()){
                final float advance = deltaTime * level.advancementRate;
                for (Entity entity : level) {
                    if (entity != null && wireSystemFamily.matches(entity)) {
                        advance(entity, advance);
                    }
                }
        }
    }


    private void advance(Entity entity, float advance) {
        PortComponent port = portMapper.get(entity);
        WireComponent wire = wireMapper.get(entity);
        wire.incomingPortColours(port.incomingPortColours());
        wire.advance(advance,port.groundDirection());
    }

    public void level(Level level) {
        this.level = level;
    }

    @Override
    public boolean checkProcessing() {
        //return (level!=null && level.stepping());
        return true;
    }
}
