package com.gdracer.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteSheet {
	
	TextureRegion sheet[][];
	int framesX, framesY, frameCount;
	float frameWidth, frameHeight;

	public SpriteSheet(Texture spriteSheet, int framesX, int framesY) {
		this.framesX = framesX;
		this.framesY = framesY;
		this.frameWidth = spriteSheet.getWidth() / framesX;
		this.frameHeight = spriteSheet.getHeight() / framesY;
		this.frameCount = framesX * framesY;
		this.sheet = TextureRegion.split(spriteSheet, (int)frameWidth, (int)frameHeight);
	}
	
	public SpriteSheet(int framesX, int framesY) {
		this.framesX = framesX;
		this.framesY = framesY;
		this.frameCount = framesX * framesY;
		this.sheet = new TextureRegion[framesX][framesY];
	}
	
	public void addFrame(TextureRegion frame, int row, int col){
		
		if(row < 0 || row >= framesX || col < 0 || col >= framesY) return;
		
		sheet[row][col] = frame;
	}
	
	public TextureRegion getFrame(int row, int col){
		
		if(row < 0 || row >= framesX || col < 0 || col >= framesY) 
			return sheet[0][0];
		
		return sheet[row][col];
	}
	
	public int getFramesX(){
		return framesX;
	}
	
	public int getFramesY(){
		return framesY;
	}

}
