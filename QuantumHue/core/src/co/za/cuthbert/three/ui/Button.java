package co.za.cuthbert.three.ui;

import co.za.cuthbert.three.ui.actions.ButtonAction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Button implements Widget{

    public enum Trigger {
        EDGE,
        LEADING_EDGE,
        TRAILING_EDGE
    }
    public String actionCommand;
    private Array<ButtonAction> actions;
    public Button(String actionCommand, Trigger trigger){
        this.actionCommand=actionCommand;
        this.trigger=trigger;
        this.actions=new Array<ButtonAction>();
    }


    public void addAction(ButtonAction action) {
        if (action != null) {
            actions.add(action);
        }
    }

    public void removeAction(ButtonAction action) {
        actions.removeValue(action, true);
    }

    public abstract float width();
    public abstract float height();
    private Trigger trigger;
    public Trigger trigger(){
        return trigger;
    }

    public void trigger(Trigger trigger) {
        this.trigger = trigger;
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

    public abstract void render(SpriteBatch batch, float x, float y);
    public abstract boolean inWidget(float worldX, float worldY, float x, float y);
}
