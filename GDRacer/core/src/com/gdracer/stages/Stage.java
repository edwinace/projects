package com.gdracer.stages;

import com.gdracer.handlers.EntityManager;

public abstract class Stage {
	
	//Stage needs: 
		//1)entityManager(Player&coins), 
		//2)Overlay
	
	EntityManager entities;
	//Overlay overlay;

	public Stage() {
		entities = new EntityManager();
		entities.init();
	}
	
	public void init(){
		entities.init();
	}
	
	public abstract void update(float delta);
	public abstract void render();
	public abstract void dispose();
	
	public void addEntity(){
//		entities.addEntity();
	}
	
	public void removeEntity(){
//		entities.removeEntity();
	}

}
