package com.gdracer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.gdracer.game.Game;
import com.gdracer.states.PlayState;
import com.gdracer.tiled.StageMap;

public class Player extends Entity {
	
	Texture car;
	float speed = 5;
	float veerSpeed = 20;
	float acceleration = 5;
	
	public static final float START_POS_Y = 64;
	
	int score = 0;
	
	boolean blocked, finished, tactile;

	public Player(PlayState stage) {
		super(stage);
	}

	@Override
	public void init() {
		
		car = new Texture("Player/car.png");
		x = Game.V_WIDTH / 2;
		y = START_POS_Y;
		w = car.getWidth();
		h = car.getHeight();
		
		tactile = true;
		
		setName("Player");
		
	}

	@Override
	public void update(float delta) {
		
        if(!blocked || !tactile)
        	move();
		
		MapObject collisionObject = checkCollision();
		
		if(collisionObject != null) handleCollision(collisionObject);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		
		batch.draw(car, getX(), getY());
		
	}

	@Override
	public void dispose() {
		car.dispose();
		
	}
	
	private MapObject checkCollision(){
		
		MapObjects objects = stage.getMap().getObjects("objects"),
				   obstacles = stage.getMap().getObjects("obstacles"),
				   current = objects;
		
		int limit = current.getCount();
		
		for(int i = 0; i < limit; i++, limit = current.getCount()){
			
			MapObject mo = current.get(i);
			
			float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
				
			if (current.equals(objects)) {

				Entity e = (Entity) mo;
				
				if(!e.equals(this)){
					x1 = e.x;
					y1 = e.y;
					x2 = x1 + e.w; 
					y2 = y1 + e.h;
				}

			}

			else {
				
				Rectangle rect = ((RectangleMapObject)mo).getRectangle();
				x1 = rect.x;
				y1 = rect.y;
				x2 = x1 + rect.width;
				y2 = y1 + rect.height;

			}
			
			if (x + w > x1 && x < x2 && y + h > y1 && y < y2)
				return mo;
			
			if(current.equals(objects) && i + 1 == limit){
				
				i = -1;
				current = obstacles;
				
			}
			
			
		}
		
		return null;
	}
	
	private void handleCollision(MapObject collisionObject) {
		
		if(collisionObject instanceof Coin)
			coinCollision((Coin)collisionObject);
		else if(collisionObject instanceof RectangleMapObject)
			obstacleCollision((RectangleMapObject)collisionObject);
		
	}

	private void coinCollision(Coin c) {
		
		score += c.value;
		
		stage.getEntities().removeEntity(c);
		stage.getMap().removeObject("objects", c);
		
		c.dispose();
		
	}
	
	private void obstacleCollision(RectangleMapObject collisionObject) {
		
		blocked = true;
		
	}
	
    private void move() {
		
		y += speed;
		
		if(y >= StageMap.MAP_HEIGHT){
			y = START_POS_Y;
			finished = true;
		}
		
		Vector3 up = stage.getCamera().project(new Vector3(x, y, 0));
		System.out.println(up.y);
		
		blocked = false;
		
	}

	public void moveRight(){
		if(x + w + veerSpeed >= stage.eastLimit) return;
		x += veerSpeed;
	}
	
	public void moveLeft(){
	   if(x - veerSpeed < stage.westLimit) return;
	   x -= veerSpeed;
	}
	
	public void accelerate(){
		speed += acceleration;
	}
	
	public void decelerate(){
//		if(speed - acceleration < 0) return;
		speed -= acceleration;
	}
	
	public boolean isBlocked(){
		return blocked;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void toggleTactility(boolean t){
		//LOL el nombre
		tactile = t;
	}

}
