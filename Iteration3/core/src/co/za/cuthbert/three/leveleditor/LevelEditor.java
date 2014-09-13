package co.za.cuthbert.three.leveleditor;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.listeners.Level;
import co.za.cuthbert.three.systems.WireRendererSystem;
import co.za.cuthbert.three.systems.WireSystem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class LevelEditor implements Screen {
    private Game game;
    private PooledEngine engine;
    private Level currentLevel=null;
    private final OrthographicCamera levelCamera;
    private final OrthographicCamera guiCamera;
    private final LevelEditorInputHandler inputHandler;
    private final ShapeRenderer shapeRenderer=new ShapeRenderer();
    private final WireRendererSystem wireRendererSystem;
    private float aspectRatio=1;
    public LevelEditor(Game game){
        this.game=game;
        stage=new Stage();
        levelCamera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        levelCamera.position.set(levelCamera.viewportWidth/2,levelCamera.viewportHeight/2,0);
        guiCamera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        guiCamera.position.set(guiCamera.viewportWidth/2,guiCamera.viewportHeight/2,0);
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(new ScreenViewport(guiCamera));
        engine=new PooledEngine();
        wireRendererSystem=new WireRendererSystem(shapeRenderer,levelCamera);
        engine.addSystem(wireRendererSystem);
        inputHandler=new LevelEditorInputHandler(levelCamera,engine);
        stage.addListener(inputHandler);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        final WireSystem wireSystem=new WireSystem();

        engine.addSystem(wireSystem);

        //New Level Dialog Box
        newLevelWidthField =new TextField("width",skin);
        newLevelHeightField =new TextField("height",skin);
        newLevelOkButton=new TextButton("Ok", skin);
        newLevelOkButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    int width=Integer.parseInt(newLevelWidthField.getText());
                    int height=Integer.parseInt(newLevelHeightField.getText());
                    newDialog.hide();
                    engine.removeEntityListener(currentLevel);
                    engine.removeAllEntities();
                    currentLevel=new Level(width,height,1,1f);//TODO add parameter to increase depth
                    wireSystem.setLevel(currentLevel);
                    inputHandler.setLevel(currentLevel);

                    if(width>height) {
                        levelCamera.viewportWidth = Config.TILE_SIZE * width;
                        levelCamera.viewportHeight =Config.TILE_SIZE * width*aspectRatio;
                    }else{
                        levelCamera.viewportWidth = Config.TILE_SIZE * height;
                        levelCamera.viewportHeight = Config.TILE_SIZE * height*aspectRatio;
                    }
                    levelCamera.position.set(levelCamera.viewportWidth/2f,levelCamera.viewportHeight/2f,0);

                    ScreenViewport screenViewport=new ScreenViewport(levelCamera);
                    levelCamera.update();
                    wireRendererSystem.setLevel(currentLevel);
                    System.out.println("Creating new level");
                    engine.addEntityListener(currentLevel);
                }catch (Exception e){
                    newDialog.add(new Label("Error enter width, height",skin));
                }
            }
        });

        newLevelCancelButton=new TextButton("Cancel", skin);
        newLevelCancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newDialog.hide();
            }
        });

        newDialog=new Dialog("New level",skin);
        newDialog.setResizable(true);
        newDialog.text("Enter map width and height");
        newDialog.row();
        newDialog.add(newLevelWidthField);
        newDialog.row();
        newDialog.add(newLevelHeightField);
        newDialog.row();
        newDialog.add(newLevelCancelButton, newLevelOkButton);
        newDialog.key(Input.Keys.ESCAPE, false);


        //End New Level Dialog Box



        //Level Editor loader gui
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        newButton=new  TextButton("new",skin);
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newDialog.show(stage);
            }
        });

        saveButton =new TextButton("save",skin);
        loadButton=new  TextButton("load",skin);
        startButton=new TextButton("start",skin);


        //End Level Editor loader gui

        table.align(Align.left|Align.bottom);
        table.add(newButton);
        table.add(saveButton);
        table.add(loadButton);
        table.add(startButton);


    }


   //REGION UI ELEMENTS
   private Stage stage;
   private Skin skin;

    private Dialog newDialog;
        private TextButton newLevelOkButton;
        private TextButton newLevelCancelButton;
        private TextField newLevelWidthField;
        private TextField newLevelHeightField;

   private TextButton saveButton;
   private TextButton loadButton;
   private TextButton newButton;
   private TextButton startButton;
    //REGION UI ELEMENTS
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(delta);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
        aspectRatio=height/((float)width);
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
        stage.dispose();
    }
}
