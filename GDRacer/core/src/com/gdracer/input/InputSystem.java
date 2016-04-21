package com.gdracer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public abstract class InputSystem {
		
		private Input input = Gdx.input;
		
		public InputSystem(){
		}
		
		public boolean keyHeld(int key){
			return input.isKeyPressed(key);
		}
		
		public boolean keyDown(int key){
			return input.isKeyJustPressed(key);
		}
		
		public boolean touched(){
			return input.isTouched();
		}
		
		public boolean buttonPressed(int btn){
			return input.isButtonPressed(btn);
		}
		
		public boolean touched(int indx){
			return input.isTouched(indx);
		}
		
		public boolean cursorCatched(){
			return input.isCursorCatched();
		}
		
		public boolean catchBackKey(){
			return input.isCatchBackKey();
		}
		
		public abstract void listen();

}
