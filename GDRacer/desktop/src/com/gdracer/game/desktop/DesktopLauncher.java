package com.gdracer.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdracer.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), config);
		
		config.width = (int)Game.V_WIDTH;
		config.height = (int)Game.V_HEIGHT;
		config.title = Game.windowTitle;
		config.y = 50;
		config.resizable = false;
		
	}
}
