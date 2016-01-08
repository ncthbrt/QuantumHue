package com.deepwallgames.quantumhue.ui.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deepwallgames.quantumhue.Config;
import com.deepwallgames.quantumhue.EntityFactory;
import com.deepwallgames.quantumhue.EntityType;
import com.deepwallgames.quantumhue.ui.LevelEditor;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ToggleTool extends Tool {
    private EntityType type;

    public ToggleTool(LevelEditor editor, EntityType type) {
        super(editor);
        this.type = type;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (editor.currentLevel() != null && x > Config.TILE_SIZE && x < Gdx.graphics.getWidth() - Config.TILE_SIZE && y > Config.TILE_SIZE && y < Gdx.graphics.getHeight() - Config.TILE_SIZE) {
            Vector3 worldCoords = editor.currentLevel().camera().unproject(new Vector3(x, y, 0));
            int worldX = Math.round((worldCoords.x) / Config.TILE_SIZE);
            int worldY = Math.round((worldCoords.y) / Config.TILE_SIZE);
            Entity tile = editor.currentLevel().get(worldX, worldY);
            if (tile != null) {
                editor.currentLevel().removeTile(tile);
            } else {
                editor.currentLevel().addTile(EntityFactory.createEntity(editor.currentLevel(), worldX, worldY, type, editor.colour(),editor.rotation()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
