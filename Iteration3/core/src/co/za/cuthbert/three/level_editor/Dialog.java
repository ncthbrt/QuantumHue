package co.za.cuthbert.three.level_editor;

import co.za.cuthbert.three.Iteration3Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Copyright Nick Cuthbert, 2014
 */
public abstract class Dialog {
    private int width;
    private int height;
    private boolean visible=false;
    private OrthographicCamera camera;
    private float slideTime=1;
    private float slide=0;
    private float offscreenY;
    private float x;
    private float onscreenY;
    protected InputMultiplexer multiplexer;
    protected static NinePatch dialogPatch=null;
    private float offsetX=0, offsetY=0;


    public Dialog(InputMultiplexer multiplexer,int width, int height){
        initialise(multiplexer,width,height);
    }

    public Dialog(){

    }

    public void initialise(InputMultiplexer multiplexer,int width, int height){
        this.multiplexer=multiplexer;
        this.width=width;
        this.height=height;
        if(dialogPatch==null) {
            dialogPatch = Iteration3Main.textureAtlas().createPatch("box");
        }

        camera=new OrthographicCamera(1920f,1920*Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        camera.update();
        float totalY=(dialogPatch.getPadTop()+dialogPatch.getPadBottom()+height);
        offscreenY=camera.viewportHeight;
        onscreenY=(camera.viewportHeight-totalY)/2f;
        x =(camera.viewportWidth-dialogPatch.getPadLeft()-dialogPatch.getPadRight()-width)/2f;
    }

    public void offset(float x, float y){
        this.offsetX=x;
        this.offsetY=y;
    }

    public OrthographicCamera camera(){
        return camera;
    }
    public Vector2 dialogPosition(){
        if(visible && slide<1){
            return new Vector2(x+offsetX,Interpolation.pow4Out.apply(offscreenY,onscreenY,slide)+offsetY);
        }else if(visible){
            return new Vector2(x+offsetX,onscreenY+offsetY);
        }else if(slide>0){
            return new Vector2(x+offsetX,Interpolation.pow4In.apply(offscreenY,onscreenY,slide)+offsetY);
        }else{
            return new Vector2(x+offsetX,offscreenY+offsetY);
        }

    }
    public Vector2 contentPosition(){
        Vector2 pos=dialogPosition();
        pos.add(dialogPatch.getLeftWidth(),dialogPatch.getBottomHeight()+dialogPatch.getPadTop());
        return pos;
    }
    public Vector2 contentWidthHeight(){
        return new Vector2(width,height);
    }

    public final void render(SpriteBatch batch, float delta){

        if(slide<1 && visible){

            slide+=(delta/slideTime);
            slide=Math.min(1, slide);
        }else if(slide>0 && !visible){

            slide-=(delta/slideTime);
            slide=Math.max(0, slide);
        }
        if(visible || slide>0) {
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            dialogPatch.setMiddleHeight(height+dialogPatch.getTopHeight());
            dialogPatch.setMiddleWidth(width+dialogPatch.getLeftWidth());
            Vector2 dialogCoord= dialogPosition();
            dialogPatch.draw(batch, dialogCoord.x, dialogCoord.y,dialogPatch.getTotalWidth(),dialogPatch.getTotalHeight());
            renderAction(batch, delta);
        }
    }

    public abstract void renderAction(SpriteBatch batch,float delta);


    public boolean inDialog(float screenX, float screenY){
        Vector3 world=camera.unproject(new Vector3(screenX,screenY,0));
        Vector2 pos=contentPosition();
        if(world.x>=pos.x && world.x<pos.x+width){
           if(world.y>=pos.y && world.y<pos.y+height){
                return true;
            }
        }
        return false;
    }



    private Array<InputProcessor> prevProcessors;
    public void show(){
        prevProcessors=new Array<InputProcessor>();
        visible=true;
        for(int i=0; i<multiplexer.getProcessors().size; i++){
            prevProcessors.add(multiplexer.getProcessors().get(i));
        }
        multiplexer.setProcessors(new Array<InputProcessor>());
    }

    public void hide(){
        multiplexer.setProcessors(prevProcessors); //Restores view
        visible=false;
    }

    public boolean visible(){
        return visible;
    }
}
