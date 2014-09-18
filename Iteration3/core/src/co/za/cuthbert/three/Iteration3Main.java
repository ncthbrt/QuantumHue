package co.za.cuthbert.three;

import co.za.cuthbert.three.level_editor.LevelEditor;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.HSVColour;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Iteration3Main extends Game {
	SpriteBatch batch;
	TextureAtlas atlas;
    private static final float colourCycleTime=60f;
    private static HSVColour currentColour=new HSVColour(0,0.81f,0.7f,1f);
    public static Colour currentColour(){
        return currentColour.toRGB();
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
        Gdx.gl.glEnable(GL20.GL_ALIASED_LINE_WIDTH_RANGE);
        Gdx.gl20.glLineWidth(2);
        atlas=new TextureAtlas(Gdx.files.internal("global.atlas"));
       // setScreen(new TitleScreen(this,atlas));
        setScreen(new LevelEditor(this,atlas));
	}


	@Override
	public void render () {
        float nextHue=currentColour.hue()+Gdx.graphics.getDeltaTime()/colourCycleTime;
        nextHue=nextHue>1?0:nextHue;
        currentColour.hue(nextHue);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
