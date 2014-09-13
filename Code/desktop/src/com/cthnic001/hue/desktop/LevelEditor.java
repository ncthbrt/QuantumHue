package com.cthnic001.hue.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cthnic001.hue.QuantumHue;
import org.lwjgl.util.Dimension;

import java.awt.*;

/**
 * Created by CTHNI_000 on 2014-08-26.
 */
public class LevelEditor {
    public static void main(String[] args){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen=true;
        config.width=width;
        config.height=height;
        new LwjglApplication(new QuantumHue(true), config);
    }
}
