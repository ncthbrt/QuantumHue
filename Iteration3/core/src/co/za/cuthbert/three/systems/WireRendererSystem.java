package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.WireComponent;
import co.za.cuthbert.three.components.interfaces.ADVector3;
import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireRendererSystem extends EntitySystem{
    private final ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Engine engine;
    private Level level;
    private Family family;
    public WireRendererSystem(ShapeRenderer shapeRenderer,OrthographicCamera camera) {
        this.family=TileType.WIRE.family;
        this.shapeRenderer=shapeRenderer;
        this.camera=camera;
    }

    public void setLevel(Level level){
        this.level=level;
    }


    public void setCamera(OrthographicCamera camera) {
        this.camera=camera;
    }

    @Override
    public void update(float deltaTime) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if(level!=null){
            for(Entity entity: level){
                if(entity!=null){
                    processEntity(entity,deltaTime);
                }
            }
        }

        shapeRenderer.end();
    }


    private static final ComponentMapper<WireComponent> wireComponentMapper=ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<PortComponent> portMapper=ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<DVector3> discretePositionMapper=ComponentMapper.getFor(DVector3.class);


    public void processEntity(Entity wire, float deltaTime) {

        PortComponent ports = portMapper.get(wire);
        WireComponent wireComponent = wireComponentMapper.get(wire);
        ADVector3 position=discretePositionMapper.get(wire);

        boolean[] portMap = ports.getPortMap();


        shapeRenderer.setColor(1, 1, 1, 1);
        int attachedPorts=0;
        //TODO Add method to composite lines into different coloured regions. Also link up all the current systems I have so far and add it to the level editor
        for (int j = 1; j >= -1; --j) {
            for (int i = -1; i <= 1; i++) {
                if (j != 0 || i != 0) {
                    if (portMap[3*(j+1)+i+1] && position.x() + i < level.width() && position.y() +j< level.height() && position.x() + i >=0 && position.y() +j>=0) {
                        shapeRenderer.line(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE, position.x() * Config.TILE_SIZE + i * Config.TILE_SIZE/2, position.y() * Config.TILE_SIZE + j * Config.TILE_SIZE/2);
                        ++attachedPorts;
                    }
                }

            }

        }
        if(attachedPorts==0){
            shapeRenderer.circle(position.x()*Config.TILE_SIZE,position.y()*Config.TILE_SIZE,Config.TILE_SIZE/2);
        }
    }


}
