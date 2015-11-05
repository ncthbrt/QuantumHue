package co.za.cuthbert.three.ui;

import co.za.cuthbert.three.*;
import co.za.cuthbert.three.systems.SystemFactory;
import co.za.cuthbert.three.ui.actions.NewLevelAction;
import co.za.cuthbert.three.ui.actions.PlayPauseAction;
import co.za.cuthbert.three.ui.actions.ShowDialogAction;
import co.za.cuthbert.three.ui.tools.BrushTool;
import co.za.cuthbert.three.ui.tools.PanTool;
import co.za.cuthbert.three.ui.tools.ToggleTool;
import co.za.cuthbert.three.value_objects.DiscreteColour;
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
public class LevelEditor implements Screen {
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Game game;
    private ColourSelector colourSelector;
    private ToolChooser chooser;
    private ButtonGroup group;
    private InputMultiplexer multiplexer;
    private final PooledEngine engine;
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

    private LevelChanger changer;

    public LevelEditor(Game game) {
        if(Config.DEBUG) {
            fps = new BitmapFont();
        }
        engine = new PooledEngine();
        changer = new LevelChanger();
        this.batch = new SpriteBatch();
        this.atlas = Iteration3Main.textureAtlas();
        SystemFactory.addToEngine(engine, changer, batch, atlas);

        this.game = game;
        group = new ButtonGroup(new Vector2(1910, 0), ButtonGroup.Direction.UP, ButtonGroup.Anchor.BOTTOM_RIGHT);
        multiplexer = new InputMultiplexer();
        chooser = new ToolChooser(multiplexer, atlas.createSprite("border_top"), atlas.createSprite("border_bottom"));
        chooser.addTool(new BrushTool(this, EntityType.WIRE), atlas.createSprite("tool_wire"));
        chooser.addTool(new ToggleTool(this, EntityType.POWER_SOURCE), atlas.createSprite("tool_power"));
        chooser.addTool(new BrushTool(this,EntityType.FILTER), atlas.createSprite("tool_agent"));
        chooser.addTool(new PanTool(this), atlas.createSprite("tool_ground"));
        chooser.addTool(new PanTool(this), atlas.createSprite("tool_pan"));


        GestureDetector detector = new GestureDetector(chooser);

        colourSelector = new ColourSelector();
        GestureDetector detector2 = new GestureDetector(colourSelector);

        NewLevelAction newLevelAction = new NewLevelAction(this, engine);
        dialog = new ConfirmDialog(multiplexer, atlas.createSprite("diagram_new_consequences"), newLevelAction, null);


        Sprite start=atlas.createSprite("icon_play");
        Sprite pause=atlas.createSprite("icon_pause");


        NinePatchButton.initialise(atlas,0, Config.BUTTON_DOWN_OFFSET);
        NinePatchButton playButton=new NinePatchButton("play", Button.Trigger.TRAILING_EDGE,start);
        PlayPauseAction playPauseAction=new PlayPauseAction(start,pause,playButton);
        playButton.addAction(playPauseAction);
        changer.addListener(playPauseAction);

        group.addButton(playButton);
        group.addButton("load", atlas.createSprite("icon_load"));
        group.addButton("save", atlas.createSprite("icon_save"));
        group.addButton("new", atlas.createSprite("icon_new"), new ShowDialogAction(dialog));


        multiplexer.addProcessor(detector);
        multiplexer.addProcessor(detector2);
        multiplexer.addProcessor(group);
        newLevelAction.actionPerformed("Ok");

    }

    private ConfirmDialog dialog;
    BitmapFont fps;
    @Override
    public void render(float delta) {
        currentLevel.update(delta);
        batch.begin();
        group.render(batch);
        dialog.render(batch, delta);
        chooser.render(batch, delta);
        colourSelector.render(batch, delta);
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
}
