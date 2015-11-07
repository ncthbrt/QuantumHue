package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.EntityType;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.WireComponent;
import co.za.cuthbert.three.value_objects.ColourBracket;
import co.za.cuthbert.three.value_objects.ColourVector;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireRendererSystem extends EntitySystem implements LevelChangeListener {
    private final ShapeRenderer shapeRenderer;
    private final float lineWidth=3f;
    private final float blur=3f;
    private Engine engine;
    private Level level;
    FrameBuffer buffer;
    FrameBuffer blurBufferA;
    FrameBuffer blurBufferB;
    TextureRegion wireRegion;
    Batch batch;
    ShaderProgram blurShader;

    public WireRendererSystem(ShapeRenderer shapeRenderer,Batch batch, ShaderProgram blurShader) {
        buffer=new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),false);
        blurBufferA =new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),false);
        blurBufferB=new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),false);



        wireRegion=new TextureRegion(buffer.getColorBufferTexture());
        wireRegion.flip(false,true);
        priority = 4;
        this.shapeRenderer = shapeRenderer;
        this.batch=batch;
        this.blurShader=blurShader;
    }

    public void level(Level level) {
        this.level = level;
    }


    @Override
    public void update(float deltaTime) {

        if (level != null) {
            buffer.begin();
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(level.camera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Entity entity : level) {
                if (entity != null && EntityType.WIRE.family.matches(entity)) {
                    processEntity(entity, deltaTime,true);
                }
            }
            shapeRenderer.end();
            buffer.end();


            blurBufferA.begin();
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.setShader(blurShader);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setBlendFunction(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            batch.setColor(1, 1, 1, 1);
            blurShader.setUniformf("dir", 1f, 0f);
            blurShader.setUniformf("blur", blur / (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()) / Gdx.graphics.getWidth() / level.zoom());
            wireRegion.setTexture(buffer.getColorBufferTexture());
            batch.draw(wireRegion, 0, 0);
            batch.flush();
            blurBufferA.end();

            blurBufferB.begin();
            batch.setShader(blurShader);
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setBlendFunction(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            batch.setColor(1, 1, 1, 1);
            blurShader.setUniformf("dir", 0f, 1f);
            blurShader.setUniformf("blur", blur / Gdx.graphics.getWidth() / level.zoom());
            wireRegion.setTexture(blurBufferA.getColorBufferTexture());
            batch.draw(wireRegion, 0, 0);
            batch.flush();
            blurBufferB.end();

            batch.setShader(null);
            batch.setColor(1f, 1f, 1f, 1f);

            wireRegion.setTexture(blurBufferB.getColorBufferTexture());
            batch.draw(wireRegion, 0, 0);
            batch.end();
            shapeRenderer.setProjectionMatrix(level.camera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (Entity entity : level) {
                if (entity != null && EntityType.WIRE.family.matches(entity)) {
                    processEntity(entity, deltaTime,false);
                }
            }
            shapeRenderer.end();
        }

    }


    private static final ComponentMapper<WireComponent> wireComponentMapper = ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);


    public void processEntity(Entity wire, float deltaTime,boolean drawOnlyColors) {
        PortComponent ports = portMapper.get(wire);
        WireComponent wireComponent = wireComponentMapper.get(wire);
        DVector2 position = discretePositionMapper.get(wire);

        boolean[] portMap = ports.portMask();

        int attachedPorts = 0;
        float actualLineWidth=lineWidth / level.zoom();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; j++) {
                if (j != 0 || i != 0) {
                    if (portMap[3 * (j + 1) + i + 1] && position.x() + i < level.width() && position.y() + j < level.height() && position.x() + i >= 0 && position.y() + j >= 0) {
                        float lastPoint = 0;
                        ColourVector vector = wireComponent.resultantVector(PortComponent.portNumber(i, j));
                        for (int k = 0; k < vector.line.size() - 1; ++k) {
                            ColourBracket end = vector.line.get(k + 1);
                            ColourBracket start = vector.line.get(k);
                            if (start.colour.equals(DiscreteColour.ALPHA.toColour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                                if(!drawOnlyColors) {
                                    shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
                                }else{
                                    shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0f);
                                }
                            } else {
                                shapeRenderer.setColor(start.colour.red() / 255f, start.colour.green() / 255f, start.colour.blue() / 255f, 1f);
                            }
                            float point = Interpolation.linear.apply(0, Config.TILE_SIZE / 2f, end.position());
                            Vector2 startVec=new Vector2(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint);
                            Vector2 endVec=new Vector2(position.x() * Config.TILE_SIZE + i * point, position.y() * Config.TILE_SIZE + j * point);
                            if(k==0){
                                shapeRenderer.circle(startVec.x, startVec.y, actualLineWidth / 2f);
                            }
                                shapeRenderer.rectLine(startVec.x,startVec.y,endVec.x,endVec.y, actualLineWidth);
                            lastPoint = point;
                        }

                        ColourBracket last = vector.line.get(vector.line.size() - 1);
                        if (last.colour.equals(DiscreteColour.ALPHA.toColour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                            if(!drawOnlyColors) {
                                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
                            }else{
                                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0f);
                            }
                        } else {
                            shapeRenderer.setColor(last.colour.red() / 255f, last.colour.green() / 255f, last.colour.blue() / 255f, 1f);
                        }
                        if(vector.line.size()==1) {
                            Vector2 xy = new Vector2(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint);
                            shapeRenderer.circle(xy.x,xy.y,actualLineWidth/2f);
                        }


                        shapeRenderer.rectLine(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * (Config.TILE_SIZE / 2f), position.y() * Config.TILE_SIZE + j * (Config.TILE_SIZE / 2f), actualLineWidth);
                        ++attachedPorts;
                    }
                }

            }
        }
        if (attachedPorts == 0) {
            shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1);
            shapeRenderer.circle(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE, Config.TILE_SIZE / 2,40);
        }
    }


}

