package com.cthnic001.hue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class QuantumHue extends Game {
	SpriteBatch batch;
	Texture img;
    private boolean debug=false;

    public QuantumHue(){

    }

    public QuantumHue(boolean debug){
        this.debug=debug;
    }


	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        if(debug){
            setScreen(new LevelEditor(this));
        }
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();

//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
}
