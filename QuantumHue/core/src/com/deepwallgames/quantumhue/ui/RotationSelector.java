package com.deepwallgames.quantumhue.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deepwallgames.quantumhue.Iteration3Main;
import com.deepwallgames.quantumhue.collision.PixelMask;
import com.deepwallgames.quantumhue.collision.PixelMaskFactory;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.HSVColour;

/**
 * Created by nick on 16/11/2015.
 */
public class RotationSelector  implements GestureDetector.GestureListener{
    public int leverArmOffsetX=140;
    public int leverArmOffsetY=140;
    public int leverArmInternalOffsetY=108;
    public int leverHandleInternalOffsetY=20;

    public int leverArmInternalOffsetX=24;
    public int directionIndicatorOffsetY=320;
    private float minimumExposure=169f;
    private float maximumExposure;

    private boolean selected=false;
    private boolean selectorPanning =false;

    private final float slideTime=0.4f;
    private float slide=0;

    private final OrthographicCamera camera;

    private final Sprite rotationArm, handle, indicator,indicator_triangle, background,face;

    private PixelMask indicatorMask;
    private PixelMask handlePixmask;
    private int colourSelectorWidth;
    private float handleAngle=90;
    private float targetAngle=0;
    private float transitionProgress=1;
    private float transitionTime=0.4f;


