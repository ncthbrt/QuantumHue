package co.za.cuthbert.three;

import co.za.cuthbert.three.leveleditor.LevelEditor;
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
		img = new Texture("badlogic.jpg");
        Gdx.gl.glEnable(GL20.GL_ALIASED_LINE_WIDTH_RANGE);
        LevelEditor editor=new LevelEditor(this);
        setScreen(editor);
	}

	@Override
	public void render () {
        Gdx.gl20.glLineWidth(5);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
