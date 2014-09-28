package co.za.cuthbert.three.level_editor.actions;

import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.level_editor.LevelEditor;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class NewLevelAction extends ButtonAction {
    private final LevelEditor editor;
    private final PooledEngine engine;
    private static final int defaultWidth = 30;

    public NewLevelAction(LevelEditor editor, PooledEngine engine) {
        this.editor = editor;
        this.engine = engine;
    }

    @Override
    public void actionPerformed(String command) {
        editor.currentLevel(new Level(engine, defaultWidth, (int) (defaultWidth * Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth())));
    }
}
