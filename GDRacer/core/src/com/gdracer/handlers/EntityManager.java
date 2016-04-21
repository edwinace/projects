package com.gdracer.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdracer.entities.Coin;
import com.gdracer.entities.Entity;

public class EntityManager {
	
	ArrayList<Entity> entities;
	
	ArrayList<Coin> coins;

	public EntityManager() {
		entities = new ArrayList<>();
		coins = new ArrayList<>();
	}
	
	public void init(){
		for(Entity e : entities)
			e.init();
	}
	
	public void update(float delta){
		for(int i = 0; i < entities.size(); i++)
			entities.get(i).update(delta);
	}
	
	public void render(SpriteBatch batch){
		for(Entity e : entities)
			e.render(batch);
	}
	
	public void dispose(){
		for(Entity e : entities)
			e.dispose();
	}
	
	public void addEntity(Entity e){
		entities.add(e);
		addTo(e);
	}
	
	public void removeEntity(Entity e){
		entities.remove(e);
		removeFrom(e);
	}
	
    public Entity getEntity(int i){
    	return entities.get(i);
    }
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	private void addTo(Entity e){
		
		if(e instanceof Coin)
			addCoin((Coin)e);
		
	}
	
	private void removeFrom(Entity e){
		
		if(e instanceof Coin)
			removeCoin((Coin)e);
		
	}

	private void addCoin(Coin c) {
		coins.add(c);
	}
	
	private void removeCoin(Coin c){
		coins.remove(c);
	}
	
	public int getEntityCount(){
		return entities.size();
	}
	
	public int getCoinCount(){
		return coins.size();
	}

}
