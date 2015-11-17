package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.deepwallgames.quantumhue.ImmediateModeShader30;
import com.deepwallgames.quantumhue.LevelChanger;
import com.deepwallgames.quantumhue.RenderLayers;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class SystemFactory {
    public static void addToEngine(Engine engine, LevelChanger changer, SpriteBatch batch, TextureAtlas atlas, RenderLayers layers) {


        ShaderProgram.pedantic = false;
        String vertShader= Gdx.files.internal("defaultVertex.glsl").readString();

        String fragShader= Gdx.files.internal("blurShader.glsl").readString();
        ShaderProgram blurShader= new ShaderProgram(vertShader, fragShader);

//        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        if(blurShader.isCompiled()){
            Gdx.app.debug("Success","Blur shader compiled!");
            Gdx.app.debug("Success",blurShader.getLog());
        }else{
            Gdx.app.log("Exception","Blur shader ignored: "+blurShader.getLog());
        }


        ShapeRenderer shapeRenderer = new ShapeRenderer(5000,ImmediateModeShader30.createDefaultShader(false,true,0));

        PortSystem portSystem = new PortSystem();

        WirePortSystem wirePortSystem = new WirePortSystem();
        WireSystem wireSystem = new WireSystem();


        EarthPotentialSystem earthPotentialSystem=new EarthPotentialSystem();
        changer.addListener(earthPotentialSystem);
        engine.addSystem(earthPotentialSystem);

        BufferClearSystem bufferClearSystem=new BufferClearSystem(layers);
        engine.addSystem(bufferClearSystem);


        WireRendererSystem wireRendererSystem;
        SwitchablePartRenderer switchablePartRenderer;
//        if(Gdx.app.getType()!= Application.ApplicationType.WebGL) {
            GlowRenderer glowRenderer = new GlowRenderer(batch, blurShader, layers);
            engine.addSystem(glowRenderer);
            changer.addListener(glowRenderer);
            wireRendererSystem = new WireRendererSystem(shapeRenderer,layers);
            switchablePartRenderer= new SwitchablePartRenderer(batch, atlas, layers);



        PowerSystem powerSystem = new PowerSystem();


        engine.addSystem(portSystem);

        engine.addSystem(wirePortSystem);
        engine.addSystem(wireSystem);
        engine.addSystem(wireRendererSystem);

        engine.addSystem(powerSystem);
        engine.addSystem(switchablePartRenderer);


        changer.addListener(portSystem);

        changer.addListener(wirePortSystem);
        changer.addListener(wireSystem);
        changer.addListener(wireRendererSystem);

        changer.addListener(powerSystem);
        changer.addListener(switchablePartRenderer);
    }
}
