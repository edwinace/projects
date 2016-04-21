package com.mimostudios.policecarracing;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerCar extends Actor {
	private TrafficGame trafficGame;
	private Rectangle bounds = new Rectangle();
	private int lane;
	private int hitPoints;
	private static int MAX_HP=2;
	float stateTime;
	
	public PlayerCar(TrafficGame trafficGame) {
		this.trafficGame = trafficGame;
		setWidth(85);
		setHeight(160);
		lane = 1;
		setPosition(trafficGame.lane1 - getWidth()/2, 100);
		//setColor(Color.YELLOW);
		setHitPoints(getMAX_HP());
		this.stateTime = 0;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		updateBounds();
		stateTime += delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);		
		batch.draw(Assets.car, getX(), getY(), getWidth()/2, getHeight()/2, getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	private void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());
	}

	public void tryMoveLeft() {
		if ((getActions().size == 0) && (lane != 0)) moveToLane(lane-1);
	}

	public void tryMoveRight() {
		if ((getActions().size == 0) && (lane != 2)) moveToLane(lane+1);
	}
	
	private void moveToLane(int lane) {
		this.lane = lane;
		
		switch (lane) {
			case 0:
				addAction(moveTo(trafficGame.lane0 - getWidth()/2, getY(), 0.3f));
				break;
			case 1:
				addAction(moveTo(trafficGame.lane1 - getWidth()/2, getY(), 0.3f));
				break;
			case 2:
				addAction(moveTo(trafficGame.lane2 - getWidth()/2, getY(), 0.3f));
				break;
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public int getMAX_HP() {
		return MAX_HP;
	}
}
