package com.gdracer.states;

import com.gdracer.handlers.GameStateManager;
import com.gdracer.input.MainMenuIS;

public class MainMenuState extends GameState {

	public MainMenuState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		System.out.println("Main Menu Desu.");
		setInputSystem(new MainMenuIS(this));
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
