package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.level_editor.tools.Tool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;


/**
 * Copyright Nick Cuthbert, 2014
 */
public class ToolChooserWidget {
    private final Tool tool;

    private final Sprite icon;
    private boolean selected = false;
    private final float slideTime = 0.25f;
    private float slide = 0;
    private float x1, x2;

    public ToolChooserWidget(Tool tool, Sprite icon, float x1, float x2) {
        this.tool = tool;
        this.icon = icon;
        this.x1 = x1;
        this.x2 = x2;
    }

    public void setXs(float x1, float x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    private float y;

    public Sprite icon() {
        return icon;
    }

    public void render(SpriteBatch batch, float deltaTime, float y) {
        if (slide < 1 && selected) {
            slide += (deltaTime / slideTime);
            slide = Math.min(1, slide);
        } else if (slide > 0 && !selected) {
            slide -= (deltaTime / slideTime);
            slide = Math.max(0, slide);
        }
        float x = Interpolation.pow3.apply(x1, x2, slide);
        icon.setPosition(x, y);
        icon.draw(batch);
    }

    public void selected(boolean selected) {
        this.selected = selected;
    }

    public boolean selected() {
        return selected;
    }

    public Tool tool() {
        return tool;
    }


    public boolean inWidget(float worldX, float worldY, float y) {
        float x = Interpolation.elasticIn.apply(x1, x2, slide);
        return worldX > x && worldX < x + icon.getRegionWidth() && worldY < y && worldY > y - icon.getRegionHeight();
    }

}
