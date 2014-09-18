package co.za.cuthbert.three.level_editor;


import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.HSVColour;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class LevelEditor implements Screen {
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Game game;
    private OrthographicCamera uiCamera;
    private ColourSelector colourSelector;
    private Sprite topBorder;
    private Sprite bottomBorder;
    private ToolChooser chooser;


    public LevelEditor(Game game, TextureAtlas textureAtlas) {
        this.atlas=textureAtlas;
        this.batch=new SpriteBatch();
        this.game=game;
        chooser=new ToolChooser(atlas.createSprite("border_top"),atlas.createSprite("border_bottom"));
        chooser.addTool(TileType.WIRE,atlas.createSprite("tool_wire"));
        chooser.addTool(TileType.WIRE,atlas.createSprite("tool_power"));
        GestureDetector detector=new GestureDetector(chooser);
        Gdx.input.setInputProcessor(detector);

        colourSelector=new ColourSelector();
        GestureDetector detector2=new GestureDetector(colourSelector);
        InputMultiplexer multiplexer=new InputMultiplexer();
        multiplexer.addProcessor(detector);
        multiplexer.addProcessor(detector2);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        chooser.render(batch,delta);
        colourSelector.render(batch,delta);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
