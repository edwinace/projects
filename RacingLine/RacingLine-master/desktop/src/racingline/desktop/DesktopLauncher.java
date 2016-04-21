package racingline.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import racingline.RacingLine;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RacingLine";
		config.height = 600;
		config.width = 1066;
		new LwjglApplication(new RacingLine(), config);
	}
}
