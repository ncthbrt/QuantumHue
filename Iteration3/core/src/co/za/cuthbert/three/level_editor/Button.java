package co.za.cuthbert.three.level_editor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class Button implements Widget {
    private final Sprite icon;
    private final Sprite buttonUp;
    private final Sprite buttonDown;


    public final String actionCommand;


    private float downOffset;
    private float upOffset;
    public Button(String actionCommand, Sprite icon, Sprite buttonUp, Sprite buttonDown, float upOffset,float downOffset){
        this.actionCommand=actionCommand;
        this.icon=icon;
        this.buttonDown=buttonDown;
        this.buttonUp=buttonUp;
        this.downOffset=downOffset;
        this.upOffset=upOffset;
    }
    private float x, y;
    public void render(SpriteBatch batch, float x, float y, boolean pressed){
        this.x=x;
        this.y=y;
        if(pressed){
            buttonDown.setPosition(x,y);
            buttonDown.draw(batch);
            icon.setPosition(x,downOffset);
        }else{
            buttonUp.setPosition(x,y);
            buttonUp.draw(batch);
            icon.setPosition(x,upOffset);
        }
        icon.draw(batch);
    }

    @Override
    public boolean inWidget(float worldX, float worldY) {
        return worldX>x && worldX<x+buttonUp.getRegionWidth() && worldY<y && worldY>y-buttonUp.getRegionHeight();
    }
}
