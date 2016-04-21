package com.mimostudios.policecarracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("static-access")
public class CopyOfGameOverScreen implements Screen {

	final MyGame game;
	
	OrthographicCamera camera;
	
	Rectangle tryagain;
	Rectangle share;
	Vector3 touchPoint;
	Rectangle background;	

	public CopyOfGameOverScreen(final MyGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		tryagain = new Rectangle(150,500,128,32);
		share = new Rectangle(150,400,128,32);
		touchPoint = new Vector3();
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
		game.fontMedium.draw(game.batch, "Game over!!! ", 150, 650);
		game.fontMedium.draw(game.batch, "Your score: " + game.score, 150, 600);
		game.batch.draw(Assets.tryAgain, tryagain.x, tryagain.y);
		game.batch.draw(Assets.share, share.x, share.y);
		game.batch.end();
		
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			//System.out.println("x: " + touchPoint.x + " y: " + touchPoint.y);
			if (tryagain.contains(touchPoint.x, touchPoint.y)) {
				//System.out.println("OK!");
				if (game.numberOfGames%3==0 && game.startAppAds!=null) {
					game.startAppAds.showInterstitial();
				}
				game.numberOfGames++;
				game.setScreen(new GameScreen(game));
				dispose();
			}
			if (share.contains(touchPoint.x, touchPoint.y)) {
				game.shareInterface.shareScore("Police Car Racing", "I scored " + 
						game.score + " on Police Car Racing! Try to beat me! https://play.google.com/store/apps/details?id=com.mimostudios.policecarracing");
			}			
		}		
 /*
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
*/
    }

    @Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
		Assets.backgroundMusic.stop();
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
