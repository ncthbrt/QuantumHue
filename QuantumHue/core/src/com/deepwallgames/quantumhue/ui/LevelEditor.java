package com.deepwallgames.quantumhue.ui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.Iteration3Main;
import com.deepwallgames.quantumhue.RenderLayers;
import com.deepwallgames.quantumhue.systems.SystemFactory;
import com.deepwallgames.quantumhue.ui.actions.PlayPauseAction;
import com.deepwallgames.quantumhue.ui.tools.BrushTool;
import com.deepwallgames.quantumhue.ui.tools.PanTool;
import com.deepwallgames.quantumhue.ui.tools.ToggleTool;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.LevelChanger;
import com.deepwallgames.quantumhue.ui.actions.NewLevelAction;
import com.deepwallgames.quantumhue.ui.actions.ShowDialogAction;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class LevelEditor implements Screen,GestureDetector.GestureListener,InputProcessor {
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Game game;
    private ColourSelector colourSelector;
    private RotationSelector rotationSelector;
    private ToolChooser chooser;
    private ButtonGroup group;
    private InputMultiplexer multiplexer;
    private final Engine engine;
    private Level currentLevel;

    public Level currentLevel() {
        return currentLevel;
    }

    public DiscreteColour colour() {
        return colourSelector.currentColour();
    }

    public void currentLevel(Level level) {
        if (currentLevel != null) {
            this.currentLevel.dispose();
        }
        this.currentLevel = level;
        changer.currentLevel(currentLevel);
        engine.addEntityListener(currentLevel);
    }

    private RenderLayers layers;
    private LevelChanger changer;

    public LevelEditor(Game game,SpriteBatch batch) {
        if(Config.DEBUG) {
            fps = new BitmapFont();
        }
        if(Gdx.app.getType()== Application.ApplicationType.WebGL){
            engine=new Engine();
        }else{
            engine = new PooledEngine();
        }

        changer = new LevelChanger();
        this.batch = batch;
        this.atlas = Iteration3Main.textureAtlas();
        layers=new RenderLayers();
        SystemFactory.addToEngine(engine, changer, batch, atlas,layers);

        this.game = game;
        group = new ButtonGroup(new Vector2(1910, 0), ButtonGroup.Direction.UP, ButtonGroup.Anchor.BOTTOM_RIGHT);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(Config.LEVEL_EDITOR_DEPTH, new GestureDetector(this));
        multiplexer.addProcessor(Config.LEVEL_EDITOR_DEPTH2,this);
        chooser = new ToolChooser(multiplexer, atlas.createSprite("border_top"), atlas.createSprite("border_bottom"),this);
        chooser.addTool(new BrushTool(this, EntityType.WIRE), atlas.createSprite("tool_wire"));
        chooser.addTool(new ToggleTool(this, EntityType.POWER_SOURCE), atlas.createSprite("tool_power"));
        //chooser.addTool(new BrushTool(this, EntityType.FILTER), atlas.createSprite("tool_agent"));
        chooser.addTool(new ToggleTool(this,EntityType.GROUND), atlas.createSprite("tool_ground"));
        if(Gdx.app.getType()!= Application.ApplicationType.Desktop) {
            chooser.addTool(new PanTool(this), atlas.createSprite("tool_pan"));
        }


        GestureDetector detector = new GestureDetector(chooser);

        colourSelector = new ColourSelector();
        GestureDetector detector2 = new GestureDetector(colourSelector);

        rotationSelector=new RotationSelector();
        GestureDetector detector3 = new GestureDetector(rotationSelector);
        NewLevelAction newLevelAction = new NewLevelAction(this, engine);

        dialog = new ConfirmDialog(multiplexer, atlas.createSprite("diagram_new_consequences"), newLevelAction, null);


        Sprite start=atlas.createSprite("icon_play");
        Sprite pause=atlas.createSprite("icon_pause");


        NinePatchButton.initialise(atlas, 0, Config.BUTTON_DOWN_OFFSET);
        NinePatchButton playButton=new NinePatchButton("play", Button.Trigger.TRAILING_EDGE,start);
        PlayPauseAction playPauseAction=new PlayPauseAction(start,pause,playButton);
        playButton.addAction(playPauseAction);
        changer.addListener(playPauseAction);

        group.addButton(playButton);
        group.addButton("load", atlas.createSprite("icon_load"));
        group.addButton("save", atlas.createSprite("icon_save"));
        group.addButton("new", atlas.createSprite("icon_new"), new ShowDialogAction(dialog));



        multiplexer.addProcessor(detector3);
        multiplexer.addProcessor(detector);
        multiplexer.addProcessor(detector2);

        multiplexer.addProcessor(group);
        newLevelAction.actionPerformed("Ok");
    }

    private ConfirmDialog dialog;
    BitmapFont fps;
    @Override
    public void render(float delta) {
        //Gdx.input.setCursorCatched(true);


        currentLevel.update(delta);
        batch.begin();
        group.render(batch);
        dialog.render(batch, delta);
        chooser.render(batch, delta);
        colourSelector.render(batch, delta);
        rotationSelector.render(batch, delta);
        if(Config.DEBUG) {
            OrthographicCamera orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            orthographicCamera.position.set(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,0);
            orthographicCamera.update();
            batch.setProjectionMatrix(orthographicCamera.combined);
            fps.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()),5, Gdx.graphics.getHeight()-5);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Resized");
        layers.resize(width,height);
    }

    @Override
    public void show() {
        chooser.init();
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void hide() {
        chooser.dispose();
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(button==Input.Buttons.RIGHT){
            rightPressed=true;
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return button == Input.Buttons.RIGHT;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return button == Input.Buttons.RIGHT;
    }

    private boolean rightPressed=false;

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return rightPressed && currentLevel().pan(x, y, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(button==Input.Buttons.RIGHT) {
            rightPressed=false;
            return currentLevel().panStop(x, y, pointer, button);
        }
        return false;
    }


    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode==Input.Keys.ESCAPE){
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button== Input.Buttons.RIGHT){
            rightPressed=true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button== Input.Buttons.RIGHT){
            rightPressed=false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return rightPressed;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        this.currentLevel().zoom(0,100*amount);
        return true;
    }
}
