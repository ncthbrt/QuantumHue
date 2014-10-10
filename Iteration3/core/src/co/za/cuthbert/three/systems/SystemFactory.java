package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.LevelChanger;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class SystemFactory {
    public static void addToEngine(PooledEngine engine, LevelChanger changer, SpriteBatch batch, TextureAtlas atlas) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        PortSystem portSystem = new PortSystem();

        WirePortSystem wirePortSystem = new WirePortSystem();
        WireSystem wireSystem = new WireSystem();
        WireRendererSystem wireRendererSystem = new WireRendererSystem(shapeRenderer);

        PowerSystem powerSystem = new PowerSystem();
        PowerRendererSystem powerRendererSystem = new PowerRendererSystem(batch, atlas);

        AgentRenderingSystem agentRenderingSystem=new AgentRenderingSystem(shapeRenderer);
        AgentSystem agentSystem=new AgentSystem();

        engine.addSystem(agentSystem);
        engine.addSystem(agentRenderingSystem);

        engine.addSystem(portSystem);

        engine.addSystem(wirePortSystem);
        engine.addSystem(wireSystem);
        engine.addSystem(wireRendererSystem);

        engine.addSystem(powerSystem);
        engine.addSystem(powerRendererSystem);

        changer.addListener(agentSystem);
        changer.addListener(agentRenderingSystem);

        changer.addListener(portSystem);

        changer.addListener(wirePortSystem);
        changer.addListener(wireSystem);
        changer.addListener(wireRendererSystem);

        changer.addListener(powerSystem);
        changer.addListener(powerRendererSystem);

    }
}
