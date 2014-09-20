package co.za.cuthbert.three.listeners;

import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.TileTypeComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Iterator;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level implements EntityListener, Iterable<Entity>{
    private Entity[][][] level;
    private int width, height, depth;
    private static final ComponentMapper<TileTypeComponent> tileTypeMapper=ComponentMapper.getFor(TileTypeComponent.class);
    private static final ComponentMapper<DVector3> positionMapper=ComponentMapper.getFor(DVector3.class);


    private final OrthographicCamera camera;
    private final PooledEngine engine;

    public OrthographicCamera camera(){
        return camera;
    }
    public float advancementRate;
    public Entity get(int x, int y, int z) {
        return level[z][y][x];
    }



    public Level(PooledEngine engine, int width, int height, int depth, float advancementRate){
        camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        level=new Entity[depth][height][width];
        this.engine=engine;
        this.width=width;
        this.height=height;
        this.depth=depth;
        this.advancementRate=advancementRate;
    }


    @Override
    public void entityAdded(Entity entity) {
        if(TileType.isTile(entity) && positionMapper.has(entity)){
            DVector3 position=positionMapper.get(entity);
            level[position.z()][position.y()][position.x()]=entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if(TileType.isTile(entity) && positionMapper.has(entity)){
            DVector3 position=positionMapper.get(entity);
            level[position.z()][position.y()][position.x()]=null;
        }
    }


    public int width(){
        return width;
    }

    public int height() {
        return height;
    }

    public int depth() {
        return depth;
    }

    public void growMap(int width, int height, int depth) {
        if (width > this.width && height > this.height && depth > this.depth) {
            Entity[][][] level = new Entity[depth][height][width];
            this.width = width;
            this.height = height;
            this.depth = depth;
            for (int k = 0; k < depth; k++) {
                for (int j = 0; j < height; j++) {
                    System.arraycopy(this.level[k][j], 0, level[k][j], 0, width);
                }
            }
        } else {
            throw new IllegalArgumentException("You cannot make a level smaller, only larger");
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new Iterator<Entity>() {
            private int count=0;
            private boolean done=false;
            @Override
            public boolean hasNext() {
                return count<width*height*depth;
            }

            @Override
            public Entity next() {

                int k=count/(width*height);
                int j=(count-(k*width*height))/width;
                int i=((count-(k*width*height)/width)%width);
                count++;
                return level[k][j][i];
            }
        };
    }
}
