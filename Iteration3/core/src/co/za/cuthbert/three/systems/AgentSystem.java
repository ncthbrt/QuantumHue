package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.EntityType;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.AgentComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.pathing.AStar;
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
    public AgentSystem(){
        priority=0;
        agentFamily = EntityType.AGENT.family;
    }


    @Override
    public void level(Level level) {
        this.level=level;
    }

    @Override
    public void update(float deltaTime) {
        if (level != null) {
            AStar aStar=new AStar(level);
            for (Entity entity : level) {
                if (entity != null && agentFamily.matches(entity)) {
                    AgentComponent agentComponent=agentMapper.get(entity);
                    List<DVector2> path=agentComponent.path();
                    if(path!=null) {
                        if(!validatePath(path)){
                            path.clear();
                            
                            //path.addAll(aStar.route(p))
                        }
                    }
                }
            }

        }
    }

    private boolean validatePath(List<DVector2> path){
        return true;
    }
}
