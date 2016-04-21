package com.mimostudios.policecarracing;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EnemyCar extends Actor {
	private Rectangle bounds = new Rectangle();
	private TextureRegion carTexture;
	
	public EnemyCar(float x, float y) {
		setWidth(85);
		setHeight(160);
		setPosition(x - getWidth()/2, y);
		
		int rnd = MathUtils.random(0, 2);
		if (rnd == 0) {carTexture=Assets.eCar1;setName("CAR");}
		if (rnd == 1) {carTexture=Assets.eCar2;setName("CAR");}
		if (rnd == 2) {carTexture=Assets.badCar;setName("BADCAR");}
		
		//addAction(moveTo(getX(), -getHeight(), MathUtils.random(4.0f, 6.0f)));
		addAction(moveTo(getX(), -getHeight(), MathUtils.random(1.2f, 1.8f)));
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		updateBounds();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);		
		batch.draw(carTexture, getX(), getY(), getWidth()/2, getHeight()/2, getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	private void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());
	}
	
	public void crash(boolean front, boolean above) {
		clearActions();
		addAction(fadeOut(1f));
		if (front && above) addAction(sequence(parallel(rotateTo(360, 1.5f), moveTo(200, 200, 1.5f)), removeActor()));
		if (front && !above) addAction(sequence(parallel(rotateTo(-360, 1.5f), moveTo(200, -200, 1.5f)), removeActor()));
		if (!front && above) addAction(sequence(parallel(rotateTo(-360, 1.5f), moveTo(-200, 200, 1.5f)), removeActor()));
		if (!front && !above) addAction(sequence(parallel(rotateTo(360, 1.5f), moveTo(-200, -200, 1.5f)), removeActor()));
	}

	public Rectangle getBounds() {
		return bounds;
	}
}
