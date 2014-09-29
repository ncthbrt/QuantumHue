package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.collision.PixelMask;
import co.za.cuthbert.three.collision.PixelMaskFactory;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private boolean panning=false;

    private final float slideTime=0.4f;
    private float slide=0;

    private Pixmap pixmap;
    private DiscreteColour currentColour=DiscreteColour.WHITE;
    public DiscreteColour currentColour(){
        return currentColour;
    }

    private final OrthographicCamera camera;
    private final Sprite border, colourSelector,indicator;
    private PixelMask colourSelectorIndicatorMask;
    private PixelMask colourSelectorMask;
    private Pixmap colourSelectorPixmap;
    public ColourSelector(){
        this.camera=new OrthographicCamera(1920,Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()*1920);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        camera.update();

        border=new Sprite(new Texture(Gdx.files.internal("colour_selector_border.png")));
        PixelMaskFactory factory=new PixelMaskFactory();
        colourSelectorIndicatorMask=factory.getPixelMask("colour_selector_indicator.png");
        indicator=new Sprite(new Texture(Gdx.files.internal("colour_selector_indicator.png")));

        colourSelectorPixmap=factory.getPixmap("colour_selector.png");
        colourSelectorMask=factory.getPixelMask("colour_selector.png");
        colourSelector=new Sprite(new Texture(Gdx.files.internal("colour_selector.png"))); //Unfortunate double loading of textures

        maximumExposure=border.getHeight();
    }

    public void render(SpriteBatch batch,float deltaTime){
        batch.setProjectionMatrix(camera.combined);
        if(!panning) {
            if (slide < 1 && selected) {
                slide += (deltaTime / slideTime);
                slide = Math.min(1, slide);
            } else if (slide > 0 && !selected) {
                slide -= (deltaTime / slideTime);
                slide = Math.max(0, slide);
            }
        }


        float currentExposure= Interpolation.pow3.apply(minimumExposure, maximumExposure, slide);
        float y=camera.viewportHeight-currentExposure;
        float x=camera.viewportWidth-border.getRegionWidth();

        border.setPosition(x,y);
        border.draw(batch);

        colourSelectorIndicatorMask.setPosition((int)x,(int)y);
        indicator.setPosition(x, y);
        Colour indicatorColour=currentColour.toColour();
        indicator.setColor(indicatorColour.red() / 255f, indicatorColour.green() / 255f, indicatorColour.blue() / 255f, indicatorColour.alpha() / 255f);
        indicator.draw(batch);

        colourSelector.setPosition(x, y);
        colourSelectorMask.setPosition((int)x,(int)y);
        colourSelector.draw(batch);
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 world=camera.unproject(new Vector3(x,y,0));
        if(colourSelectorIndicatorMask.isAt((int)world.x,(int)world.y)) {
            selected=!selected;
            return true;
        }else if(colourSelectorMask.isAt((int)world.x,(int)world.y)){
            int i=(int)world.x-colourSelectorMask.x;
            int j=colourSelectorMask.height-((int)world.y-colourSelectorMask.y);
            Colour colour=new Colour(colourSelectorPixmap.getPixel(i,j));
            int r=colour.red();
            int g=colour.green();
            int b=colour.blue();
            boolean red=r>200?true:false;
            boolean green=g>200?true:false;
            boolean blue=b>200?true:false;
            if(r==255 || g==255 || b==255){
                currentColour=DiscreteColour.map(red, green, blue);
            }
            System.out.println(currentColour.name());
            return true;
        }
        System.out.println(currentColour.name());
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
        Vector3 worldXY=camera.unproject(new Vector3(x,y,0));
        if(!panning && colourSelectorIndicatorMask.isAt((int)worldXY.x,(int)worldXY.y)) {
            panning=true;
        }
        if(panning){
            float delta=(deltaY/(maximumExposure-minimumExposure));
            slide+=delta;
            slide+=delta;
            slide=slide>1?1:slide;
            slide=slide<0?0:slide;
            return true;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (panning) {
            if(slide>0.5){
                selected=true;
            }else{
                selected=false;
            }
            panning = false;

            return true;
        }
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
