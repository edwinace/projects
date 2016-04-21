package com.gdracer.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdracer.game.Game;
import com.gdracer.handlers.GameStateManager;
import com.gdracer.input.InputSystem;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
    protected Game game;
	
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
    protected OrthographicCamera hudCam;
	
	InputSystem iSystem;
	
	public GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.getGame();
		batch = game.getBatch();
		camera = game.getCamera();
		hudCam = game.getHudCam();
	}
	
	public abstract void init();
	public abstract void update(float delta);
	public abstract void render();
	public abstract void dispose();
	
	public InputSystem getInputSystem(){
		return iSystem;
	}
	
	public void setInputSystem(InputSystem iSystem){
		this.iSystem = iSystem;
	}
	
	public GameStateManager getGSM(){
		return gsm;
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
	
	public Game getGame() {
		return game;
	}

}
