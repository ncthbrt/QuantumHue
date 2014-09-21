package co.za.cuthbert.three.listeners;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.TileTypeComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level implements EntityListener, Iterable<Entity>, GestureDetector.GestureListener{
    private Entity[][] level;
    private int width, height;

    private boolean stepping=false;

    public boolean stepping(){
        return stepping;
    }

    public void pause(){
        stepping=false;
    }

    public void resume(){
        stepping=true;
    }


    private static final ComponentMapper<TileTypeComponent> tileTypeMapper=ComponentMapper.getFor(TileTypeComponent.class);
    private static final ComponentMapper<DVector2> positionMapper=ComponentMapper.getFor(DVector2.class);

    private static final float zoomRate=1f/200;
    private final OrthographicCamera camera;
    private final PooledEngine engine;

    public OrthographicCamera camera(){
        return camera;
    }
    public float advancementRate;
    public Entity get(int x, int y) {
        return level[y][x];
    }



    public Level(PooledEngine engine, int width, int height,float advancementRate){
        camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        level=new Entity[height][width];
        this.engine=engine;
        this.width=width;
        this.height=height;
        this.advancementRate=advancementRate;
    }


    @Override
    public void entityAdded(Entity entity) {
        if(TileType.isTile(entity) && positionMapper.has(entity)){
            DVector2 position=positionMapper.get(entity);

            if(position.x()>width) {
                growMap(position.x(),height);
            }
            else if(position.x()<0){
                growMap(position.x(),height);
                camera.translate(-position.x()* Config.TILE_SIZE,0);
                reconcile();
            }

            if(position.y()>height) {
                growMap(width,position.y());
            }
            else if(position.y()<0){
                growMap(width,position.y());
                camera.translate(0,-position.y()* Config.TILE_SIZE);
                reconcile();
            }
            camera.update();
            level[position.y()][position.x()]=entity;
        }
    }
    public void reconcile(){
        for(int j=0; j<height; j++){
            for(int i=0; i<width;i++){
                if(level[j][i]!=null) {
                    positionMapper.get(level[j][i]).set(i,j);
                }
            }
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if(TileType.isTile(entity) && positionMapper.has(entity)){
            DVector2 position=positionMapper.get(entity);
            level[position.y()][position.x()]=null;
        }
    }


    public int width(){
        return width;
    }

    public int height() {
        return height;
    }



    public void growMap(int extremeX, int extremeY) {
        if(extremeX>this.width && extremeY>this.height) {
                Entity[][] level = new Entity[extremeY][extremeX];
                this.width = extremeX;
                this.height = extremeY;
                for (int j = 0; j < extremeY; j++) {
                        System.arraycopy(this.level[j], 0, level[j], 0, extremeX);
                }
        }else if(extremeY<0){
            Entity[][] level = new Entity[height-extremeY][extremeX];
            this.width = extremeX;
            this.height =height-extremeY;
            for (int j = 0; j < extremeY; j++) {
                System.arraycopy(this.level[j], 0, level[j-extremeY],0, width);
            }
        }else if(extremeX<0){
            Entity[][] level = new Entity[height][width-extremeX];
            this.width = width-extremeX;
            this.height = extremeY;
            for (int j = 0; j < extremeY; j++) {
                System.arraycopy(this.level[j], 0, level[j],-extremeX,width+extremeX);
            }
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new Iterator<Entity>() {
            private int count=0;
            private boolean done=false;
            @Override
            public boolean hasNext() {
                return count<width*height;
            }

            @Override
            public Entity next() {

                int j=count/width;
                int i=count%width;
                count++;
                return level[j][i];
            }
        };
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.translate(deltaX,deltaY);
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float zoom=(distance-initialDistance)/zoomRate;
        camera.zoom+=zoom;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
