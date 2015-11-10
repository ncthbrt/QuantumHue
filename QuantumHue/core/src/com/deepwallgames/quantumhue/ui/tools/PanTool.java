package com.deepwallgames.quantumhue.ui.tools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deepwallgames.quantumhue.ui.Button;
import com.deepwallgames.quantumhue.ui.LevelEditor;

/**
 * Copyright Nick Cuthbert, 2014
 * <p/>
 * A dummy tool which allows the pan and zoom gestures to pass through to the level.
 */
public class PanTool extends Tool{
    public PanTool(LevelEditor editor) {
        super(editor);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(button!= Input.Buttons.RIGHT){
            panning=true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return editor.currentLevel().tap(x, y, count, button);
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    private boolean panning=false;
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return panning && editor.currentLevel().pan(x, y, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(panning && button!= Input.Buttons.RIGHT) {
            panning=false;
            return editor.currentLevel().panStop(x, y, pointer, button);
        }
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return editor.currentLevel().zoom(initialDistance, distance);
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }



}
