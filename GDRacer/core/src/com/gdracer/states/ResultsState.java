package com.gdracer.states;

import com.gdracer.handlers.GameStateManager;

public class ResultsState extends GameState {

	public ResultsState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		System.out.println("You win!!!!");
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		gsm.setState(States.MAIN_MENU);
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
