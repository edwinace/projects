package com.mimostudios.policecarracing.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mimostudios.policecarracing.MyGame;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Police Car Racing";
		cfg.width = 540;//480;
		cfg.height = 960;//800;
		
		new LwjglApplication(new MyGame(), cfg);
	}
}
