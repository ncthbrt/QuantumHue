package co.za.cuthbert.three.level_editor;


import co.za.cuthbert.three.Iteration3Main;
import co.za.cuthbert.three.TileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class LevelEditor implements Screen {
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Game game;
    private ColourSelector colourSelector;
    private ToolChooser chooser;
    private ButtonGroup group;


    public LevelEditor(Game game) {
        this.atlas= Iteration3Main.textureAtlas();
        this.batch=new SpriteBatch();
        this.game=game;
        group=new ButtonGroup(new Vector2(1910,0), ButtonGroup.Direction.UP, ButtonGroup.Anchor.BOTTOM_RIGHT);

        chooser=new ToolChooser(atlas.createSprite("border_top"),atlas.createSprite("border_bottom"));
        chooser.addTool(TileType.WIRE,atlas.createSprite("tool_wire"));
        chooser.addTool(TileType.WIRE,atlas.createSprite("tool_power"));
        GestureDetector detector=new GestureDetector(chooser);

        colourSelector=new ColourSelector();
        GestureDetector detector2=new GestureDetector(colourSelector);
        InputMultiplexer multiplexer=new InputMultiplexer();

        Stage stage=new Stage();
        stage.setViewport(new FillViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));

        group.addButton("load",atlas.createSprite("icon_load"),new ArrayList<ButtonAction>());
        group.addButton("save",atlas.createSprite("icon_save"),new ArrayList<ButtonAction>());
        group.addButton("new",atlas.createSprite("icon_new"),new ArrayList<ButtonAction>());

        dialog=new ConfirmDialog(multiplexer,atlas.createSprite("diagram_new_consequences"), new ButtonAction());

        multiplexer.addProcessor(detector);
        multiplexer.addProcessor(detector2);

        //Show dialog last, as the show method removes the processors from the multiplexer
        dialog.show();

        Gdx.input.setInputProcessor(multiplexer);
    }
    private ConfirmDialog dialog;

    @Override
    public void render(float delta) {
        batch.begin();
        chooser.render(batch,delta);
        colourSelector.render(batch,delta);
        group.render(batch);
        dialog.render(batch,delta);
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
