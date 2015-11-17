package com.deepwallgames.quantumhue.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.RenderLayers;
import com.deepwallgames.quantumhue.RenderLayers.LevelEditorLayer;
import com.deepwallgames.quantumhue.components.ColourComponent;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.EntityTypeComponent;
import com.deepwallgames.quantumhue.components.SwitchComponent;
import com.deepwallgames.quantumhue.value_objects.Colour;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class SwitchablePartRenderer extends EntitySystem implements LevelChangeListener {


    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);
    private static final ComponentMapper<ColourComponent> colourMapper = ComponentMapper.getFor(ColourComponent.class);
    private static final ComponentMapper<SwitchComponent> switchComponentMapper = ComponentMapper.getFor(SwitchComponent.class);
    private static final ComponentMapper<EntityTypeComponent> entityTypeComponentMapper= ComponentMapper.getFor(EntityTypeComponent.class);
    private Level level;

    private final float blur=1.5f;
    private final SpriteBatch batch;


    private final EntityType[] switchableParts={EntityType.POWER_SOURCE, EntityType.GROUND};
    private final String[] switchablePartSpriteNames={"power_source","ground"};
    private final Sprite[] sprites= new Sprite[switchableParts.length];
    private final RenderLayers layers;
    public SwitchablePartRenderer(SpriteBatch batch, TextureAtlas atlas, RenderLayers layers){
        this.layers=layers;
        priority = 4;
        this.batch = batch;
        for (int i=0; i<switchablePartSpriteNames.length; ++i){
            sprites[i]=atlas.createSprite(switchablePartSpriteNames[i]);
        }
    }

    public SwitchablePartRenderer(SpriteBatch batch, TextureAtlas atlas){
        this.layers=null;
        priority = 4;
        this.batch = batch;
        for (int i=0; i<switchablePartSpriteNames.length; ++i){
            sprites[i]=atlas.createSprite(switchablePartSpriteNames[i]);
        }
    }

    public void level(Level level) {
        this.level = level;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime){

        if (level != null) {
            batch.begin();
            {
                if(layers!=null) {
                    layers.get(LevelEditorLayer.BASE).begin();
                }

                {
                    batch.setProjectionMatrix(level.camera().combined);
                    for (int i = 0; i < sprites.length; ++i) {
                        for (Entity entity : level) {
                            if (entity != null && switchableParts[i].family.matches(entity) &&
                                entityTypeComponentMapper.get(entity).tileType()==switchableParts[i]) {
                                boolean on=true;
                                if(switchComponentMapper.has(entity)){
                                    on=switchComponentMapper.get(entity).on;
                                }
                                renderPart(entity, i,on);
                            }
                        }
                    }
                    batch.flush();
                }
                if(layers!=null) {
                    layers.get(LevelEditorLayer.BASE).end();
                    layers.get(LevelEditorLayer.GLOW_EFFECT).begin();
                }
                {
                    batch.setProjectionMatrix(level.camera().combined);
                    for (int i = 0; i < sprites.length; ++i) {
                        for (Entity entity : level) {
                            if (entity != null && switchableParts[i].family.matches(entity) &&
                                entityTypeComponentMapper.get(entity).tileType()==switchableParts[i]) {
                                boolean on=false;
                                if(switchComponentMapper.has(entity)){
                                    on=switchComponentMapper.get(entity).on;
                                }
                                if(on) {
                                    renderPart(entity, i, true);
                                }
                            }
                        }
                    }
                    batch.flush();
                }
                if(layers!=null) {
                    layers.get(LevelEditorLayer.GLOW_EFFECT).end();
                }
            }
            batch.end();
        }
    }



    public void renderPart(Entity part, int i,boolean on) {
        DVector2 position = discretePositionMapper.get(part);
        Colour colour = colourMapper.get(part).colour();
        if(on) {
            this.sprites[i].setColor(colour.red() / 255f, colour.green() / 255f, colour.blue() / 255f, colour.alpha() / 255f);
        }else{
            this.sprites[i].setColor(colour.red() / 255f, colour.green() / 255f, colour.blue() / 255f, colour.alpha() / 255f*0.5f);
        }
        sprites[i].setPosition((position.x() - 0.5f) * Config.TILE_SIZE, (position.y() - 0.5f) * Config.TILE_SIZE);
        sprites[i].setSize(32, 32);
        sprites[i].draw(batch);
    }
}

