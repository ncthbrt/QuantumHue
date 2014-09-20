package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.listeners.Level;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Tool{
    private final PooledEngine engine;
    public Tool(PooledEngine engine){
        this.engine=engine;
    }

    public abstract void tap(int x, int y);
    public abstract void longPress(int x, int y);
    public abstract void swipe(int x, int y);
    public abstract void swipeStop(int x, int y);
}
