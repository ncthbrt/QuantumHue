package co.za.cuthbert.three;


import co.za.cuthbert.three.components.AgentComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.EntityTypeComponent;
import co.za.cuthbert.three.pathing.AStar;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level implements EntityListener, Iterable<Entity>, GestureDetector.GestureListener {
    private Entity[][] level;
    private ArrayList<ArrayList<ArrayList<Entity>>> agentMap;

    private final HashSet<Entity> agents;

    /**This method is to be called at the beginning of every frame.
     * Agents are dynamic, so it is not that inefficient to rebuild
     * the mapping as opposed to repairing it.
     */
    public void resetAgentMapping(){
            agentMap=new ArrayList<ArrayList<ArrayList<Entity>>>(height);
            agentMap.ensureCapacity(height);
            for(int j=0; j<height; ++j){
                ArrayList<ArrayList<Entity>> row = new ArrayList<ArrayList<Entity>>(width);
                for(int i=0; i<width; ++i){
                    row.add(new ArrayList<Entity>());
                }
                agentMap.add(row);
            }
    }

    public HashSet<Entity> agents(){
        return agents;
    }

    public void addAgent(Entity agent){
        agents.add(agent);
    }

    public void removeAgent(Entity agent){
        agents.remove(agent);
    }

    public void updateAgentPositions(){
        resetAgentMapping();
        for(Entity agent: agents) {
            AgentComponent agentComponent = agentMapper.get(agent);
            DVector2 position = agentComponent.currentTile();
            agentMap.get(position.y()).get(position.x()).add(agent);
            }
    }


    public List<Entity> nearbyAgents(Entity agent){
        List<Entity> results=new ArrayList<Entity>();
        AgentComponent agentComponent=agentMapper.get(agent);
        DVector2 currentTile=agentComponent.currentTile();
        for(int j=-1; j<=1; ++j){
            for(int i=-1; i<=1; ++i){
                if(currentTile.x()+i>=0 && currentTile.x()+i<width
                   && currentTile.y()+j>=0 && currentTile.y()+j<height){
                  results.addAll(agentMap.get(currentTile.y()+j).get(currentTile.x()+i));
                }
            }
        }
        results.remove(agent);
        return results;
    }


    private int width, height;

    private boolean stepping = false;
    private boolean levelModified = false;

    public boolean levelModified() {
        return levelModified;
    }


    public boolean stepping() {
        return stepping;
    }

    public void pause() {
        stepping = false;
    }

    public void resume() {
        stepping = true;
    }

    private static final ComponentMapper<AgentComponent> agentMapper=ComponentMapper.getFor(AgentComponent.class);
    private static final ComponentMapper<EntityTypeComponent> tileTypeMapper = ComponentMapper.getFor(EntityTypeComponent.class);
    private static final ComponentMapper<DVector2> positionMapper = ComponentMapper.getFor(DVector2.class);


    private final OrthographicCamera camera;

    private final PooledEngine engine;

    public PooledEngine engine() {
        return engine;
    }

    public OrthographicCamera camera() {
        return camera;
    }

    private static final float defaultAdvancementRate = 1f;
    public float advancementRate = defaultAdvancementRate;


    public Entity get(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return level[y][x];
        }
        return null;
    }


    public Level(PooledEngine engine, int width, int height) {
        camera = new OrthographicCamera(width * Config.TILE_SIZE, height * Config.TILE_SIZE);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        level = new Entity[height][width];
        this.engine = engine;
        this.width = width;
        this.height = height;
        resetAgentMapping();
        agents=new HashSet<Entity>();

    }




    public void addTile(Entity entity) {
        if(EntityType.isTile(entity) && tileTypeMapper.get(entity).tileType()!=EntityType.AGENT) {
             if (positionMapper.has(entity)){
                DVector2 position = positionMapper.get(entity);
                DVector2 newPosition = new DVector2();
                if (position.x() >= width) {
                    growMap(position.x() + 1, height);
                    newPosition.x(position.x());
                } else if (position.x() < 0) {
                    newPosition.x(0);
                    growMap(position.x() - 1, height);
                    reconcile();
                    renconcileAgents(-(position.x()),0);
                    camera.translate(-position.x() * Config.TILE_SIZE, 0);
                } else {
                    newPosition.x(position.x());
                }

                if (position.y() >= height) {
                    growMap(width, position.y() + 1);
                    newPosition.y(position.y());
                } else if (position.y() < 0) {
                    newPosition.y(0);
                    growMap(width, position.y() - 1);
                    reconcile();
                    camera.translate(0, -position.y() * Config.TILE_SIZE);

                } else {
                    newPosition.y(position.y());
                }
                camera.update();
                level[newPosition.y()][newPosition.x()] = entity;
                position.set(newPosition.x(), newPosition.y());
            }
            levelModified = true;
            engine.addEntity(entity);
        }
    }

    public void removeTile(Entity entity) {
        engine.removeEntity(entity);
    }


    @Override
    public void entityAdded(Entity entity) {

    }

    public void reconcile() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (level[j][i] != null) {
                    positionMapper.get(get(i, j)).set(i, j);
                }
            }
        }
    }

    public void renconcileAgents(int deltaWidth, int deltaHeight){
        for(Entity agent: agents){
            AgentComponent agentComponent=agentMapper.get(agent);
            agentComponent.shift(deltaHeight,deltaHeight);
        }
        resetAgentMapping();
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (EntityType.isTile(entity)) {
            if(tileTypeMapper.get(entity).tileType()==EntityType.AGENT){
                agents.remove(entity);
            }
            else if (positionMapper.has(entity)) {
                DVector2 position = positionMapper.get(entity);
                level[position.y()][position.x()] = null;
            }
        }
    }


    public int width() {
        return width;
    }

    public int height() {
        return height;
    }


    public void growMap(int newWidth, int newHeight) {
        Entity[][] level = null;

        if (newWidth > this.width) {
            level = new Entity[height][newWidth];
            this.width = newWidth;
        } else if (newHeight > this.height) {
            level = new Entity[newHeight][width];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j], 0, width);
            }
            this.height = newHeight;
        } else if (newHeight < 0) {
            level = new Entity[height - newHeight][width];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j - newHeight - 1], 0, width);
            }
            this.height = height - newHeight;
        } else if (newWidth < 0) {
            level = new Entity[height][width - newWidth];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j], -newWidth - 1, width);
            }
            this.width = width - newWidth;
        }
        if (level != null) {
            this.level = level;
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new Iterator<Entity>() {
            private int count = 0;
            private boolean done = false;

            @Override
            public boolean hasNext() {
                return count < width * height;
            }

            @Override
            public Entity next() {

                int j = count / width;
                int i = count % width;
                count++;
                return level[j][i];
            }

            @Override
            public void remove() {

            }
        };
    }

    public void update(float deltaTime) {
        engine.update(deltaTime);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 worldCoords =camera().unproject(new Vector3(x, y, 0));
        int worldX = Math.round((worldCoords.x) / Config.TILE_SIZE);
        int worldY = Math.round((worldCoords.y) / Config.TILE_SIZE);
        if(get(worldX,worldY)!=null) {
            AStar aStar=new AStar(this);
            final ComponentMapper<AgentComponent> agentComponentMapper=ComponentMapper.getFor(AgentComponent.class);
            for (Entity agent : agents) {
                AgentComponent agentComponent = agentComponentMapper.get(agent);
                if(agentComponent.followingPath()){ //Only allow agents to follow one route at a time.
                    return true;
                }
            }
            for (Entity agent : agents){
                AgentComponent agentComponent=agentComponentMapper.get(agent);
                agentComponent.path(aStar.route(agentComponent.currentTile(), new DVector2(worldX,worldY),agent));
            }
        }
        return true;
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
        camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom);
        camera.update();
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return true;
    }

    private static final float zoomRate = .0001f;

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float zoom = (initialDistance - distance) * zoomRate;
        if (camera.zoom * (1 + zoom) > 0.1f && camera.zoom * (1 + zoom) < 20f) {
            camera.zoom *= (1 + zoom);
            camera.update();
        }
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void dispose() {
        engine.removeAllEntities();
    }
}
