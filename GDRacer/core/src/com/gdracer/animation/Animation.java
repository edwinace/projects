package com.gdracer.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	
	SpriteSheet sheet;
	
	boolean playing = false;
	
	int frameIndex = 0;
	
	float switchFrame;
	
	float ticks = 0;

	public Animation(SpriteSheet sheet, float switchFrame) {
		this.sheet = sheet;
		this.switchFrame = switchFrame;
	}
	
	public void play(){
		playing = true;
	}
	
	public void pause(){
		playing = false;
	}
	
	public void stop(){
		frameIndex = 0;
		ticks = 0;
		pause();
	}
	
	public void animate(){
		
		if(playing){
			
			ticks++;
			
			if(ticks >= switchFrame){
				
				ticks = 0;
				frameIndex++;
				
				if(frameIndex == sheet.getFramesY())
					frameIndex = 0;
				
			}
			
		}
		
	}
	
	public TextureRegion getCurrentFrame(){
		return sheet.getFrame(0, frameIndex);
	}

}
