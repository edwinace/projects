package com.gdracer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdracer.animation.Animation;
import com.gdracer.animation.SpriteSheet;
import com.gdracer.states.PlayState;

public class Coin extends Entity {
	
	Animation currentAnimation;	
	
	public int value = 20;

	public Coin(PlayState stage) {
		super(stage);
	}
	
	public Coin(PlayState stage, float x, float y) {
		super(stage);
		setPosition(x, y);
	}

	@Override
	public void init() {
		
		SpriteSheet sheet = new SpriteSheet(1, 6);
		
		for(int i = 0; i < 6; i++){
			
			TextureRegion reg = new TextureRegion();
			
			reg.setRegion(new Texture("Coins/Coin"+(i+1)+".png"));
			
			sheet.addFrame(reg, 0, i);
		}
		
		currentAnimation = new Animation(sheet, 3);
		
		currentAnimation.play();
		
		animations.addAnimation("Main", currentAnimation);
		
	}

	@Override
	public void update(float delta) {
		
		currentAnimation.animate();
		
		w = currentAnimation.getCurrentFrame().getRegionWidth();
		h = currentAnimation.getCurrentFrame().getRegionHeight();
	}

	@Override
	public void render(SpriteBatch batch) {
		
		batch.draw(currentAnimation.getCurrentFrame(), x, y);
		
	}

	@Override
	public void dispose() {
		
	}

}
