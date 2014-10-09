package co.za.cuthbert.three.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentAnimationComponent extends Component{
    public float rotation;

    private enum AnimationState{
        ALIVE, MERGING, DYING, DEAD;
    }

    public AgentAnimationComponent(){

    }

    public void update(float deltaTime){

    }
    public void render(Vector2 position, ShapeRenderer shapeRenderer){

    }


}
