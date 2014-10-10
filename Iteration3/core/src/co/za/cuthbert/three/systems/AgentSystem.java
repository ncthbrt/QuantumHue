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

    @Override
    public void update(float deltaTime) {
        if (level != null && level.stepping()) {
            AStar star = new AStar(level);

            for (Entity agent : level.agents()) { //Update positions
                AgentStateComponent agentState=agentStateMapper.get(agent);
                if(agentState.state== AgentStateComponent.State.NORMAL) {
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
           level.resetAgentMapping();
           level.updateAgentPositions(); //Prepare for collision detection

            for (Entity agent : level.agents()) { //Collision detection
                AgentComponent agentComponent=agentMapper.get(agent);
                for(Entity neighborAgent:level.nearbyAgents(agent)){
                    AgentComponent neighborAgentComponent=agentMapper.get(neighborAgent);
                    if(overlapping(agentComponent,neighborAgentComponent)){
                        agentStateMapper.get(agent).state(AgentStateComponent.State.MERGING);
                        if(neighborAgentComponent.nextTile()!=null && agentComponent.nextTile()!=null && neighborAgentComponent.nextTile().equals(agentComponent.nextTile())){

                            float delta=neighborAgentComponent.between()-agentComponent.between();
                            if(Math.abs(delta)<0.05f){
                                DiscreteColour mergedColour = DiscreteColour.add(colourMapper.get(agent).colour(), colourMapper.get(neighborAgent).colour());
                                colourMapper.get(agent).colour(mergedColour);
                                level.removeAgent(neighborAgent);
                                agentStateMapper.get(agent).state(AgentStateComponent.State.NORMAL);
                            }
                            else{
                                agentComponent.between(agentComponent.between()+delta*(deltaTime*1.2f));
                            }
                        }else if((neighborAgentComponent.nextTile()!=null && neighborAgentComponent.nextTile().equals(agentComponent.currentTile())) ||(agentComponent.nextTile()!=null && agentComponent.nextTile().equals(neighborAgentComponent.currentTile()))){
                            float delta=neighborAgentComponent.between()-(1-agentComponent.between());
                            if(Math.abs(delta)<0.05f){
                                DiscreteColour mergedColour = DiscreteColour.add(colourMapper.get(agent).colour(), colourMapper.get(neighborAgent).colour());
                                colourMapper.get(agent).colour(mergedColour);
                                level.removeAgent(neighborAgent);
                                agentStateMapper.get(agent).state(AgentStateComponent.State.NORMAL);
                            }
                            else{
                                agentComponent.between(agentComponent.between()+delta*(deltaTime*1.2f));
                            }
                        }else{
                            float delta=neighborAgentComponent.between()-(1-agentComponent.between());
                            if(Math.abs(delta)<0.05f) {
                                DiscreteColour mergedColour = DiscreteColour.add(colourMapper.get(agent).colour(), colourMapper.get(neighborAgent).colour());
                                colourMapper.get(agent).colour(mergedColour);
                                level.removeAgent(neighborAgent);
                                agentStateMapper.get(agent).state(AgentStateComponent.State.NORMAL);
                            }
                        }
                    }

                }
            }

        }
    }

    private boolean overlapping(AgentComponent agent, AgentComponent neighbor){
        return agent.position().dst2(neighbor.position())<=4*(agent.radius*agent.radius)/(Config.TILE_SIZE*Config.TILE_SIZE);
    }



    @Override
    public boolean checkProcessing() {
        return level!=null;
    }
}
