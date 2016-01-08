package com.deepwallgames.quantumhue.systems;

import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.components.PortComponent;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PortSystem extends EntitySystem implements LevelChangeListener {
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<DVector2> positionMapper = ComponentMapper.getFor(DVector2.class);

    private Level level = null;

    public PortSystem() {
        priority = 0;
    }

    @Override
    public void level(Level level) {
        this.level = level;
    }

    @Override
    public void update(float deltaTime) {
        if (level != null) {
            for (Entity entity : level) { //Update adjacent colours
                updatePort(entity);
            }

            for (Entity entity : level) { //Cull ports
                cullPort(entity);
            }
        }
    }

    private void updatePort(Entity entity) {
        if (entity != null && EntityType.isTile(entity) && portMapper.has(entity)) {
            DVector2 position = positionMapper.get(entity);
            PortComponent port = portMapper.get(entity);
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (!(i == 0 && j == 0) && position.x() + i >= 0 && position.x() + i < level.width() && position.y() + j >= 0 && position.y() + j < level.height()) {
                        Colour colour = port.outgoingPortColour(PortComponent.portNumber(i, j));
                        Entity adjacentEntity = level.get(position.x() + i, position.y() + j);
                        if (adjacentEntity != null && portMapper.has(adjacentEntity)) {
                            PortComponent adjacentPort = portMapper.get(adjacentEntity);
                            adjacentPort.incomingPortColour(PortComponent.adjacentPortNumber(PortComponent.portNumber(i, j)), colour);
                        }
                    }
                }
            }
        }
    }

    public void cullPort(Entity entity) {
        if (entity != null && EntityType.isTile(entity) && portMapper.has(entity)) {

            DVector2 position = positionMapper.get(entity);
            PortComponent component = portMapper.get(entity);

            boolean[] adjacentPorts = getAdjacentPorts(position);
            for (int i = 0; i < 9; ++i) {
                if(i==4) continue;
                if(!adjacentPorts[i]){
                    component.incomingPortColour(i, DiscreteColour.ALPHA.toColour());
                }
                component.mask(i, adjacentPorts[i]);
            }


            if (component.portMask(8) && component.portMask(5)) {
                component.incomingPortColour(8,null);
                component.mask(8, false);
            }

            if (component.portMask(5) && component.portMask(2)) {
                component.incomingPortColour(2,null);
                component.mask(2, false);
            }

            if (component.portMask(1) && component.portMask(2)) {
                component.incomingPortColour(2,null);
                component.mask(2, false);
            }
            if (component.portMask(1) && component.portMask(0)) {
                component.incomingPortColour(0,null);
                component.mask(0, false);
            }

            if (component.portMask(3) && component.portMask(0)) {
                component.incomingPortColour(0, null);
                component.mask(0, false);
            }
            if (component.portMask(3) && component.portMask(6)) {
                component.incomingPortColour(6,null);
                component.mask(6, false);
            }

            if (component.portMask(7) && component.portMask(6)) {
                component.incomingPortColour(6,null);
                component.mask(6,false);
            }
            if (component.portMask(7) && component.portMask(8)) {
                component.incomingPortColour(8,null);
                component.mask(8, false);
            }

        }
    }


    public boolean[] getAdjacentPorts(DVector2 position) {
        boolean[] adjacentPorts = new boolean[9];
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (!(i == 0 && j == 0) && position.x() + i >= 0 && position.x() + i < level.width() && position.y() + j >= 0 && position.y() + j < level.height()) {
                    Entity adjacentEntity = level.get(position.x() + i, position.y() + j);
                    adjacentPorts[PortComponent.portNumber(i, j)] = adjacentEntity != null;
                }
            }
        }
        return adjacentPorts;
    }


}

