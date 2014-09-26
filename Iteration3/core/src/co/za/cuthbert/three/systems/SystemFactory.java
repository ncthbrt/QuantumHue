package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.LevelChanger;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class SystemFactory {
    public static void addToEngine(PooledEngine engine, LevelChanger changer) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        WireSystem wireSystem = new WireSystem();
        WireRendererSystem wireRendererSystem = new WireRendererSystem(shapeRenderer);

        engine.addSystem(wireSystem);
        engine.addSystem(wireRendererSystem);
        changer.addListener(wireSystem);
        changer.addListener(wireRendererSystem);
    }
}
