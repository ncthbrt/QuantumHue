package com.cthnic001.hue.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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


    @Override
    public void added(Level level) {

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
