package co.za.cuthbert.three.level_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ButtonGroup {

    private OrthographicCamera camera;
    public ArrayList<Button> buttons;
    private final float xMargin=20f;
    private final float yMargin=10f;
    private final float buttonDownOffset=13f;
    private Sprite buttonUp;
    private Sprite buttonDown;

    public ButtonGroup (TextureAtlas atlas){
        buttons=new ArrayList<Button>();
        camera=new OrthographicCamera(1080*(Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight()),1080);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        camera.update();
        buttonDown=atlas.createSprite("button_default_down");
        buttonUp=atlas.createSprite("button_default_up");
    }

    public void addButton(String actionCommand,Sprite icon,ArrayList<ButtonAction> actions){
        Button button=new Button(actionCommand, icon, buttonUp, buttonDown,0,buttonDownOffset, Button.Trigger.EDGE);
        buttons.add(button);
        for(ButtonAction action:actions){
            button.addButtonAction(action);
        }
    }

    public Button getButton(int index) {
        return buttons.get(index);
    }

    public void render(SpriteBatch batch){
        batch.setProjectionMatrix(camera.combined);
        float x=camera.viewportWidth-(buttonUp.getRegionWidth()+xMargin);
        float y=yMargin;
        boolean touched=Gdx.input.isTouched();
        int touchX=Gdx.input.getX();
        int touchY=Gdx.input.getY();
        for(Button button:buttons){
            //x=x-(buttonUp.getRegionWidth()+xMargin);
            if(touched) {
                Vector3 world = camera.unproject(new Vector3(touchX, touchY, 0));
                if (button.inWidget(world.x, world.y, x, y)) {
                    button.pressed(true);
                }else{
                    button.pressed(false);
                }
            }else{
                button.pressed(false);
            }
            button.render(batch,x,y);
            y+=buttonDown.getHeight()+yMargin;
        }
    }
}
