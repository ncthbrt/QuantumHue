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
import com.badlogic.gdx.graphics.GL20;
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
        priority = 4;
        this.shapeRenderer = shapeRenderer;
    }

    public void level(Level level) {
        this.level = level;
    }


    @Override
    public void update(float deltaTime) {
        if (level != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(level.camera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Entity entity : level) {
                if (entity != null && EntityType.WIRE.family.matches(entity)) {
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

        boolean[] portMap = ports.portMask();

        int attachedPorts = 0;

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; j++) {
                if (j != 0 || i != 0) {
                    if (portMap[3 * (j + 1) + i + 1] && position.x() + i < level.width() && position.y() + j < level.height() && position.x() + i >= 0 && position.y() + j >= 0) {
                        float lastPoint = 0;
                        ColourVector vector = wireComponent.resultantVector(PortComponent.portNumber(i, j));
                        for (int k = 0; k < vector.line.size() - 1; ++k) {
                            ColourBracket end = vector.line.get(k + 1);
                            ColourBracket start = vector.line.get(k);
                            if (start.colour== DiscreteColour.ALPHA) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                                shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
                            } else {
                                shapeRenderer.setColor(start.colour.toColour().red() / 255f, start.colour.toColour().green() / 255f, start.colour.toColour().blue() / 255f, 1f);
                            }
                            float point = Interpolation.linear.apply(0, Config.TILE_SIZE / 2f, end.position());
                            shapeRenderer.line(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * point, position.y() * Config.TILE_SIZE + j * point);
                            lastPoint = point;
                        }
                        ColourBracket last = vector.line.get(vector.line.size() - 1);
                        if (last.colour==DiscreteColour.ALPHA) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                            shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
                        } else {
                            shapeRenderer.setColor(last.colour.toColour().red() / 255f, last.colour.toColour().green() / 255f, last.colour.toColour().blue() / 255f, 1f);
                        }
                        shapeRenderer.line(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * Config.TILE_SIZE / 2, position.y() * Config.TILE_SIZE + j * Config.TILE_SIZE / 2);
                        ++attachedPorts;
                    }
                }

            }
        }
        if (attachedPorts == 0) {
            shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1);
            shapeRenderer.circle(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE, Config.TILE_SIZE / 2);
        }
    }


}
