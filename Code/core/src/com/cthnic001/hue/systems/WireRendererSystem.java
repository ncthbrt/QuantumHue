package com.cthnic001.hue.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cthnic001.hue.Config;
import com.cthnic001.hue.components.PortComponent;
import com.cthnic001.hue.components.WireComponent;
import com.cthnic001.hue.entities.Level;
import com.cthnic001.hue.entities.Tile;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireRendererSystem extends System {
    private final ArrayList<Tile> wires;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    public WireRendererSystem() {
        wires = new ArrayList<Tile>();
        shapeRenderer = null;
    }


    public void set(ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
    }

    private Level level;
    @Override
    public void added(Level level) {
        this.level = level;
    }

    @Override
    public void removed() {

    }

    @Override
    public void update(float deltaTime) {
        for (Tile wire : wires) {
            PortComponent ports = (PortComponent) wire.getComponent(PortComponent.class);
            WireComponent wireComponent = (WireComponent) wire.getComponent(WireComponent.class);
            boolean[] portMap = ports.getPortMap();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 1, 1);
            int count = 0;

            //TODO Add method to composite lines into different coloured regions. Also link up all the current systems I have so far and add it to the level editor
            for (int j = -1; j <= 1; j++) {
                for (int i = -1; i <= 1; i++) {
                    if (j != wire.position.y || i != wire.position.y) {
                        if (wire.position.x + i < level.width() && wire.position.y < level.height()) {
                            shapeRenderer.setColor();
                            shapeRenderer.line(wire.position.x * Config.TILE_SIZE, wire.position.y * Config.TILE_SIZE, wire.position.x * Config.TILE_SIZE + i * Config.TILE_SIZE / 2, wire.position.y * Config.TILE_SIZE - j * Config.TILE_SIZE / 2);
                        }
                        count++;
                    }

                }
            }
            shapeRenderer.end();
        }
    }




    @Override
    public boolean checkProcessing() {
        return false;
    }

    @Override
    public void reset() {
        wires.clear();
    }
}
