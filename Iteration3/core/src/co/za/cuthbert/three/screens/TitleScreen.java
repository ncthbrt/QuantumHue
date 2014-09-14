package co.za.cuthbert.three.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by CTHNI_000 on 2014-09-13.
 */
public class TitleScreen implements Screen {
    SpriteBatch batch;
    Game game;
    Texture titleBackground;
    Sprite createButton;
    Sprite playButton;
    Sprite avatar;
    Sprite logo;
    Camera camera;

    float menuWidth;
    float menuHeight;
    public  TitleScreen(Game game){
        this.batch=new SpriteBatch();
        this.game=game;
//        titleBackground=new Texture(Gdx.files.internal("title_background.png"));

        createButton=new Sprite(new Texture(Gdx.files.internal("create_button.png")));

        playButton=new Sprite(new Texture(Gdx.files.internal("play_button.png")));
        logo=new Sprite(new Texture(Gdx.files.internal("logo.png")));
        avatar=new Sprite(new Texture(Gdx.files.internal("avatar.png")));
        menuWidth=avatar.getWidth()+logo.getWidth();
        menuHeight=avatar.getHeight();
        menuWidth=1920;
        menuHeight=1080;
        titleBackground=new Texture(Gdx.files.internal("avatar.png"));


        camera=new OrthographicCamera(menuWidth,menuHeight);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        FitViewport viewport=new FitViewport(menuWidth,menuHeight,camera);
        camera.update();
    }
    private final float fadeInTime=2;
    private float fadeInTimeRemaining=fadeInTime;
    private final float fadeOutTime=2;
    private float fadeOutTimeRemaining=fadeOutTime;
    private boolean fade=false;
    private Screen nextScreen=null;

    @Override
    public void render(float delta) {
        if(Gdx.input.isTouched() && nextScreen==null){
            int x =Gdx.input.getX();
            int y=Gdx.input.getY();
            Vector3 world=camera.unproject(new Vector3(x,y,0));
            if(playButton.getBoundingRectangle().contains(world.x,world.y)){

            }else if(createButton.getBoundingRectangle().contains(world.x,world.y)){
                nextScreen=new LevelEditor(game);
                fade=true;
            }
        }
        if(!fade && fadeInTimeRemaining>0) {
                fadeInTimeRemaining -= delta;
        }else if (fade){
            if(fadeOutTimeRemaining>0){
                fadeOutTimeRemaining-=delta;
            }else{
                game.setScreen(nextScreen);
            }
        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float location=(camera.viewportWidth-menuWidth)/2f;
        float alpha;
        if(!fade) {
            alpha=Math.min(1 - fadeInTimeRemaining / fadeInTime, 1f);
        }else{
            alpha=Math.max(fadeOutTimeRemaining / fadeOutTime, 0);
        }
        avatar.setAlpha(alpha);
        playButton.setAlpha(alpha);
        logo.setAlpha(alpha);
        createButton.setAlpha(alpha);

        avatar.setPosition(location,0);
        playButton.setPosition(location+avatar.getWidth(),0);
        createButton.setPosition(location+avatar.getWidth()+playButton.getWidth(),0);
        logo.setPosition(location+avatar.getWidth(),playButton.getHeight());

        avatar.draw(batch);
        logo.draw(batch);
        createButton.draw(batch);
        playButton.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        logo.getTexture().dispose();
        playButton.getTexture().dispose();
        createButton.getTexture().dispose();
        avatar.getTexture().dispose();
        batch.dispose();

    }
}
