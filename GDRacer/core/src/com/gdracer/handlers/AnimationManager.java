package com.gdracer.handlers;

import java.util.HashMap;

import com.gdracer.animation.Animation;

public class AnimationManager {
	
	HashMap<String,Animation> animations;	

	public AnimationManager() {
		animations = new HashMap<String, Animation>();
	}
	
	public void addAnimation(String key, Animation a){
		animations.put(key, a);
	}
	
	public void removeAnimation(String key){
		animations.remove(key);
	}
	
	public Animation getAnimation(String key){
		return animations.get(key);
	}

}
