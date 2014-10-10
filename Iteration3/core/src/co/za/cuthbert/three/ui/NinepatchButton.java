package co.za.cuthbert.three.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class NinePatchButton extends Button {

    private static NinePatch buttonUp=null;
    private static NinePatch buttonDown=null;
    private static float upOffset;
    private static float downOffset;

    public static void initialise(TextureAtlas atlas, float upOffset, float downOffset){
        buttonUp=atlas.createPatch("button_up");
        buttonDown=atlas.createPatch("button_down");
        NinePatchButton.upOffset=upOffset;
        NinePatchButton.downOffset=downOffset;
    }


    public void icon(Sprite icon){
            this.icon=icon;
    }
    private Sprite icon;
    public NinePatchButton(String actionCommand, Trigger trigger, Sprite icon) {
        super(actionCommand, trigger);
        this.icon=icon;

        if(buttonUp==null){
            throw new IllegalArgumentException("You need to initialise the class first, using the static initialise method");
        }
    }

    @Override
    public float width() {
        return icon.getWidth();
    }

    public float height(){
        return icon.getHeight();
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {
        if (pressed()) {
            buttonDown.draw(batch,x,y,icon.getRegionWidth(),icon.getRegionHeight()-downOffset);
            icon.setPosition(x, y - downOffset);
        } else {
            buttonUp.draw(batch,x,y,icon.getRegionWidth(),icon.getRegionHeight());
            icon.setPosition(x, y + upOffset);
        }

        icon.draw(batch);
    }

    @Override
    public boolean inWidget(float worldX, float worldY, float x, float y) {
        return worldX >= x && worldX < x + width() && worldY >= y && worldY < y + height();
    }
}
