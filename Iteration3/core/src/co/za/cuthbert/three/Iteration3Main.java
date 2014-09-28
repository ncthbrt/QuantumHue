package co.za.cuthbert.three;


import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.HSVColour;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Iteration3Main extends Game {
    SpriteBatch batch;
    private static TextureAtlas atlas;
    private static final float colourCycleTime = 30f;
    private static HSVColour currentColour = new HSVColour(0, 0.81f, 0.7f, 1f);

    public static Colour currentColour() {
        return currentColour.toRGB();
    }

    private static TitleScreen titleScreen;
    private static Iteration3Main main;

    public static TextureAtlas textureAtlas() {
        return atlas;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.gl20.glLineWidth(5);
        atlas = new TextureAtlas(Gdx.files.internal("global.atlas"));
        titleScreen = new TitleScreen(this, atlas);
        main = this;
        setScreen(titleScreen);
    }


    @Override
    public void render() {
        float nextHue = currentColour.hue() + Gdx.graphics.getDeltaTime() / colourCycleTime;
        nextHue = nextHue > 1 ? 0 : nextHue;
        currentColour.hue(nextHue);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }
}
