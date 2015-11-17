package com.deepwallgames.quantumhue;


import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.EntityTypeComponent;
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

    private static final ComponentMapper<EntityTypeComponent> tileTypeMapper = ComponentMapper.getFor(EntityTypeComponent.class);
    private static final ComponentMapper<DVector2> positionMapper = ComponentMapper.getFor(DVector2.class);


    private final OrthographicCamera camera;

    private final Engine engine;

    public Engine engine() {
        return engine;
    }

    public OrthographicCamera camera() {
        camera.viewportHeight=Config.TILE_SIZE*(int) (initialWidth* Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());
        camera.update();
        return camera;
    }

    private static final float defaultAdvancementRate = 1f;
    public float advancementRate = defaultAdvancementRate;


    public Entity get(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if(level[y][x]==null || tileTypeMapper.get(level[y][x]).tileType()==EntityType.VOID){
                return null;
            }
            return level[y][x];
        }
        return null;
    }

    private int initialWidth;
    public Level(Engine engine, int width, int height) {

        initialWidth=width;
        camera = new OrthographicCamera(width * Config.TILE_SIZE, height * Config.TILE_SIZE);
        level = new Entity[height][width];
        this.engine = engine;
        this.width = width;
        this.height = height;
    }

    public void addTile(Entity entity) {

        if(EntityType.isTile(entity)) {
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

    @Override
    public void entityRemoved(Entity entity) {
        if (EntityType.isTile(entity)) {
            if (positionMapper.has(entity)) {
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
        } else if (newWidth < 0) {
            level = new Entity[height][width - newWidth];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j], -newWidth - 1, width);
            }
            this.width = width - newWidth;
        }

        if (newHeight > this.height) {
            level = new Entity[newHeight][width];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j], 0, width);
            }
            this.height = newHeight;
        }
        else if (newHeight < 0) {
            level = new Entity[height - newHeight][width];
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j - newHeight - 1], 0, width);
            }
            this.height = height - newHeight;
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

    public float zoom(){
        return camera.zoom;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void dispose() {
        engine.removeAllEntities();
    }
}
