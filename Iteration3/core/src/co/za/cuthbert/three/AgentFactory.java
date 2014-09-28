package co.za.cuthbert.three;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class AgentFactory {

    private static final Family agentFamily=Family.getFor();
    public boolean isAgent(Entity entity){
        return  (agentFamily.matches(entity));
    }
}
