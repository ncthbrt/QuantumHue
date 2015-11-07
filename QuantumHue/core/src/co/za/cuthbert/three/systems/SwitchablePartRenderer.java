package co.za.cuthbert.three.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;

/**
 * Created by nick_000 on 07/11/2015.
 */
public class SwitchablePartRenderer  extends com.badlogic.ashley.core.EntitySystem implements LevelChangeListener {
    Level level;
    ShaderProgram blurShader;
    SpriteBatch batcher;
    public SwitchablePartRenderer(ShaderProgram blurShader, SpriteBatch batcher){
        super(10);
        this.blurShader=blurShader;
        this.batcher=batcher;
    }
    @Override
    public void level(Level level) {
        this.level = level;
    }



}
