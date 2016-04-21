package com.gdracer.handlers;

import java.util.Stack;

import com.gdracer.game.Game;
import com.gdracer.input.InputSystem;
import com.gdracer.states.ControlsState;
import com.gdracer.states.GameOverState;
import com.gdracer.states.GameState;
import com.gdracer.states.HighScoreState;
import com.gdracer.states.MainMenuState;
import com.gdracer.states.PauseState;
import com.gdracer.states.PlayState;
import com.gdracer.states.ResultsState;
import com.gdracer.states.States;

public class GameStateManager {
	
	Stack<GameState> states;
	
	Game game;
	
	public GameStateManager(Game game){
		this.game = game;
		states = new Stack<>();
	}
	
	public GameState getState(States state){
		
		switch(state){
		
		case MAIN_MENU:
			return new MainMenuState(this);
		case PLAY:
			return new PlayState(this);
		case PAUSE:
			return new PauseState(this);
		case RESULTS:
			return new ResultsState(this);
		case CONTROLS:
			return new ControlsState(this);
		case HIGH_SCORES:
			return new HighScoreState(this);
		case GAME_OVER:
			return new GameOverState(this);
			
		}
		
		return null;
	}
	
	public void pushState(States state){
		states.push(getState(state));
		states.peek().init();
	}
	
	public void setState(States state){
		popState();
		pushState(state);
	}
	
	public void popState(){
		GameState state = states.pop();
		state.dispose();
	}
	
	private void handleInput(){
		InputSystem iSystem = states.peek().getInputSystem();
		
		if(iSystem != null)
			iSystem.listen();
	}
	
	public void update(float delta){
		handleInput();
		states.peek().update(delta);
	}
	
	public void render(){
		states.peek().render();
	}

	public Game getGame() {
		return game;
	}

}
