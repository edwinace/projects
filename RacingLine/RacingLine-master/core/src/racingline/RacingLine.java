package racingline;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class RacingLine extends Game {
	
	private float masterVolume;
	private int resolutionWidth;
	private int resolutionHeight;
	
	@Override
	public void create () {
		KeyBinder inputProcessor = new KeyBinder();
		Gdx.input.setInputProcessor(inputProcessor);
		if (!Gdx.files.external("RacingLine/controls.con").exists()) {
			Gdx.app.log("INFO","No control scheme found, creating default");
			Gdx.files.external("RacingLine/controls.con").write(Gdx.files.internal("defaults/controls.con").read(), false);
		}
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		resolutionWidth = 1066;
		resolutionHeight = 600;
		masterVolume = 0.1f;
		setScreen(new RaceScreen(this, Gdx.files.internal("tracks/enduro.trk"), 4));
	}

	@Override
	public void render () {
		super.render();
	}
	
	public int getresolutionWidth() { return resolutionWidth; }
	public int getresolutionHeight() { return resolutionHeight; }
	public float getMasterVolume() { return masterVolume; }
}
