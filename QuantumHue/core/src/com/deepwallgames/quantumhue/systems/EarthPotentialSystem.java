package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.DigitallyTraversable;
import com.deepwallgames.quantumhue.components.EntityTypeComponent;
import com.deepwallgames.quantumhue.components.PortComponent;
import com.deepwallgames.quantumhue.value_objects.DijkstraNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by nick_000 on 09/11/2015.
 */
public class EarthPotentialSystem extends EntitySystem implements LevelChangeListener{
    private final EntityType ground= EntityType.GROUND;
    private final ComponentMapper<EntityTypeComponent> entityTypeMapper=ComponentMapper.getFor(EntityTypeComponent.class);
    private final ComponentMapper<DVector2> positionMapper=ComponentMapper.getFor(DVector2.class);
    private final ComponentMapper<DigitallyTraversable> traversableComponentMapper=ComponentMapper.getFor(DigitallyTraversable.class);
    private final ComponentMapper<PortComponent> portComponentMapper=ComponentMapper.getFor(PortComponent.class);

    public EarthPotentialSystem(){
        priority=0;
    }
    private Level level;

    @Override
    public void update(float deltaTime) {
        if(level!=null) {
            resetEarthPotential();
            for (Entity entity : level) {
                if(entity!=null && ground.family.matches(entity) && entityTypeMapper.get(entity).tileType()==ground){
                    calculateEarthPotential(entity);
                }
            }
        }

    }

    private void resetEarthPotential(){
        for (Entity entity : level) {
            if(entity!=null && portComponentMapper.has(entity)){
                portComponentMapper.get(entity).resetGroundDirection();
            }
        }
    }

    private void calculateEarthPotential(Entity ground){
        HashMap<DijkstraNode,DijkstraNode> visited= new HashMap<DijkstraNode, DijkstraNode>();
        PriorityQueue<DijkstraNode> nodes=new PriorityQueue<DijkstraNode>();
        nodes.add(new DijkstraNode(ground,0,null));
        DVector2 currentPosition;

        while (!nodes.isEmpty()) {
            DijkstraNode node=nodes.poll();
            currentPosition=positionMapper.get(node.entity);
            PortComponent currentPort=portComponentMapper.get(node.entity);

            visited.put(node, node);

            for (int i = -1; i<=1 ; ++i) {

                for (int j = -1; j<=1; ++j) {
                    if(i==0 && j==0) {
                        continue;
                    }
                    if(currentPort.portMask(PortComponent.portNumber(i, j))) {
                        Entity entity = level.get(currentPosition.x() + i, currentPosition.y() + j);

                        if (entity != null && (node.parent==null ||!node.parent.equals(entity))) {
                            PortComponent portComponent = portComponentMapper.get(entity);
                            int adjPort = PortComponent.adjacentPortNumber(PortComponent.portNumber(i, j));
                            DijkstraNode newNode = new DijkstraNode(entity, node.distanceToGround + 1,node.entity);
                            portComponent.groundDirection(adjPort, true);
                            if (!visited.containsKey(newNode)) {
                                nodes.add(newNode);
                            }
                        }
                    }
                }
            }
        }
    }



    @Override
    public void level(Level level) {
        this.level=level;
    }
}