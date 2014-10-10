package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentStateComponent extends Component implements Pool.Poolable{


    public State state;

    @Override
    public void reset() {
        state = State.NORMAL;
    }

    public enum State {
        NORMAL, MERGING, DYING, DEAD;
    }

    public void state(State state){
        this.state=state;
    }
    public AgentStateComponent(){
        reset();
    }


}
