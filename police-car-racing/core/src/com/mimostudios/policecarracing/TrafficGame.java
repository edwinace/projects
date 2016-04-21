package com.mimostudios.policecarracing;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

@SuppressWarnings("static-access")
public class TrafficGame extends Table {
	private InfiniteScrollBg backgroundRoad;
	private Array<EnemyCar> enemyCars;
	private long lastCarTime = 0;
	public final float lane2 = 390;
	public final float lane1 = 240;
	public final float lane0 = 90;
	public PlayerCar playerCar;
	private MyGame game;
	private GameOverScreen gameOverScreen;
	private String status="";
	private static String GAME_OVER = "gameover";
	
	Label score;
	Label hits;
	Label gameOver;
	AnimatedImage explosion;
	
	public TrafficGame(MyGame game) {
		this.game = game;
		setBounds(0, 0, this.game.WIDTH, this.game.HEIGHT);
		setClip(true);
		backgroundRoad = new InfiniteScrollBg(getWidth(),getHeight());
		addActor(backgroundRoad);
		playerCar = new PlayerCar(this);
		addActor(playerCar);
		enemyCars = new Array<EnemyCar>();
		
		score = new Label("Score: " + game.score, game.skin, "small");
		score.setX(10);
		score.setY(760);
		addActor(score);
		hits = new Label("Hits: " + playerCar.getHitPoints() + "/"+playerCar.getMAX_HP(), game.skin, "small");
		hits.setX(game.WIDTH-100);
		hits.setY(760);
		addActor(hits);
		gameOver = new Label("", game.skin, "big");
		gameOver.setX(100);
		gameOver.setY(400);
		addActor(gameOver);
		explosion = new AnimatedImage(Assets.carExplosion);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		// spawn every 2.5 seconds
		//if ((TimeUtils.nanoTime() - lastCarTime > 2500000000f) && !isGameOver() ) spawnCar();
		  if ((TimeUtils.nanoTime() - lastCarTime > 1000000000f) && !isGameOver() ) spawnCar();		
		
		if (!isGameOver()) game.score++;
		
		Iterator<EnemyCar> iter = enemyCars.iterator();
		while (iter.hasNext()) {
			EnemyCar enemyCar = iter.next();
			if (enemyCar.getBounds().x + enemyCar.getWidth() <= 0) {
				iter.remove();
				removeActor(enemyCar);
			}
			if (enemyCar.getBounds().overlaps(playerCar.getBounds())) {
				Assets.hitSound.play();
                iter.remove();
                if (enemyCar.getX() > playerCar.getX()) {
                    if (enemyCar.getY() > playerCar.getY()) enemyCar.crash(true, true);
                    else enemyCar.crash(true, false);
                }
                else {
                    if (enemyCar.getY() > playerCar.getY()) enemyCar.crash(false, true);
                    else enemyCar.crash(false, false);
                }
                //Remove hp or give score?
                if (enemyCar.getName().equals("BADCAR")){
                	game.score = game.score + 100;
                } else {
                    playerCar.setHitPoints(playerCar.getHitPoints()-1);
                    Gdx.input.vibrate(50);
                }
                if (playerCar.getHitPoints()<=0) {
                	status=GAME_OVER;
                	Gdx.input.vibrate(50);
        			Assets.expSound.play();
                }
			}
		}
		
		score.setText("Score: " + game.score);
		hits.setText("Hits: " + playerCar.getHitPoints() + "/"+playerCar.getMAX_HP());
		if (isGameOver()) {
			playerCar.setVisible(false);
    		gameOver.setText("GAME OVER!");
    		explosion.setX(playerCar.getX());
    		explosion.setY(playerCar.getY());
    		explosion.setWidth(playerCar.getWidth());
    		explosion.setHeight(playerCar.getHeight());
    		addActor(explosion);
		}
				
		if (Gdx.input.isTouched() && status.equals(GAME_OVER)) {
        	gameOverScreen = new GameOverScreen(game);
    		game.setScreen(gameOverScreen);
		}
		
		
	}

	private void spawnCar() {
		int lane = MathUtils.random(0, 2);
		float xPos = 0;
		if (lane == 0) xPos = lane0;
		if (lane == 1) xPos = lane1;
		if (lane == 2) xPos = lane2;
		EnemyCar enemyCar = new EnemyCar(xPos, getHeight());
		enemyCars.add(enemyCar);
		addActor(enemyCar);
		lastCarTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Color.WHITE);
		super.draw(batch, parentAlpha);
	}
	
	private boolean isGameOver() {
		return status!=null && status.equals(GAME_OVER);
	}
}
