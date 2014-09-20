package co.za.cuthbert.three.level_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Dialog implements GestureDetector.GestureListener {
    private final int width;
    private final int height;
    private boolean visible=false;
    private final OrthographicCamera camera;
    private final OrthographicCamera renderActionCamera;
    private final float slideTime=0.4f;
    private float slide=0;
    private final float offscreenY;
    private final float x;
    private final float onscreenY;

    private final NinePatch dialogPatch;

    public Dialog(TextureAtlas atlas,int width, int height){
        this.width=width;
        this.height=height;
        dialogPatch=atlas.createPatch("box");

        camera=new OrthographicCamera(1920f,1920*Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        renderActionCamera=new OrthographicCamera(width,height);
        renderActionCamera.position.set(renderActionCamera.viewportWidth/2f,renderActionCamera.viewportHeight/2f,0);
        renderActionCamera.update();
        camera.update();

        float totalY=(dialogPatch.getPadTop()+dialogPatch.getPadBottom()+height);
        offscreenY=-totalY;
        onscreenY=(camera.viewportHeight-totalY)/2f;
        x =(camera.viewportWidth-dialogPatch.getPadLeft()-dialogPatch.getPadRight()-width)/2f;
    }

    public final void render(SpriteBatch batch, float delta){
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if(slide<1 && visible){
            System.out.println("Slide "+ slide);
            slide+=(delta/slideTime);
            slide=Math.min(1, slide);
        }else if(slide>0 && !visible){
            System.out.println("Slide "+ slide);
            slide-=(delta/slideTime);
            slide=Math.max(0, slide);
        }
        float y= (int)Interpolation.elastic.apply(offscreenY,onscreenY,slide);
        if(visible || slide>0) {
            dialogPatch.draw(batch, x, y, width, height);
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();
            batch.end();
            Gdx.gl.glViewport((int) x, (int) y, width, height);
            batch.begin();//Expensive but necessary. I'm changing the viewport size, so the batch needs to flush
            renderActionCamera.update();

            batch.setProjectionMatrix(renderActionCamera.combined);
            renderAction(batch, delta);
            Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
        }
    }

    public abstract void renderAction(SpriteBatch batch,float delta);




    public void show(){
        this.visible=true;
    }
    public void hide(){
        this.visible=false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(visible){
            touchDownAction(x,y,pointer,button);
            return true;
        }else{
            return false;
        }
    }

    public abstract void touchDownAction(float x, float y, int pointer,int button);

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(visible){
            tapAction(x,y,count,button);
            return true;
        }else{
            return false;
        }
    }

    public abstract void tapAction(float x, float y, int count, int button) ;

    @Override
    public boolean longPress(float x, float y) {
        if(visible){
            longPressAction(x,y);
            return true;
        }else{
            return false;
        }
    }


    public abstract void longPressAction(float x, float y);



    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(visible){
            flingAction(velocityX,velocityY,button);
            return true;
        }else{
            return false;
        }
    }

    public abstract void flingAction(float velocityX, float velocityY, int button);



    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(visible){
            panAction(x,y,deltaX,deltaY);
            return true;
        }else{
            return false;
        }
    }
    public abstract void panAction(float x, float y, float deltaX, float deltaY);

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(visible){
            panStopAction(x,y,pointer,button);
            return true;
        }else{
            return false;
        }
    }
    public abstract void panStopAction(float x, float y, int pointer, int button);


    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(visible){
            return true;
        }else{
            return false;
        }
    }
    public abstract void zoomAction(float initialDistance, float distance);

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if(visible){
            return true;
        }else{
            return false;
        }
    }
    public abstract void pinchAction(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2);
}
