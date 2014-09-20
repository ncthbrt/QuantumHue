package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.SwitchComponent;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PowerRendererSystem extends EntitySystem {


    private static final ComponentMapper<DVector3> discretePositionMapper=ComponentMapper.getFor(DVector3.class);
    private static final ComponentMapper<ColourComponent> colourMapper=ComponentMapper.getFor(ColourComponent.class);
    private static final ComponentMapper<SwitchComponent> switchComponentMapper=ComponentMapper.getFor(SwitchComponent.class);
    private Level level;

    private final SpriteBatch batch;
    private final Texture powerPort;
    private final Family powerPortFamily;
    public PowerRendererSystem(SpriteBatch batch) {
        powerPortFamily=TileType.POWER_SOURCE.family;
        priority=2;
        this.batch=batch;
        powerPort=(new Texture(Gdx.files.internal("power_port.png")));
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        powerPort.dispose();
    }

    @Override
    public void update(float deltaTime) {
        if(level!=null){
            batch.setProjectionMatrix(level.camera().combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            for (Entity entity : level) {
                if(entity!=null && powerPortFamily.matches(entity)) {
                    renderPowerPort(entity);
                }
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void renderPowerPort(Entity powerPort) {
        boolean on=true;
        DVector3 position= discretePositionMapper.get(powerPort);
        Colour colour= colourMapper.get(powerPort).colour();
        if(switchComponentMapper.has(powerPort)){
            SwitchComponent switchComponent=switchComponentMapper.get(powerPort);
            on=switchComponent.on;
        }
        if(on){
            batch.setColor(colour.red()/255f,colour.green()/255f,colour.blue()/255f,1f);
        }else{
            batch.setColor(1,1,1,0.5f);
        }
        batch.draw(this.powerPort,position.x()- Config.TILE_SIZE,position.y()- Config.TILE_SIZE);
    }
}
