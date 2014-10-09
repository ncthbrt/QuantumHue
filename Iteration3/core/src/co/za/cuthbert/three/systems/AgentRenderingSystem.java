package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.EntityType;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.AgentComponent;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentRenderingSystem  extends EntitySystem implements LevelChangeListener {
    private final Family agentFamily;
    private final ShapeRenderer shapeRenderer;
    public AgentRenderingSystem(ShapeRenderer shapeRenderer){
        super.priority=5;
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
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                for (Entity entity : level) {
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
            shapeRenderer.setColor(colour.red()/255f,colour.green()/255f,colour.blue()/255f,colour.alpha()/255f);
            Vector2 position=agentComponent.position();
            shapeRenderer.circle(position.x, position.y, Config.TILE_SIZE/1.8f);

            final float offsetHyp=Config.TILE_SIZE/4f;
            final float offsetXY=(float)Math.sqrt(offsetHyp/2f);

            List<Colour> componentColours=discreteColour.toComponentColours();


            for(int i=0; i<componentColours.size(); ++i){
                Colour component=componentColours.get(i);

            }
            if(discreteColour.red) {
                shapeRenderer.setColor(1f, 0, 0, 1f);
                shapeRenderer.circle(position.x-offsetXY, position.y+offsetXY, Config.TILE_SIZE/8f);

            }
            if(discreteColour.green) {
                shapeRenderer.setColor(0, 1f, 0, 1f);
                shapeRenderer.circle(position.x+offsetXY, position.y+offsetXY, Config.TILE_SIZE/8f);
            }
            if(discreteColour.blue) {
                shapeRenderer.setColor(0, 0, 1f, 1f);
                shapeRenderer.circle(position.x, position.y-offsetHyp, Config.TILE_SIZE/8f);
            }
        }
    }

