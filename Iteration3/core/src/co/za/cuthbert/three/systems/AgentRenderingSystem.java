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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentRenderingSystem  extends EntitySystem implements LevelChangeListener {
    private final Family agentFamily;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;

    public AgentRenderingSystem(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        super.priority=8;

        agentFamily = EntityType.AGENT.family;
        this.shapeRenderer=shapeRenderer;
        this.spriteBatch=spriteBatch;
        buffer=new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),false);
        agentRender = new TextureRegion(buffer.getColorBufferTexture(),0,0, buffer.getWidth(),buffer.getHeight());
        agentRender.flip(false,true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }
    private final FrameBuffer buffer;
    private final TextureRegion agentRender;
    private final OrthographicCamera camera;
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
                if(Config.DEBUG){
                    for (Entity entity : level.agents()) {
                        renderDebugTrail(entity);
                    }
                }

                shapeRenderer.end();
                buffer.begin();
                Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
                shapeRenderer.setProjectionMatrix(level.camera().combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                for (Entity entity : level.agents()) {

                    if (entity != null && agentFamily.matches(entity)) {
                        renderAgent(entity);
                    }
                }
                shapeRenderer.end();

                buffer.end();


                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                OrthographicCamera orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                orthographicCamera.position.set(orthographicCamera.viewportWidth/2f,orthographicCamera.viewportHeight/2f, 0);
                orthographicCamera.update();

                spriteBatch.setProjectionMatrix(orthographicCamera.combined);

                spriteBatch.begin();
                spriteBatch.draw(agentRender, 0, 0);
                spriteBatch.end();






            }
        }

        private void renderAgent(Entity agent) {
            DiscreteColour discreteColour = colourMapper.get(agent).colour();
            AgentComponent agentComponent=agentMapper.get(agent);

            Colour colour=discreteColour.toColour();

            Vector2 position=agentComponent.position();
            List<Colour> componentColours = discreteColour.toComponentColours();
            float angle=(360f/componentColours.size());
            for(int i=0; i<componentColours.size(); ++i) {
                Colour cColour=componentColours.get(i);
                shapeRenderer.setColor(cColour.red()/255f,cColour.green()/255f,cColour.blue()/255f,1f);
                shapeRenderer.arc(position.x * Config.TILE_SIZE, position.y * Config.TILE_SIZE, agentComponent.radius + 2,i*angle, angle,100);

            }
            shapeRenderer.setColor(colour.red()/255f,colour.green()/255f,colour.blue()/255f,1f);
            shapeRenderer.circle(position.x* Config.TILE_SIZE, position.y* Config.TILE_SIZE, agentComponent.radius,100);

        }
        private void renderDebugTrail(Entity agent){
            DiscreteColour discreteColour = colourMapper.get(agent).colour();
            Colour colour=discreteColour.toColour();

            AgentComponent agentComponent=agentMapper.get(agent);
            shapeRenderer.setColor(colour.red()/255f,colour.green()/255f,colour.blue()/255f,0.4f);
            if(agentComponent.path()!=null) {
                for (DVector2 point: agentComponent.path()){
                    shapeRenderer.circle(point.x()* Config.TILE_SIZE, point.y()* Config.TILE_SIZE, agentComponent.radius/4f,20);
                }
            }
        }

    }



