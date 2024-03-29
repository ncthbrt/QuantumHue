package com.deepwallgames.quantumhue.ui;


import com.deepwallgames.quantumhue.ui.actions.ButtonAction;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**The icon button is not a dynamic button. The button size is fixed at 88x89 pixels
 * Copyright Nick Cuthbert, 2014
 */
public class IconButton extends Button {
    public IconButton(String actionCommand, Sprite icon, Sprite buttonUp, Sprite buttonDown, float upOffset, float downOffset, Trigger trigger) {
        super(actionCommand, trigger);
        this.actionCommand = actionCommand;
        this.icon = icon;
        this.buttonDown = buttonDown;
        this.buttonUp = buttonUp;
        this.downOffset = downOffset;
        this.upOffset = upOffset;
        this.trigger = trigger;
        this.actions = new Array<ButtonAction>();
    }

    private Sprite icon;
    private final Sprite buttonUp;


    private final Sprite buttonDown;
    public final String actionCommand;

    private Array<ButtonAction> actions;

    public void addAction(ButtonAction action) {
        if (action != null) {
            actions.add(action);
        }
    }



    public void removeAction(ButtonAction action) {
        actions.removeValue(action, true);
    }

    @Override
    public float width() {
        return buttonUp.getRegionWidth();
    }

    @Override
    public float height() {
        return buttonUp.getRegionHeight();
    }

    private float downOffset;
    private float upOffset;

    private Trigger trigger;


    public void icon(Sprite icon) {
        this.icon = icon;
    }

    private boolean pressed = false;

    public boolean pressed() {
        return pressed;
    }

    public void pressed(boolean pressed) {
        if ((trigger == Trigger.EDGE && pressed) || (trigger == Trigger.TRAILING_EDGE && this.pressed && !pressed) || (trigger == Trigger.LEADING_EDGE && !this.pressed && pressed)) {
            for (int i = 0; i < actions.size; ++i) {
                actions.get(i).actionPerformed(actionCommand);
            }
            this.pressed = false;
        } else {
            this.pressed = pressed;
        }
    }

    public void render(SpriteBatch batch, float x, float y) {
        if (pressed) {

            buttonDown.setPosition(x, y);
            buttonDown.draw(batch);
            icon.setPosition(x, y - downOffset);
        } else {
            buttonUp.setPosition(x, y);
            buttonUp.draw(batch);
            icon.setPosition(x, y + upOffset);
        }

        icon.draw(batch);
    }

    @Override
    public boolean inWidget(float worldX, float worldY, float x, float y) {
        return worldX >= x && worldX < x + buttonUp.getRegionWidth() && worldY >= y && worldY < y + buttonUp.getRegionHeight();

    }
}
