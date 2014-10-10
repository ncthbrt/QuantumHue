package co.za.cuthbert.three.ui.tools;

import co.za.cuthbert.three.ui.LevelEditor;
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
