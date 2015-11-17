package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Logger;
import com.deepwallgames.quantumhue.Iteration3Main;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.RenderLayers;

import static com.badlogic.gdx.Gdx.*;

/**
 * Created by nick_000 on 08/11/2015.
 */
public class GlowRenderer extends EntitySystem implements LevelChangeListener {

    public float blur=1.4f;

    private final SpriteBatch batcher;
    private final ShaderProgram blurShader;
    private final OrthographicCamera bufferCamera;
    private Level level;
    private final TextureRegion frameRegion;
    private RenderLayers layers;
    Logger logger;
    public GlowRenderer(SpriteBatch batcher, ShaderProgram blurShader, RenderLayers layers){
//        Gdx.app.setLogLevel(Logger.DEBUG);
        this.layers=layers;
        priority=Integer.MAX_VALUE;
        this.batcher=batcher;
        this.blurShader=blurShader;
        logger=new Logger("Logging");

        logger.setLevel(Logger.DEBUG);
        logger.debug(blurShader.getLog());

        bufferCamera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bufferCamera.setToOrtho(false);
        frameRegion=new TextureRegion(layers.get(RenderLayers.LevelEditorLayer.UTILITY).getColorBufferTexture());
        frameRegion.flip(false, true);
    }

    @Override
    public void update(float deltaTime) {

        if(level!=null) {
            //bufferCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            layers.get(RenderLayers.LevelEditorLayer.UTILITY).getColorBufferTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).getColorBufferTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

            batcher.begin();
            {
                batcher.setProjectionMatrix(bufferCamera.combined);

                layers.get(RenderLayers.LevelEditorLayer.UTILITY).begin();
                batcher.setShader(blurShader);
                {
                    gl.glClearColor(0f, 0f,0f, 0f);
                    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


                    blurShader.setUniformf("dir", 1f, 0f);
                    blurShader.setUniformf("blur", blur / Gdx.graphics.getWidth() / level.zoom());
                    frameRegion.setTexture(layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).getColorBufferTexture());
                    batcher.draw(frameRegion, 0,0);
                    batcher.flush();
                }
                layers.get(RenderLayers.LevelEditorLayer.UTILITY).end();
                layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).begin();
                {
                    gl.glClearColor(0f, 0f, 0f, 0f);
                    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    gl.glEnable(GL20.GL_BLEND);
                    gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    blurShader.setUniformf("dir", 0f, 1f);
                    blurShader.setUniformf("blur", blur / Gdx.graphics.getWidth() / level.zoom());
                    frameRegion.setTexture(layers.get(RenderLayers.LevelEditorLayer.UTILITY).getColorBufferTexture());
                    batcher.draw(frameRegion, 0, 0);
                    batcher.flush();
                }
                layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).end();
                batcher.setShader(null);

                Gdx.gl.glEnable(GL20.GL_BLEND);
                gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                batcher.setColor(1, 1, 1, 1);
                batcher.setProjectionMatrix(bufferCamera.combined);

                frameRegion.setTexture(layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).getColorBufferTexture());

                batcher.draw(frameRegion,0, 0);
                batcher.setColor(1, 1, 1, 1);
                batcher.setProjectionMatrix(bufferCamera.combined);
                frameRegion.setTexture(layers.get(RenderLayers.LevelEditorLayer.BASE).getColorBufferTexture());
                batcher.draw(frameRegion,0,0);
            }
            batcher.end();
        }
    }

    @Override
    public void level(Level level) {
        this.level=level;
    }
}
