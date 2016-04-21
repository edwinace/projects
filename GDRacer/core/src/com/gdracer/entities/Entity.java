package com.gdracer.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.gdracer.handlers.AnimationManager;
import com.gdracer.states.PlayState;

public abstract class Entity extends MapObject {
	
	protected float x, y, w, h;
	
	protected AnimationManager animations;	
	
	protected PlayState stage;

	public Entity(PlayState stage) {
		this.stage = stage;
		this.animations = new AnimationManager();
	}
	
	public abstract void init();
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

}
