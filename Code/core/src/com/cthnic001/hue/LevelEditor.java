package com.cthnic001.hue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class LevelEditor implements Screen {
    private Game game;
    public LevelEditor(Game game){
        this.game=game;
        stage=new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Gdx.input.setInputProcessor(stage);

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
        newDialog.add(newLevelCancelButton,newLevelOkButton);
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
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
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
