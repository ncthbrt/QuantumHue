package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.collision.PixelMask;
import co.za.cuthbert.three.collision.PixelMaskFactory;
import co.za.cuthbert.three.value_objects.Colour;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ColourSelector implements GestureDetector.GestureListener{

    private float minimumExposure=169f;
    private float maximumExposure;

    private boolean selected=false;
    private final float slideTime=0.4f;
    private float slide=0;

    private Pixmap pixmap;
    private final Colour currentColour=new Colour(255,0,0,255);
    public Colour currentColour(){
        return currentColour;
    }

    private final OrthographicCamera camera;
    private final Sprite border, colourSelector,indicator;
    private PixelMask colourSelectorIndicatorMask;
    private Pixmap colourSelectorPixmap;
    public ColourSelector(){
        this.camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        camera.update();

        border=new Sprite(new Texture(Gdx.files.internal("colour_selector_border.png")));
        PixelMaskFactory factory=new PixelMaskFactory();
        colourSelectorIndicatorMask=factory.getPixelMask("colour_selector_indicator.png");
        indicator=new Sprite(new Texture(Gdx.files.internal("colour_selector_indicator.png")));
        colourSelectorPixmap=factory.getPixmap("colour_selector.png");
        colourSelector=new Sprite(new Texture(Gdx.files.internal("colour_selector.png"))); //Unfortunate double loading of textures
        maximumExposure=border.getHeight();
    }

    public void render(SpriteBatch batch,float deltaTime){
        batch.setProjectionMatrix(camera.combined);
        if(slide<1 && selected){
            slide+=(deltaTime/slideTime);
            slide=Math.min(1, slide);
        }else if(slide>0 && !selected){
            slide-=(deltaTime/slideTime);
            slide=Math.max(0, slide);
        }
        float currentExposure= Interpolation.pow3.apply(minimumExposure, maximumExposure, slide);
        float y=camera.viewportHeight-currentExposure;
        float x=camera.viewportWidth-border.getRegionWidth();
        border.setPosition(x,y);
        border.draw(batch);
        indicator.setPosition(x, y);
        indicator.setColor(currentColour.red() / 255f, currentColour.green() / 255f, currentColour.blue() / 255f, currentColour.alpha() / 255f);
        indicator.draw(batch);
        colourSelector.setPosition(x, y);
        colourSelectorIndicatorMask.setPosition((int)x,(int)y);
        colourSelector.draw(batch);
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 world=camera.unproject(new Vector3(x,y,0));
        System.out.println("Tapping");
        if(colourSelectorIndicatorMask.isAt((int)world.x,(int)world.y)) {
            System.out.println("Pressed");
            selected=!selected;
            return true;
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
