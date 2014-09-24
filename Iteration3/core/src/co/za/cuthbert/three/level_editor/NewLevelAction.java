package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class NewLevelAction extends ButtonAction {
    private final LevelEditor editor;
    private final PooledEngine engine;
    private static final int defaultWidth=30;
    public NewLevelAction(LevelEditor editor, PooledEngine engine){
        this.editor=editor;
        this.engine=engine;
    }
    @Override
    public void actionPerformed(String command) {
        if(command.equals("Ok")) {
            editor.currentLevel(new Level(engine, defaultWidth, (int) (defaultWidth * Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
        }
    }
}
