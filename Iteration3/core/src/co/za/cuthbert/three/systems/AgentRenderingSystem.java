package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.EntityType;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.AgentComponent;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentRenderingSystem  extends EntitySystem implements LevelChangeListener {
    private final Family agentFamily;
    private final ShapeRenderer shapeRenderer;
    public AgentRenderingSystem(ShapeRenderer shapeRenderer){
        super.priority=8;
        agentFamily = EntityType.AGENT.family;
        this.shapeRenderer=shapeRenderer;
    }


    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    private static final ComponentMapper<AgentComponent> agentMapper = ComponentMapper.getFor(AgentComponent.class);

    private Level level;
    public void level(Level level) {
        this.level = level;
    }

    @Override
    public void removedFromEngine(Engine engine) {
            super.removedFromEngine(engine);
    }

        @Override
        public void update(float deltaTime) {
            if (level != null) {
                shapeRenderer.setProjectionMatrix(level.camera().combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                for (Entity entity : level.agents()) {

                    if (entity != null && agentFamily.matches(entity)) {
                        renderAgent(entity);
                    }
                }
                shapeRenderer.end();
            }
        }

        public void renderAgent(Entity agent) {
            DiscreteColour discreteColour = colourMapper.get(agent).colour();
            AgentComponent agentComponent=agentMapper.get(agent);

            Colour colour=discreteColour.toColour();
            shapeRenderer.setColor(colour.red()/255f,colour.green()/255f,colour.blue()/255f,1f);
            Vector2 position=agentComponent.position();
            shapeRenderer.circle(position.x* Config.TILE_SIZE, position.y* Config.TILE_SIZE, agentComponent.radius,100);
            if(Config.DEBUG){
                if(agentComponent.path()!=null) {
                    for (DVector2 point: agentComponent.path()){
                        shapeRenderer.circle(point.x()* Config.TILE_SIZE, point.y()* Config.TILE_SIZE, agentComponent.radius/4f,20);
                    }
                }
            }
        }
    }

