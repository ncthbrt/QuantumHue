package co.za.cuthbert.three.systems;

import co.za.cuthbert.three.Config;
import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.SwitchComponent;
import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PowerRendererSystem extends EntitySystem implements LevelChangeListener {


    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);
    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    private static final ComponentMapper<SwitchComponent> switchComponentMapper = ComponentMapper.getFor(SwitchComponent.class);
    private Level level;

    private final SpriteBatch batch;
    private final Sprite powerPortSprite;
    private final Family powerPortFamily;

    public PowerRendererSystem(SpriteBatch batch, TextureAtlas atlas) {
        powerPortFamily = TileType.POWER_SOURCE.family;
        priority = 4;
        this.batch = batch;
        powerPortSprite = atlas.createSprite("power_source");
    }

    public void level(Level level) {
        this.level = level;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        if (level != null) {
            batch.begin();
            batch.setProjectionMatrix(level.camera().combined);
            for (Entity entity : level) {
                if (entity != null && powerPortFamily.matches(entity)) {
                    renderPowerPort(entity);
                }
            }
            batch.end();
        }
    }

    public void renderPowerPort(Entity powerPort) {
        DVector2 position = discretePositionMapper.get(powerPort);
        Colour colour = colourMapper.get(powerPort).colour().toColour();
        this.powerPortSprite.setColor(colour.red() / 255f, colour.green() / 255f, colour.blue() / 255f, colour.alpha() / 255f);
        powerPortSprite.setPosition((position.x() - 0.5f) * Config.TILE_SIZE, (position.y() - 0.5f) * Config.TILE_SIZE);
        powerPortSprite.setSize(32, 32);
        powerPortSprite.draw(batch);
    }
}
