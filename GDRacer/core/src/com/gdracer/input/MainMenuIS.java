package com.gdracer.input;

import com.badlogic.gdx.Input.Keys;
import com.gdracer.states.MainMenuState;
import com.gdracer.states.States;

public class MainMenuIS extends InputSystem {
	
	MainMenuState state;
	
	public MainMenuIS(MainMenuState state) {
		this.state = state;
	}

	@Override
	public void listen() {
		
		if(keyDown(Keys.ENTER)) playGame();
			
	}
	
	public void playGame(){
		state.getGSM().setState(States.PLAY);
	}

}
