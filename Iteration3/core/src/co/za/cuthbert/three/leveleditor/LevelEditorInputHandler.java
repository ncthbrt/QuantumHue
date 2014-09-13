package co.za.cuthbert.three.leveleditor;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.factories.TileFactory;
import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class LevelEditorInputHandler extends InputListener {
    private final OrthographicCamera camera;
    private final PooledEngine engine;
    private Level level;
    public LevelEditorInputHandler(OrthographicCamera camera, PooledEngine engine){
        this.camera=camera;
        this.engine=engine;
    }
    public void setLevel(Level level){
        this.level=level;
    }

    private int currentDepth=0;

    public int currentDepth() {
        return currentDepth;
    }
    public void currentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    private TileType tool=TileType.WIRE;

    public void tool(TileType tool) {
        this.tool = tool;
    }

    public TileType tool() {
        return tool;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch Event initiated");
        if(level!=null){
            System.out.println("Handling touch event");
            Vector3 worldCoords=camera.unproject(new Vector3(x, Gdx.graphics.getHeight()-y,0));
            int worldX=(int)(worldCoords.x/ Config.TILE_SIZE+0.5f);
            int worldY=(int)(worldCoords.y/ Config.TILE_SIZE+0.5f);
            if(worldX>=0 && worldY>=0 && worldX<level.width() && worldY<level.height()) {
                System.out.println("Creating tile at " + worldX + ", " + worldY);
                Entity tile = level.get(worldX, worldY, currentDepth);
                if (tile != null) {
                    engine.removeEntity(tile);
                }else {
                    engine.addEntity(TileFactory.createWire(engine, worldX, worldY, currentDepth));
                }
                return true;
            }
        }
        return super.touchDown(event,x,y,pointer,button);
    }
}
