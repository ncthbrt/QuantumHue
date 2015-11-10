package com.deepwallgames.quantumhue.ui.tools;

import com.deepwallgames.quantumhue.ui.LevelEditor;
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
