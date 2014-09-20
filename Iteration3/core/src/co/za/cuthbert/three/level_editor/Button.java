package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.Iteration3Main;
import co.za.cuthbert.three.value_objects.Colour;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class Button implements Widget {
    private final Sprite icon;
    private final Sprite buttonUp;
    private final Sprite buttonDown;


    public final String actionCommand;
    public ArrayList<ButtonAction> actions=new ArrayList<ButtonAction>();
    public void addButtonAction(ButtonAction action){
        actions.add(action);
    }
    public enum Trigger{
        EDGE,
        LEADING_EDGE,
        TRAILING_EDGE
    }
    private float downOffset;
    private float upOffset;
    private Trigger trigger;
    public Button(String actionCommand, Sprite icon, Sprite buttonUp, Sprite buttonDown, float upOffset,float downOffset, Trigger trigger){
        this.actionCommand=actionCommand;
        this.icon=icon;
        this.buttonDown=buttonDown;
        this.buttonUp=buttonUp;
        this.downOffset=downOffset;
        this.upOffset=upOffset;
        this.trigger=trigger;
    }

    private  boolean pressed=false;
    public boolean pressed(){
        return pressed();
    }
    public void pressed(boolean pressed){
        if(trigger==Trigger.EDGE || (trigger==Trigger.TRAILING_EDGE && this.pressed && !pressed)||(trigger==Trigger.LEADING_EDGE && !this.pressed && pressed)){
            for (ButtonAction action:actions){
                action.actionPerformed(actionCommand);
            }
        }
        this.pressed=pressed;
    }

    public void render(SpriteBatch batch, float x, float y){
        if(pressed){
            buttonDown.setPosition(x,y);
            buttonDown.draw(batch);
            icon.setPosition(x,y-downOffset);
        }else{
            buttonUp.setPosition(x,y);
            buttonUp.draw(batch);
            icon.setPosition(x,y+upOffset);
        }

        icon.draw(batch);
    }

    @Override
    public boolean inWidget(float worldX, float worldY, float x, float y) {
        return worldX>=x && worldX<x+buttonUp.getRegionWidth() && worldY>=y && worldY<y+buttonUp.getRegionHeight();

    }
}