    public RotationSelector(){
        this.camera=new OrthographicCamera(1920, Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()*1920);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        camera.update();


        boolean notWebgl=Gdx.app.getType()!= Application.ApplicationType.WebGL;
        Texture back=new Texture(Gdx.files.internal("rotation_background.png"),notWebgl);
        Texture handleTex=new Texture(Gdx.files.internal("rotation_handle.png"),notWebgl);
        Texture faceTex=new Texture(Gdx.files.internal("rotation_face.png"),notWebgl);
        Texture triTex=new Texture(Gdx.files.internal("rotation_indicator_triangle.png"),notWebgl);
        Texture indiTex=new Texture(Gdx.files.internal("rotation_indicator.png"),notWebgl);
        Texture armTex=new Texture(Gdx.files.internal("rotation_arm.png"),notWebgl);


        if(notWebgl) {
            back.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.MipMapLinearLinear);
            handleTex.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.Linear);
            faceTex.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.MipMapLinearLinear);
            triTex.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.MipMapLinearLinear);
            indiTex.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.MipMapLinearLinear);
            armTex.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.MipMapLinearLinear);
        }else{
            back.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            handleTex.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            faceTex.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            triTex.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            indiTex.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            armTex.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        }


        background=new Sprite(back);

        handle=new Sprite(handleTex);
        face=new Sprite(faceTex);
        indicator_triangle=new Sprite(triTex);
        indicator=new Sprite(indiTex);

        rotationArm=new Sprite(armTex);
        colourSelectorWidth=new Texture(Gdx.files.internal("colour_selector_border.png")).getWidth();

        PixelMaskFactory factory=new PixelMaskFactory();
        indicatorMask =factory.getPixelMask("rotation_indicator.png");
        handlePixmask=factory.getPixelMask("rotation_handle.png");
        maximumExposure=background.getHeight();
    }


    public void render(SpriteBatch batch, float deltaTime){
        this.camera.setToOrtho(false,1920,Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth()*1920);
        batch.setProjectionMatrix(camera.combined);
        if(!selectorPanning) {
            if (slide < 1 && selected) {
                slide += (deltaTime / slideTime);
                slide = Math.min(1, slide);
            } else if (slide > 0 && !selected) {
                slide -= (deltaTime / slideTime);
                slide = Math.max(0, slide);
        }
        }


        float currentExposure;
        if(!selectorPanning) {
            currentExposure = Interpolation.swing.apply(minimumExposure, maximumExposure, slide);
        }
        else{
            currentExposure = Interpolation.linear.apply(minimumExposure, maximumExposure, slide);
        }

        float y=camera.viewportHeight-currentExposure;
        float x=camera.viewportWidth-background.getRegionWidth()-colourSelectorWidth;
        float rAPosX=x+leverArmOffsetX-rotationArm.getWidth()/2f;
        float rAPosY=y+(background.getHeight()-leverArmOffsetY)-(rotationArm.getHeight()-leverArmInternalOffsetY);
        rotationArm.setPosition(rAPosX,rAPosY);

        float angle=handleAngle;
        if(handleReset) {
            handleReset = false;
            angle = (float) (Math.atan2(handlePosY - rAPosY, handlePosX - rAPosX) / Math.PI) * 180f;
            targetAngle = (Math.round(angle/45)) * 45;
            handleAngle = angle;
            transitionProgress = 0;
        }
        if(!handleTurning && transitionProgress<1){
            transitionProgress+=deltaTime/transitionTime;
            if(transitionProgress>=1f){
                handleAngle=targetAngle;
                transitionProgress=1f;
                angle=targetAngle;
            }else{
                angle=Interpolation.swing.apply(handleAngle,targetAngle,transitionProgress);
            }
        }else if(handleTurning){
            angle=(float)(Math.atan2(handlePosY-rAPosY,handlePosX-rAPosX)/Math.PI)*180f;
        }





        background.setPosition(x,y);
        background.draw(batch);

        indicatorMask.setPosition((int)x,(int)y);
        indicator.setPosition(x, y);

        indicator_triangle.setRotation(angle-90);
        indicator_triangle.setPosition(x+leverArmOffsetX-indicator_triangle.getWidth()/2f,y+(background.getHeight()-directionIndicatorOffsetY)-indicator_triangle.getHeight()/2f);



        HSVColour hsvColour=Iteration3Main.currentColour().toHSV();
        hsvColour.saturation(0.1f);
        hsvColour.value(1f);
        Colour c=hsvColour.toRGB();



        float r=c.red()/255f;
        float g=c.green()/255f;
        float b=c.blue()/255f;



        face.setColor(r,g,b,1);
        face.setPosition(x, y);
        face.draw(batch);

        indicatorMask.setPosition((int)x,(int)y);
        indicator.draw(batch);

        hsvColour.saturation(1f);
        c=hsvColour.toRGB();
        r=c.red()/255f;
        g=c.green()/255f;
        b=c.blue()/255f;






        float pivotX=leverArmInternalOffsetX;
        float pivotY=rotationArm.getHeight()-leverArmInternalOffsetY;
        rotationArm.setOrigin(pivotX,pivotY);
        rotationArm.setRotation(angle-90);


        handle.setColor(r,g,b,1f);

        handle.setOrigin(handle.getWidth()/2,handle.getWidth()/2);



        boolean isOver=false;
        if(Gdx.app.getType()!= Application.ApplicationType.Android && Gdx.app.getType()!= Application.ApplicationType.iOS){
            Vector3 worldXY = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (handlePixmask.isAt((int) worldXY.x, (int) worldXY.y)) {
                isOver=true;
            }
        }

        if(isOver || handleTurning) {
            handle.setScale(1.3f, 1.3f);
        }else{
            handle.setScale(1f, 1f);
        }


        float handlePosX=rAPosX+(float)(Math.abs(leverArmInternalOffsetY-leverHandleInternalOffsetY)*Math.cos((angle)/180f*Math.PI));
        float handlePosY=rAPosY+(float)(Math.abs(leverArmInternalOffsetY-leverHandleInternalOffsetY)*Math.sin((angle)/180f*Math.PI));
        handle.setPosition(handlePosX,handlePosY);



        handlePixmask.setPosition((int)handlePosX,(int)handlePosY);
        rotationArm.draw(batch);
        handle.draw(batch);
        indicator.setColor(r,g,b,1);
        indicator_triangle.draw(batch);
    }



    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        Vector3 world=camera.unproject(new Vector3(x,y,0));
        if(handlePixmask.isAt((int)world.x,(int)world.y)) {
            handleTurning=true;
            handlePosX=world.x;
            handlePosY=world.y;
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 world=camera.unproject(new Vector3(x,y,0));
        if(indicatorMask.isAt((int)world.x,(int)world.y)) {
            Gdx.app.log("Selected",selected+"");
            Gdx.app.log("Selected","Panning"+selectorPanning);
            selected=!selected;
            return true;
        }else if(handleTurning){
            handleTurning=false;
            targetAngle=handleAngle-45;
            transitionProgress=0;
        }

        return false;
    }

    private void deactivate(){

    }
    @Override
    public boolean longPress(float x, float y) {
        if(handleTurning){
            handleTurning=false;
            return true;
        }
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }


    private boolean handleTurning=false;
    private boolean handleReset=false;

    private float handlePosX,handlePosY;


    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 worldXY=camera.unproject(new Vector3(x,y,0));

        if(!handleTurning && handlePixmask.isAt((int)worldXY.x,(int)worldXY.y)){
            handleTurning=true;
            transitionProgress=0;
            targetAngle=handleAngle;
        }else if(!handleTurning && !selectorPanning && indicatorMask.isAt((int)worldXY.x,(int)worldXY.y)) {
            selectorPanning =true;
        }

        if(handleTurning){
            handlePosX=worldXY.x;
            handlePosY=worldXY.y;
            return true;
        }else if (selectorPanning) {
                float delta = (deltaY / (maximumExposure - minimumExposure));
                slide += delta;
                slide += delta;
                slide = slide > 1.0f ? 1.0f : slide;
                slide = slide < 0 ? 0 : slide;
        }
        return handleTurning|| selectorPanning;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (selectorPanning) {
            selectorPanning = false;
            selected = slide > 0.5;
            return true;
        }
        if(handleTurning){

            handleTurning=false;
            handleReset=true;
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
    public boolean hidden=false;
    public boolean hidden(){
        return hidden;
    }
    public void hide(){
        hidden=true;
    }
    public void show(){
        hidden=true;
    }
}
