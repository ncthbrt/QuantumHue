package co.za.cuthbert.three;

import com.badlogic.gdx.utils.Array;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class LevelChanger {
    public Array<LevelChangeListener> listeners;

    public LevelChanger() {
        listeners = new Array<LevelChangeListener>();
    }

    private Level currentLevel;

    public Level currentLevel() {
        return currentLevel;
    }

    public void currentLevel(Level level) {
        this.currentLevel = level;
        for (int i = 0; i < listeners.size; i++) {
            listeners.get(i).level(level);
        }
    }

    public void addListener(LevelChangeListener listener) {
        listeners.add(listener);
    }

}
