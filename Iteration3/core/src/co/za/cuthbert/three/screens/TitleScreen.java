package co.za.cuthbert.three.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by CTHNI_000 on 2014-09-13.
 */
public class TitleScreen implements Screen {
    SpriteBatch batch;
    Game game;
    Texture titleBackground;
    Camera camera;

    public  TitleScreen(Game game){
        this.batch=new SpriteBatch();
        this.game=game;
        titleBackground=new Texture(Gdx.files.internal("title_background.png"));
        FitViewport fitViewport=new FitViewport(titleBackground.getWidth(),titleBackground.getHeight());
        camera=fitViewport.getCamera();
        camera.update();
    }
    private final float fadeInTime=1.5f;
    private float fadeInTimeRemaining=fadeInTime;
    @Override
    public void render(float delta) {
        if (fadeInTimeRemaining>0){
            fadeInTimeRemaining-=delta;
        }

        batch.begin();
        batch.setColor(1,1,1,Math.min(1-fadeInTimeRemaining/fadeInTime,1f));
        batch.draw(titleBackground, 0,0);
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


    }
}
