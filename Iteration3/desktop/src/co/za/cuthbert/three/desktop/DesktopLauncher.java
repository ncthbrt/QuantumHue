package co.za.cuthbert.three.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import co.za.cuthbert.three.Iteration3Main;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen=false;
        config.width=width;
        config.height=height;
//        config.width=1024;
//        config.height=768;
		new LwjglApplication(new Iteration3Main(), config);
	}
}
