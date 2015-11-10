package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.deepwallgames.quantumhue.LevelChanger;
import com.deepwallgames.quantumhue.RenderLayers;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class SystemFactory {
    public static void addToEngine(PooledEngine engine, LevelChanger changer, SpriteBatch batch, TextureAtlas atlas, RenderLayers layers) {


        ShaderProgram.pedantic = false;
        String vertShader= Gdx.files.internal("defaultVertex.glsl").readString();

        String fragShader= Gdx.files.internal("blurShader.glsl").readString();
        ShaderProgram blurShader= new ShaderProgram(vertShader, fragShader);


        if(blurShader.isCompiled()){
            System.out.println("Blur shader Correctly compiled");
        }

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        PortSystem portSystem = new PortSystem();

        WirePortSystem wirePortSystem = new WirePortSystem();
        WireSystem wireSystem = new WireSystem();


        EarthPotentialSystem earthPotentialSystem=new EarthPotentialSystem();
        changer.addListener(earthPotentialSystem);
        engine.addSystem(earthPotentialSystem);

        BufferClearSystem bufferClearSystem=new BufferClearSystem(layers);
        engine.addSystem(bufferClearSystem);

        GlowRenderer glowRenderer =new GlowRenderer(batch,blurShader,layers);
        engine.addSystem(glowRenderer);
        WireRendererSystem wireRendererSystem = new WireRendererSystem(shapeRenderer,layers);

        PowerSystem powerSystem = new PowerSystem();
        SwitchablePartRenderer switchablePartRenderer = new SwitchablePartRenderer(batch, atlas, layers);

        engine.addSystem(portSystem);

        engine.addSystem(wirePortSystem);
        engine.addSystem(wireSystem);
        engine.addSystem(wireRendererSystem);

        engine.addSystem(powerSystem);
        engine.addSystem(switchablePartRenderer);

        changer.addListener(glowRenderer);
        changer.addListener(portSystem);

        changer.addListener(wirePortSystem);
        changer.addListener(wireSystem);
        changer.addListener(wireRendererSystem);

        changer.addListener(powerSystem);
        changer.addListener(switchablePartRenderer);
    }
}
