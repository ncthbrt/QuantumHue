package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.EntityType;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.AgentComponent;
import co.za.cuthbert.three.components.AgentStateComponent;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.pathing.AStar;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentSystem extends EntitySystem implements LevelChangeListener{
    private Level level;
    private final Family agentFamily;
    private static final ComponentMapper<AgentComponent> agentMapper = ComponentMapper.getFor(AgentComponent.class);
    private static final ComponentMapper<AgentStateComponent> agentStateMapper = ComponentMapper.getFor(AgentStateComponent.class);
    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    public AgentSystem(){
        priority=7;
        agentFamily = EntityType.AGENT.family;
    }


    @Override
    public void level(Level level) {
        this.level=level;
    }

    private boolean powered(AgentComponent agentComponent, ColourComponent colourComponentl){
        Vector2 position=agentComponent.position();
        int x=Math.round(position.x);
        int y=Math.round(position.y);
        level.get(x,y);
        return true;
    }

    @Override
    public void update(float deltaTime) {
        if (level != null && level.stepping()) {
            AStar star = new AStar(level);

            for (Entity agent : level.agents()) { //Update positions
                AgentStateComponent agentState=agentStateMapper.get(agent);
                if(agentState.state== AgentStateComponent.State.POWERED) {
                    AgentComponent agentComponent = agentMapper.get(agent);
                    List<DVector2> path = agentComponent.path();
                    if (path != null) {
                        if (!star.validPath(path, agent)) {
                            DVector2 target = path.get(path.size() - 1);
                            agentComponent.path(star.route(agentComponent.currentTile(), target, agent));
                        }
                    }
                    agentComponent.advance(deltaTime);
                }
           }

           level.updateAgentPositions(); //Prepare for collision detection

            HashSet<Entity> removedAgents=new HashSet<Entity>();
            for (Entity agent : level.agents()) { //Collision detection

                if(removedAgents.contains(agent)){
                    continue;
                }
                AgentComponent agentComponent=agentMapper.get(agent);
                for(Entity neighborAgent:level.nearbyAgents(agent)){
                    if(removedAgents.contains(neighborAgent)){
                        continue;
                    }
                    AgentComponent neighborAgentComponent=agentMapper.get(neighborAgent);
                    if(overlapping(agentComponent,neighborAgentComponent)) {

                        agentStateMapper.get(agent).state(AgentStateComponent.State.MERGING);
                            if ((neighborAgentComponent.nextTile() != null && agentComponent.nextTile() != null && neighborAgentComponent.nextTile().equals(agentComponent.nextTile()))) {
                                float delta = 1 - agentComponent.between();
                                if (agentComponent.radius / 20f > Config.TILE_SIZE * delta) {
                                    DiscreteColour mergedColour = DiscreteColour.add(colourMapper.get(agent).colour(), colourMapper.get(neighborAgent).colour());
                                    colourMapper.get(agent).colour(mergedColour);
                                    removedAgents.add(neighborAgent);
                                    agentStateMapper.get(agent).state(AgentStateComponent.State.POWERED);
                                } else {
                                    agentComponent.between(agentComponent.between() + (delta) * AgentComponent.movementSpeed * deltaTime * 8);
                                }
                            } else if ((agentComponent.nextTile() != null && agentComponent.nextTile().equals(neighborAgentComponent.currentTile()))) {
                                float delta = (1 - agentComponent.between() - neighborAgentComponent.between());
                                if (agentComponent.radius / 20f > Config.TILE_SIZE * Math.abs(delta)) {
                                    System.out.println("Scenario 1");
                                    DiscreteColour mergedColour = DiscreteColour.add(colourMapper.get(agent).colour(), colourMapper.get(neighborAgent).colour());
                                    colourMapper.get(agent).colour(mergedColour);
                                    removedAgents.add(neighborAgent);
                                    agentStateMapper.get(agent).state(AgentStateComponent.State.POWERED);
                                } else {
                                    agentComponent.between(agentComponent.between() + ((delta) * AgentComponent.movementSpeed * deltaTime * 8));
                                }
                            }
                    }


                }
            }
            for (Entity agent:removedAgents){
                level.removeAgent(agent);
            }

        }
    }

    private boolean overlapping(AgentComponent agent, AgentComponent neighbor){
        return agent.position().dst(neighbor.position())*Config.TILE_SIZE<=2*(agent.radius);
    }



    @Override
    public boolean checkProcessing() {
        return level!=null;
    }
}
