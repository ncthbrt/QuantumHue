package com.deepwallgames.quantumhue.ui;

import com.badlogic.gdx.Input;
import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.Iteration3Main;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.ui.tools.Tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ToolChooser implements GestureDetector.GestureListener{


    public ToolChooser(InputMultiplexer multiplexer, Sprite borderTop, Sprite borderBottom, LevelEditor editor) {
        this.multiplexer = multiplexer;
        this.editor=editor;

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        centreCamera();
        tools = new ArrayList<ToolChooserWidget>();
        this.borderBottom = borderBottom;
        this.borderTop = borderTop;
        toolWidth = borderBottom.getRegionWidth();
        toolHeight += borderBottom.getRegionHeight();
        toolHeight += borderTop.getRegionHeight();
        toolChooserGestureDetector = new GestureDetector(this);

    }

    public void show(){

    }

    public void hide(){

    }

    private boolean hidden=false;


    public void init() {
        multiplexer.addProcessor(Config.TOOL_CHOOSER_DEPTH, toolChooserGestureDetector);
    }

    public void dispose() {
        multiplexer.removeProcessor(toolChooserGestureDetector);
    }

    private final ArrayList<ToolChooserWidget> tools;
    private LevelEditor editor;
    private OrthographicCamera camera;
    private final InputMultiplexer multiplexer;
    private GestureDetector toolGestureDetector;
    private final GestureDetector toolChooserGestureDetector;
    private ToolChooserWidget currentTool = null;
    private float toolHeight = 0;
    private float toolWidth = 0;
    private final float padding = 5;
    private final Sprite borderTop;
    private final Sprite borderBottom;
    private final float hideTime=0.3f;
    private OrthographicCamera camera() {
        return camera;
    }

    private void centreCamera() {
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    public void addTool(Tool tool, Sprite icon) {
        toolHeight += icon.getRegionHeight() - padding;
        toolWidth = icon.getRegionWidth();
        if (toolHeight > camera.viewportHeight) {
            float aspectRatio = camera.viewportHeight / camera.viewportWidth;
            camera.viewportHeight = toolHeight;
            camera.viewportWidth = camera.viewportHeight / aspectRatio;
            centreCamera();
        }
        float x1 = -(toolWidth - borderTop.getRegionWidth());
        float x2 = 0;
        tools.add(new ToolChooserWidget(tool, icon, x1, x2));
    }

    public void render(SpriteBatch batch, float delta) {
        this.camera.setToOrtho(false,1920, 1920*(Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()));

        batch.setProjectionMatrix(camera.combined);
        float currentHeight = (camera.viewportHeight + toolHeight) / 2f - borderTop.getRegionHeight();
        Colour colour = Iteration3Main.currentColour();
        borderBottom.setColor(colour.red() / 255f, colour.green() / 255f, colour.blue() / 255f, colour.alpha() / 255f);
        borderTop.setColor(colour.red() / 255f, colour.green() / 255f, colour.blue() / 255f, 1f);
        borderTop.setPosition(0, currentHeight);
        borderTop.draw(batch);
        currentHeight -= padding;
        for (ToolChooserWidget widget : tools) {
            currentHeight -= (widget.icon().getRegionHeight() - padding);
            widget.render(batch, delta, currentHeight);
        }
        borderBottom.setPosition(0, currentHeight + padding - borderBottom.getRegionHeight());
        borderBottom.draw(batch);
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 world = camera.unproject(new Vector3(x, y, 0));
        if (world.x > toolWidth) {
            return false;
        } else {
            float currentHeight = (camera.viewportHeight - toolHeight) / 2f + borderBottom.getRegionHeight();
            if (tools.size() > 0) {
                ToolChooserWidget widget = tools.get(0);
                currentHeight += widget.icon().getHeight();
            }
            for (int i = tools.size() - 1; i >= 0; --i) {
                ToolChooserWidget widget = tools.get(i);
                if (widget.inWidget(world.x, world.y, currentHeight)) {
                    if (currentTool != null) {
                        currentTool.selected(false);
                        multiplexer.removeProcessor(toolGestureDetector);
                    }

                    if (currentTool == widget) {
                        currentTool = null;
                    } else {
                        currentTool = widget;
                        currentTool.selected(true);
                        toolGestureDetector = new GestureDetector(currentTool.tool());
                        multiplexer.addProcessor(toolGestureDetector);

                    }
                    return true;
                }
                currentHeight += widget.icon().getHeight() - padding;
            }
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    //TODO Add ability to hide ENTIRE chooser to increase real estate
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
