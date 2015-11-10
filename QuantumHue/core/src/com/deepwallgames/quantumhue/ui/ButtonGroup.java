package com.deepwallgames.quantumhue.ui;

import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.Iteration3Main;
import com.deepwallgames.quantumhue.ui.actions.ButtonAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ButtonGroup extends InputAdapter {

    private OrthographicCamera camera;
    public ArrayList<Button> buttons;

    private static Sprite buttonUp = null;
    private static Sprite buttonDown = null;
    private final Vector2 anchorPoint;
    private final Direction direction;
    private final Anchor anchor;

    public Vector2 anchorPoint() {
        return anchorPoint;
    }

    public void anchorPoint(float x, float y) {
        this.anchorPoint.set(x, y);
    }

    public ButtonGroup(Vector2 anchorPoint, Direction direction, Anchor anchor) {


        this.direction = direction;
        this.anchor = anchor;

        buttons = new ArrayList<Button>();
        camera = new OrthographicCamera(1920, 1920 * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));
        this.anchorPoint = anchorPoint;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        if (buttonDown == null) {
            buttonDown = Iteration3Main.textureAtlas().createSprite("button_default_down");
            buttonUp = Iteration3Main.textureAtlas().createSprite("button_default_up");
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int buttonNum) {
        buttonPressed = false;
        if(buttons.size()>0) {
            float xCorner = anchorPoint.x;
            float yCorner = anchorPoint.y;
            for (Button button : buttons) {
                Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));
                if (button.inWidget(world.x, world.y, xCorner - anchor.xFromBottomLeft * (button.width()), yCorner-anchor.yFromBottomLeft * (button.height()))) {
                    button.pressed(true);
                    buttonPressed = true;
                    return true;
                } else {
                    button.pressed(false);
                }
                xCorner += direction.xFactor * (button.width() + Config.BUTTON_X_MARGIN);
                yCorner += direction.yFactor * (button.height() + Config.BUTTON_Y_MARGIN);
            }
        }
        return false;
    }


    private boolean down(int screenX, int screenY) {

        buttonPressed = false;
        if(buttons.size()>0) {
            float xCorner = anchorPoint.x;
            float yCorner = anchorPoint.y;
            for (Button button : buttons) {
                Vector3 world = camera.unproject(new Vector3(screenX,screenY, 0));
                if (button.inWidget(world.x, world.y, xCorner - anchor.xFromBottomLeft * (button.width()), yCorner-anchor.yFromBottomLeft * (button.height()))) {
                    button.pressed(true);
                    buttonPressed = true;
                    return true;
                } else {
                    button.pressed(false);
                }
                xCorner += direction.xFactor * (button.width() + Config.BUTTON_X_MARGIN);
                yCorner += direction.yFactor * (button.height() + Config.BUTTON_Y_MARGIN);
            }
        }
        return false;
    }

    private void up() {
        for (Button button : buttons) {
            button.pressed(false);
        }
        buttonPressed = false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (buttonPressed) {
            down(screenX, screenY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int buttonNum) {
        if (buttonPressed) {
            up();
            return true;
        } else {
            up();
            return false;
        }
    }

    public enum Anchor {
        TOP_LEFT(0, 1), TOP_RIGHT(1, 1),
        BOTTOM_LEFT(0, 0), BOTTOM_RIGHT(1, 0),
        CENTER(0.5f, 0.5f);

        Anchor(float x, float y) {
            this.xFromBottomLeft = x;
            this.yFromBottomLeft = y;
        }

        public final float xFromBottomLeft;
        public final float yFromBottomLeft;
    }

    public enum Direction {
        DOWN(0, -1),
        UP(0, 1),
        LEFT_TO_RIGHT(1, 0),
        RIGHT_TO_LEFT(-1, 0);

        public final int xFactor;
        public final int yFactor;

        Direction(int xFactor, int yFactor) {
            this.xFactor = xFactor;
            this.yFactor = yFactor;
        }
    }

    public void addButton(String actionCommand, Sprite icon, ButtonAction action) {
        IconButton button = new IconButton(actionCommand, icon, buttonUp, buttonDown, 0, Config.BUTTON_DOWN_OFFSET, IconButton.Trigger.TRAILING_EDGE);
        buttons.add(button);
        button.addAction(action);
    }

    public void addButton(String actionCommand, Sprite icon, Array<ButtonAction> actions) {
        IconButton button = new IconButton(actionCommand, icon, buttonUp, buttonDown, 0, Config.BUTTON_DOWN_OFFSET, IconButton.Trigger.TRAILING_EDGE);
        buttons.add(button);
        for (int i = 0; i < actions.size; ++i) {
            button.addAction(actions.get(i));
        }
    }

    public void addButton(Button button){
        buttons.add(button);
    }

    public void addButton(String actionCommand, Sprite icon) {
        IconButton button = new IconButton(actionCommand, icon, buttonUp, buttonDown, 0, Config.BUTTON_DOWN_OFFSET, IconButton.Trigger.TRAILING_EDGE);
        buttons.add(button);

    }

    public Button getButton(int index) {
        return buttons.get(index);
    }

    public Button getButton(String actionCommand) {
        for (Button button : buttons) {
            if (actionCommand.equals(button.actionCommand)) {
                return button;
            }
        }
        return null;
    }

    public void removeButton(String actionCommand) {
        for (Button button : buttons) {
            if (actionCommand.equals(button.actionCommand)) {
                buttons.remove(button);
                return;
            }
        }
    }

    private boolean buttonPressed = false;

    public boolean buttonPressed() {
        return buttonPressed;
    }


    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        if(buttons.size()>0) {
            float x = anchorPoint.x ;
            float y = anchorPoint.y;
            for (Button button : buttons) {
                button.render(batch, x- anchor.xFromBottomLeft * (button.width()), y- anchor.yFromBottomLeft * (button.height()));
                x += direction.xFactor * (button.width() + Config.BUTTON_X_MARGIN);
                y += direction.yFactor * (button.height() + Config.BUTTON_Y_MARGIN);
            }
        }
    }

}
