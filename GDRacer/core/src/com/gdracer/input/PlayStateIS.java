package com.gdracer.input;

import com.badlogic.gdx.Input.Keys;
import com.gdracer.entities.Player;
import com.gdracer.states.PlayState;

public class PlayStateIS extends InputSystem {
	
	PlayState state;
	Player player;
	
	int layerToggler = 1, tactilityToggler = 1;

	public PlayStateIS(PlayState state) {
		this.state = state;
		player = state.player;
	}

	@Override
	public void listen() {
		
		if(keyHeld(Keys.D))
			player.moveRight();
		if(keyHeld(Keys.A))
			player.moveLeft();
		if(keyDown(Keys.SPACE))
			player.accelerate();
		if(keyDown(Keys.BACKSPACE))
			player.decelerate();
		if(keyDown(Keys.H))
			hideObjectLayer();
		if(keyDown(Keys.T))
			toggleTact();
			
	}
	
	void hideObjectLayer(){
		state.getMap().toggleLayer("objects", layerToggler % 2 == 0);
		layerToggler++;
	}
	
	void toggleTact(){
		player.toggleTactility(tactilityToggler % 2 == 0);
		tactilityToggler++;
	}

}
