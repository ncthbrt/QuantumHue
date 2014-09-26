package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.WireComponent;

import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.ColourBracket;
import co.za.cuthbert.three.value_objects.ColourVector;
import co.za.cuthbert.three.Level;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireRendererSystem extends EntitySystem implements LevelChangeListener {
    private final ShapeRenderer shapeRenderer;

    private Engine engine;
    private Level level;

    public WireRendererSystem(ShapeRenderer shapeRenderer) {
        super(3);
        this.shapeRenderer = shapeRenderer;
    }

    public void level(Level level) {
        this.level = level;
    }


    @Override
    public void update(float deltaTime) {
        if (level != null) {
            shapeRenderer.setProjectionMatrix(level.camera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            for (Entity entity : level) {
                if (entity != null) {
                    processEntity(entity, deltaTime);
                }
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }


    private static final ComponentMapper<WireComponent> wireComponentMapper = ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);


    public void processEntity(Entity wire, float deltaTime) {

        PortComponent ports = portMapper.get(wire);
        WireComponent wireComponent = wireComponentMapper.get(wire);
        DVector2 position = discretePositionMapper.get(wire);

        boolean[] portMap = ports.getPortMap();

        int attachedPorts = 0;

        for (int j = 1; j >= -1; --j) {
            for (int i = -1; i <= 1; i++) {
                if (j != 0 || i != 0) {
                    if (portMap[3 * (j + 1) + i + 1] && position.x() + i < level.width() && position.y() + j < level.height() && position.x() + i >= 0 && position.y() + j >= 0) {
                        float lastPoint = 0;
                        ColourVector vector = wireComponent.getResultantVector(3 * (j + 1) + i + 1);
                        for (int k = 0; k < vector.line.size() - 1; k++) {
                            ColourBracket end = vector.line.get(k + 1);
                            ColourBracket start = vector.line.get(k);
                            if (start.colour.equals(new Colour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                                shapeRenderer.setColor(1, 1, 1, 0.4f);
                            } else {
                                shapeRenderer.setColor(start.colour.red() / 255f, start.colour.green() / 255f, start.colour.blue() / 255f, 1f);
                            }
                            float point = Interpolation.linear.apply(0, Config.TILE_SIZE / 2, end.position());
                            shapeRenderer.line(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * point, position.y() * Config.TILE_SIZE + j * point);
                            lastPoint = point;
                        }
                        ColourBracket last = vector.line.get(vector.line.size() - 1);
                        if (last.colour.equals(new Colour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                            shapeRenderer.setColor(1, 1, 1, 0.4f);
                        } else {
                            shapeRenderer.setColor(last.colour.red() / 255f, last.colour.green() / 255f, last.colour.blue() / 255f, 1f);
                        }
                        shapeRenderer.line(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * Config.TILE_SIZE / 2, position.y() * Config.TILE_SIZE + j * Config.TILE_SIZE / 2);
                        ++attachedPorts;
                    }
                }

            }
        }
        if (attachedPorts == 0) {
            shapeRenderer.circle(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE, Config.TILE_SIZE / 2);
        }
    }


}
