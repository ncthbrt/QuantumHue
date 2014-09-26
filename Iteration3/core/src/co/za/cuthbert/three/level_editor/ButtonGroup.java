package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.Iteration3Main;
import co.za.cuthbert.three.level_editor.actions.ButtonAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ButtonGroup extends InputAdapter {

    private OrthographicCamera camera;
    public ArrayList<Button> buttons;
    private final float xMargin = 20f;
    private final float yMargin = 10f;
    private final float buttonDownOffset = 13f;
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
        float xCorner = anchorPoint.x - anchor.xFromBottomLeft * (buttonUp.getRegionWidth());
        float yCorner = anchorPoint.y - anchor.yFromBottomLeft * (buttonUp.getRegionHeight());


        for (Button button : buttons) {
            Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));
            if (button.inWidget(world.x, world.y, xCorner, yCorner)) {
                buttonPressed = true;
                button.pressed(true);
                return true;
            } else {
                button.pressed(false);
            }
            xCorner += direction.xFactor * (buttonUp.getRegionWidth() + xMargin);
            yCorner += direction.yFactor * (buttonUp.getRegionHeight() + yMargin);
        }
        return false;
    }


    private boolean down(int screenX, int screenY) {
        buttonPressed = false;
        float xCorner = anchorPoint.x - anchor.xFromBottomLeft * (buttonUp.getRegionWidth());
        float yCorner = anchorPoint.y - anchor.yFromBottomLeft * (buttonUp.getRegionHeight());
        for (Button button : buttons) {
            Vector3 world = camera.unproject(new Vector3(screenX, Gdx.graphics.getHeight() - screenY, 0));
            if (button.inWidget(world.x, world.y, xCorner, yCorner)) {
                buttonPressed = true;
                button.pressed(true);
                return true;
            } else {
                button.pressed(false);
            }
            xCorner += direction.xFactor * (buttonUp.getRegionWidth() + xMargin);
            yCorner += direction.yFactor * (buttonUp.getRegionHeight() + yMargin);
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
        }
        return false;
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

    public void addButton(String actionCommand, Sprite icon, ArrayList<ButtonAction> actions) {
        Button button = new Button(actionCommand, icon, buttonUp, buttonDown, 0, buttonDownOffset, Button.Trigger.EDGE);
        buttons.add(button);
        for (ButtonAction action : actions) {
            button.addButtonAction(action);
        }
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

    private boolean buttonPressed = false;

    public boolean buttonPressed() {
        return buttonPressed;
    }


    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        float x = anchorPoint.x - anchor.xFromBottomLeft * (buttonUp.getRegionWidth());
        float y = anchorPoint.y - anchor.yFromBottomLeft * (buttonUp.getRegionHeight());
        for (Button button : buttons) {
            button.render(batch, x, y);
            x += direction.xFactor * (buttonUp.getRegionWidth() + xMargin);
            y += direction.yFactor * (buttonUp.getRegionHeight() + yMargin);

        }
    }

}
