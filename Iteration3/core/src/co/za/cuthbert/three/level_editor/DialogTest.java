package co.za.cuthbert.three.level_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class DialogTest extends Dialog {
    public DialogTest(TextureAtlas atlas) {
        super(atlas, 200,200);
        super.show();
    }

    @Override
    public void renderAction(SpriteBatch batch, float delta) {

    }

    @Override
    public void touchDownAction(float x, float y, int pointer, int button) {

    }

    @Override
    public void tapAction(float x, float y, int count, int button) {

    }

    @Override
    public void longPressAction(float x, float y) {

    }

    @Override
    public void flingAction(float velocityX, float velocityY, int button) {

    }

    @Override
    public void panAction(float x, float y, float deltaX, float deltaY) {

    }

    @Override
    public void panStopAction(float x, float y, int pointer, int button) {

    }

    @Override
    public void zoomAction(float initialDistance, float distance) {

    }

    @Override
    public void pinchAction(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

    }
}
