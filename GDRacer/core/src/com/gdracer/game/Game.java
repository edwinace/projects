package com.gdracer.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdracer.handlers.GameStateManager;
import com.gdracer.states.States;

public class Game extends ApplicationAdapter {
	
	GameStateManager gsm;
	
	SpriteBatch batch;
	OrthographicCamera camera;
	OrthographicCamera hudCam;
	
	FPSLogger fps;
	
	public static final float V_WIDTH = 20*32;
	public static final float V_HEIGHT = 600;
	
	public static final String windowTitle = "GDRacer X 1.0";
	
	public static ApplicationType appType;
	
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		
		camera.viewportWidth = Game.V_WIDTH;
		camera.viewportHeight = Game.V_HEIGHT;
		
		camera.position.x = Game.V_WIDTH / 2;
		camera.position.y = Game.V_HEIGHT / 2;
		
		fps = new FPSLogger();
		
		appType = Gdx.app.getType();
	
		gsm = new GameStateManager(this);
		gsm.pushState(States.PLAY);
		
	}

	@Override
	public void render () {
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		gsm.update(Gdx.graphics.getDeltaTime());
		
	    //fps.log();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.render();
		
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public OrthographicCamera getHudCam() {
		return hudCam;
	}
	
}
