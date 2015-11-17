package com.deepwallgames.quantumhue.systems;

import com.badlogic.gdx.Gdx;
import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.LevelChangeListener;
import com.deepwallgames.quantumhue.RenderLayers;
import com.deepwallgames.quantumhue.components.DVector2;
import com.deepwallgames.quantumhue.components.WireComponent;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.ColourBracket;
import com.deepwallgames.quantumhue.Level;
import com.deepwallgames.quantumhue.components.PortComponent;
import com.deepwallgames.quantumhue.value_objects.ColourVector;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    private Level level;
    private final RenderLayers layers;
    public WireRendererSystem(ShapeRenderer shapeRenderer, RenderLayers layers){
        this.layers=layers;
        priority = 4;
        this.shapeRenderer = shapeRenderer;
    }

    public WireRendererSystem(ShapeRenderer shapeRenderer){
        this.layers=null;
        priority = 4;
        this.shapeRenderer = shapeRenderer;
    }

    public void level(Level level) {
        this.level = level;
    }


    @Override
    public void update(float deltaTime) {

        if (level != null) {
            if(layers!=null) {
                layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).begin();
            }
            {
                shapeRenderer.setProjectionMatrix(level.camera().combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    for (Entity entity : level) {
                        if (entity != null && EntityType.WIRE.family.matches(entity)) {
                            processEntity(entity, deltaTime, true);
                        }
                    }
                shapeRenderer.end();
            }
            if(layers!=null) {
                layers.get(RenderLayers.LevelEditorLayer.GLOW_EFFECT).end();
                layers.get(RenderLayers.LevelEditorLayer.BASE).begin();
            }
            {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(level.camera().combined);

                for (Entity entity : level) {
                    if (entity != null && EntityType.WIRE.family.matches(entity)) {
                        processEntity(entity, deltaTime, false);
                    }
                }
                shapeRenderer.end();
            }
            if(layers!=null) {
                layers.get(RenderLayers.LevelEditorLayer.BASE).end();
            }
        }
    }


    private static final ComponentMapper<WireComponent> wireComponentMapper = ComponentMapper.getFor(WireComponent.class);
    private static final ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
    private static final ComponentMapper<DVector2> discretePositionMapper = ComponentMapper.getFor(DVector2.class);


    public void processEntity(Entity wire, float deltaTime,boolean drawOnlyColors) {

        PortComponent ports = portMapper.get(wire);
        WireComponent wireComponent = wireComponentMapper.get(wire);
        DVector2 position = discretePositionMapper.get(wire);

        int attachedPorts = 0;
        float actualLineWidth=lineWidth / level.zoom();
        Colour centreColour=DiscreteColour.ALPHA.toColour();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; j++) {
                if (j != 0 || i != 0) {
                    if (ports.portMask(3 * (j + 1) + i + 1) && position.x() + i < level.width() && position.y() + j < level.height() && position.x() + i >= 0 && position.y() + j >= 0) {
                        float lastPoint = 0;
                        ColourVector vector = wireComponent.resultantVector(PortComponent.portNumber(i, j));
                        for (int k = 0; k < vector.line.size() - 1; ++k) {
                            ColourBracket end = vector.line.get(k + 1);
                            ColourBracket start = vector.line.get(k);
                            if (start.colour.equals(DiscreteColour.ALPHA.toColour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                                if(!drawOnlyColors) {
                                    shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
                                }else{
                                    shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 0f);
                                }
                            } else {
                                if(k==0){
                                    centreColour.add(start.colour);
                                }
                                shapeRenderer.setColor(start.colour.red() / 255f, start.colour.green() / 255f, start.colour.blue() / 255f, 1f);
                            }
                            float point = Interpolation.linear.apply(0, Config.TILE_SIZE / 2f, end.position());
                            Vector2 startVec=new Vector2(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint);
                            Vector2 endVec=new Vector2(position.x() * Config.TILE_SIZE + i * point, position.y() * Config.TILE_SIZE + j * point);
                            shapeRenderer.rectLine(startVec.x,startVec.y,endVec.x,endVec.y, actualLineWidth);
                            lastPoint = point;
                        }

                        ColourBracket last = vector.line.get(vector.line.size() - 1);
                        if (last.colour.equals(DiscreteColour.ALPHA.toColour())) {//If black set colour to a grey, so as to allow players to see the line on the dark background
                            if(!drawOnlyColors) {
                                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
                            }else{
                                shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 0f);
                            }
                        } else {
                            shapeRenderer.setColor(last.colour.red() / 255f, last.colour.green() / 255f, last.colour.blue() / 255f, 1f);
                        }
                        shapeRenderer.rectLine(position.x() * Config.TILE_SIZE + i * lastPoint, position.y() * Config.TILE_SIZE + j * lastPoint, position.x() * Config.TILE_SIZE + i * (Config.TILE_SIZE / 2f), position.y() * Config.TILE_SIZE + j * (Config.TILE_SIZE / 2f), actualLineWidth);
                        ++attachedPorts;
                    }
                }

            }
        }

        if (attachedPorts == 0) {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
            shapeRenderer.circle(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE,Config.TILE_SIZE / 2,40);
        }else{
            if(centreColour.equals(DiscreteColour.ALPHA.toColour())){
                if(!drawOnlyColors){
                    shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
                }else{
                    shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0f);
                }
            }else{
                shapeRenderer.setColor(centreColour.red() / 255f, centreColour.green() / 255f, centreColour.blue() / 255f, 1f);
            }
            shapeRenderer.circle(position.x() * Config.TILE_SIZE, position.y() * Config.TILE_SIZE, actualLineWidth / 2f);
        }
    }


}

