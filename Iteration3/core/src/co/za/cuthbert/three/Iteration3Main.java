package co.za.cuthbert.three;

import co.za.cuthbert.three.screens.LevelEditor;
import co.za.cuthbert.three.screens.TitleScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Iteration3Main extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        Gdx.gl.glEnable(GL20.GL_ALIASED_LINE_WIDTH_RANGE);
        Gdx.gl20.glLineWidth(2);
//        LevelEditor editor=new LevelEditor(this);
//        setScreen(editor);
        setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
