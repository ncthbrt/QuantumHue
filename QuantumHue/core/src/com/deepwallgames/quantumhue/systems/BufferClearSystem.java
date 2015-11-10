package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.deepwallgames.quantumhue.RenderLayers;

/**
 * Created by nick_000 on 08/11/2015.
 */
public class BufferClearSystem extends EntitySystem {

    private final RenderLayers layers;
    public BufferClearSystem(RenderLayers layers){
        priority=0;
        this.layers=layers;
    }
    @Override
    public void update(float deltaTime) {
        for (FrameBuffer buffer:layers) {
            buffer.begin();
            {
                Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.gl.glEnable(GL20.GL_BLEND);
            }
            buffer.end();
        }
    }
}
