package com.mimostudios.policecarracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("static-access")
public class CopyOfMenuScreen implements Screen {

	final MyGame game;
	
	OrthographicCamera camera;
	Rectangle background;

	public CopyOfMenuScreen(final MyGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		background = new Rectangle();
		background.x = 0;
		background.y = 0;
		background.width = game.WIDTH;
		background.height = game.HEIGHT;		
    }

    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		game.batch.begin();
		game.batch.draw(Assets.mainMenuBackground, background.x, background.y);
		game.fontMedium.draw(game.batch, "Welcome to Super Police Racing!!! ", 100, 750);
		game.fontMedium.draw(game.batch, "Tap anywhere to begin!", 100, 700);
		game.fontMedium.draw(game.batch, "Avoid BLUE and YELLOW cars!", 100, 550);
		game.fontMedium.draw(game.batch, "Hit the BLACK cars!", 100, 500);
		game.fontMedium.draw(game.batch, "Swipe to move your car", 100, 450);
		game.batch.end();
 
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}    	
    }

    @Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
	}
 
	@Override
	public void hide() {
	}
 
	@Override
	public void pause() {
	}
 
	@Override
	public void resume() {
	}
 
	@Override
	public void dispose() {
	}
}
