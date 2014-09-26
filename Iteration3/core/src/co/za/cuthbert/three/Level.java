package co.za.cuthbert.three;


import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.TileTypeComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class Level implements EntityListener, Iterable<Entity>, GestureDetector.GestureListener {
    private Entity[][] level;
    private int width, height;

    private boolean stepping = true;

    public boolean stepping() {
        return stepping;
    }

    public void pause() {
        stepping = false;
    }

    public void resume() {
        stepping = true;
    }


    private static final ComponentMapper<TileTypeComponent> tileTypeMapper = ComponentMapper.getFor(TileTypeComponent.class);
    private static final ComponentMapper<DVector2> positionMapper = ComponentMapper.getFor(DVector2.class);


    private final OrthographicCamera camera;

    private final PooledEngine engine;

    public PooledEngine engine() {
        return engine;
    }

    public OrthographicCamera camera() {
        return camera;
    }

    private static final float defaultAdvancementRate = 0.4f;
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

    }


    public void addTile(Entity entity) {
        if (TileType.isTile(entity) && positionMapper.has(entity)) {
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
        }
        engine.addEntity(entity);
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
                    positionMapper.get(level[j][i]).set(i, j);
                }
            }
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (TileType.isTile(entity) && positionMapper.has(entity)) {
            DVector2 position = positionMapper.get(entity);
            level[position.y()][position.x()] = null;
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
            for (int j = 0; j < height; j++) {
                System.arraycopy(this.level[j], 0, level[j], 0, width);
            }
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
