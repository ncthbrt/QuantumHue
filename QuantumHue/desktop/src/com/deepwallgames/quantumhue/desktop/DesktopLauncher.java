package com.deepwallgames.quantumhue.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deepwallgames.quantumhue.Iteration3Main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen=false;
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = (int)(gd.getDisplayMode().getWidth()*0.9f);
		int height = (int)(gd.getDisplayMode().getHeight()*0.9f);
		config.width=width;
		config.height=height;
		config.useGL30=true;
		new LwjglApplication(new Iteration3Main(), config);
	}
}
