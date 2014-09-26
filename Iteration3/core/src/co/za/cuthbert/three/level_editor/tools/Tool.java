package co.za.cuthbert.three.level_editor.tools;

import co.za.cuthbert.three.level_editor.LevelEditor;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Tool implements GestureDetector.GestureListener {
    protected LevelEditor editor;

    public Tool(LevelEditor editor) {
        this.editor = editor;
    }


}
