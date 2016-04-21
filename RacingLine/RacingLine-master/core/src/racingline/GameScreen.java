package racingline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import gamelogic.Car;
import gamelogic.Constants;
import gamelogic.Track;

import java.awt.geom.Line2D;

public class GameScreen implements Screen {
	
	RacingLine mainGame;

	//Asset Tools
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	
	//Assets
	private Texture carTexture;
	private TextureRegion carSprite;
	private Texture trackTexture;
	private Pixmap trackData;
	private Sound idle;
	private Sound engine;
	
	//Game Loop
	private int axelWidth;
	private int carHeight;
	private int carWidth;
	private long idleID;
	private long engineLowID;
	private long engineMidID;
	private int trueWheelbase;
	private int wheelWidth;
	private int wheelHeight;
	private float[] frontLeft;
	private float[] frontRight;
	private float[] backLeft;
	private float[] backRight;
	private int wheelsOnGrass;
	private float speedo;
	private float engineSpeed;
	
	//Game Logic 
	private Car car;
	private boolean accel;
	private float timer;
	private float bestTime;
	private float timeDiff;
	private float tempTimer;
	private float prevX;
	private float prevY;
	private Line2D movement;
	private int lapCheck;
	private Track track;
	private Line2D[] checkpoints = {new Line2D.Float(881,15,894,237),
			new Line2D.Float(1300,289,1430,111),
			new Line2D.Float(1330,720,1326,897),
			new Line2D.Float(1063,443,1123,246),
			new Line2D.Float(675,765,501,603),
			new Line2D.Float(279,474,43,478),
			new Line2D.Float(421,45,457,259),
			new Line2D.Float(745,24,752,235),
			};
	
	public GameScreen(RacingLine mainGame) {
		this.mainGame = mainGame;
		
		Gdx.app.log("PREP","Setting up tools");
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 1600, 900);
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();	
		Gdx.app.log("PREP","Setting up tools - DONE");
		
		Gdx.app.log("PREP","Loading assets");
		carTexture = new Texture(Gdx.files.internal("porsche_smaller.png"));
		axelWidth = 24;
		carHeight = 30;
		carWidth = 64;
		carSprite = new TextureRegion(carTexture, 0, 0, carWidth, carHeight);
		trackTexture = new Texture(Gdx.files.internal("track.png"));
		trackTexture.getTextureData().prepare();
		trackData = trackTexture.getTextureData().consumePixmap();
		idle = Gdx.audio.newSound(Gdx.files.internal("idle.wav"));
		engine = Gdx.audio.newSound(Gdx.files.internal("engine2.wav"));	
		Gdx.app.log("PREP","Loading assets - DONE");
		
		Gdx.app.log("PREP","Setting up game");
		car = new Car(800,800); 
		trueWheelbase = (int) (Constants.WHEELBASE-6);
		wheelWidth = 10;
		wheelHeight = 6;
		frontLeft = new float[2];
		frontRight = new float[2];
		backLeft = new float[2];
		backRight = new float[2];
		track = new Track(checkpoints);
		prevX = car.getX();
		prevY = car.getY();
		
		timer = System.nanoTime();
		bestTime = 999;
		
		idleID = idle.loop();
		engineLowID = engine.loop();
		engineMidID = engine.loop();
		setVolume(idle, idleID, 0);
		setVolume(engine, engineLowID, 0);
		setVolume(engine, engineMidID, 0);	
		Gdx.app.log("PREP","Setting up game - DONE");
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		
		accel = false;
		wheelsOnGrass = 0;
		
		handleKeyboardInput();
		gameLogic();
		calculateDrawings();
		calculateSounds();
		draw();	
	}
	
	private void handleKeyboardInput() {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
		      Gdx.app.exit();
		}
		if(Gdx.input.isKeyJustPressed(Keys.F)) {
			
			if (Gdx.graphics.isFullscreen()) {
				Gdx.app.log("META", "Changing to windowed");
				Gdx.graphics.setDisplayMode(mainGame.getresolutionWidth(), mainGame.getresolutionHeight(), false);
			}
			else {
				Gdx.app.log("META", "Changing to fullscreen");
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
			}
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			car.turn(Car.TURN_LEFT);
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			car.turn(Car.TURN_RIGHT);
		}
		else {
			car.turn(Car.STRAIGHT);
		}
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			car.accelerate(Car.ACCELERATE);
			accel = true;
		}
		else if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			car.accelerate(Car.BRAKE);
		}
		else {
			car.accelerate(Car.NO_POWER);
		}
	}
	
	private void gameLogic() {
		car.grass(wheelsOnGrass);
		car.move(Gdx.graphics.getDeltaTime());
		
		movement = new Line2D.Float(prevX, 900-prevY, car.getX(), 900-car.getY());
		prevX = car.getX();
		prevY = car.getY();
		lapCheck = track.carMove(movement);
		if (lapCheck == Track.LAP) {
			tempTimer = (System.nanoTime()-timer)/1000000000;
			timeDiff = tempTimer - bestTime;
			if(tempTimer < bestTime) {
				bestTime = tempTimer;
			}
			timer = System.nanoTime();
		}
	}
	
	private void calculateDrawings() {
		speedo = (float) ((Constants.TOP_TURN_SPEED+100)/car.getSpeed());
		speedo = (float) (Math.PI/speedo);
		speedo += Math.PI-Math.PI/4;
		
		frontLeft[0] = (float) ( car.getX()+(trueWheelbase/2)*Math.cos(car.getAngle())-(axelWidth/2)*Math.sin(car.getAngle()) );
		frontLeft[1] = (float) ( car.getY()+(trueWheelbase/2)*Math.sin(car.getAngle())+(axelWidth/2)*Math.cos(car.getAngle()) );
		
		frontRight[0] = (float) ( car.getX()+(trueWheelbase/2)*Math.cos(car.getAngle())+(axelWidth/2)*Math.sin(car.getAngle()) );
		frontRight[1] = (float) ( car.getY()+(trueWheelbase/2)*Math.sin(car.getAngle())-(axelWidth/2)*Math.cos(car.getAngle()) );
		
		backLeft[0] = (float) ( car.getX()-(trueWheelbase/2)*Math.cos(car.getAngle())-(axelWidth/2)*Math.sin(car.getAngle()) );
		backLeft[1] = (float) ( car.getY()-(trueWheelbase/2)*Math.sin(car.getAngle())+(axelWidth/2)*Math.cos(car.getAngle()) );
		
		backRight[0] = (float) ( car.getX()-(trueWheelbase/2)*Math.cos(car.getAngle())+(axelWidth/2)*Math.sin(car.getAngle()) );
		backRight[1] = (float) ( car.getY()-(trueWheelbase/2)*Math.sin(car.getAngle())-(axelWidth/2)*Math.cos(car.getAngle()) );
		
		if (((trackData.getPixel((int) frontLeft[0], 900-(int) frontLeft[1])>>8)&0xFF)<=Constants.GRASS_UPPER 
				&& ((trackData.getPixel((int) frontLeft[0], 900-(int) frontLeft[1])>>8)&0xFF)>=Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) frontRight[0], 900-(int) frontRight[1])>>8)&0xFF)<=Constants.GRASS_UPPER 
				&& ((trackData.getPixel((int) frontRight[0], 900-(int) frontRight[1])>>8)&0xFF)>=Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) backLeft[0], 900-(int) backLeft[1])>>8)&0xFF)<=Constants.GRASS_UPPER 
				&& ((trackData.getPixel((int) backLeft[0], 900-(int) backLeft[1])>>8)&0xFF)>=Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) backRight[0], 900-(int) backRight[1])>>8)&0xFF)<=Constants.GRASS_UPPER 
				&& ((trackData.getPixel((int) backRight[0], 900-(int) backRight[1])>>8)&0xFF)>=Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
	}
	
	private void calculateSounds() {
		engineSpeed = (float) (car.getSpeed()/(Constants.TOP_TURN_SPEED+300));
		idle.setPitch(idleID, engineSpeed+1);
		engine.setPitch(engineLowID, engineSpeed);
		setVolume(idle, idleID, 0.25f-engineSpeed*0.25f);
		setVolume(engine, engineLowID, 0.8f*engineSpeed+0.1f);
		if (!accel) {
			engineSpeed=0;
		}
		engine.setPitch(engineMidID, (engineSpeed+1.5f));
		setVolume(engine, engineMidID, engineSpeed*0.5f);
		
		
	}
	
	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		//Draw the track, times
		batch.setProjectionMatrix(camera.combined);
		batch.begin(); {
			batch.disableBlending();
			batch.draw(trackTexture, 0, 0);
			batch.enableBlending();
			
			font.draw(batch, String.format("%.3f", bestTime), 200, 50);
			font.draw(batch, String.format("%.3f", timeDiff), 200, 70);
			font.draw(batch, ""+Gdx.graphics.getFramesPerSecond(), 200, 30);
		}
		batch.end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled); {
			shapeRenderer.identity();
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.translate(car.getX(), car.getY(), 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle()));
			shapeRenderer.rect(-wheelWidth/2-Constants.WHEELBASE/2, -wheelHeight/2+axelWidth/2, wheelWidth, wheelHeight); //back left wheel
			shapeRenderer.rect(-wheelWidth/2-Constants.WHEELBASE/2, -wheelHeight/2-axelWidth/2, wheelWidth, wheelHeight); //back right wheel
			
			//front left wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontLeft[0], frontLeft[1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle()+car.getTurnAngle()));
			shapeRenderer.rect(-wheelWidth/2, -wheelHeight/2, wheelWidth, wheelHeight);
	
			//front right wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontRight[0], frontRight[1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle()+car.getTurnAngle()));
			shapeRenderer.rect(-wheelWidth/2, -wheelHeight/2, wheelWidth, wheelHeight);
			
			//speedometer
			shapeRenderer.identity();
			shapeRenderer.setColor(1,1,1,0.5f);
			shapeRenderer.circle(100, 100, 50);
			shapeRenderer.setColor(0,0,0,1);
			shapeRenderer.scale(4, 4, 0);
			shapeRenderer.line(25, 25, 25+ (float) (10*Math.cos(speedo)),25+ (float) (-10*Math.sin(speedo)));
		}
		shapeRenderer.end();
		
		batch.begin(); {
			batch.draw(carSprite, car.getX()-(carWidth/2), car.getY()-(carHeight/2),
				carWidth/2,  carHeight/2, carWidth, carHeight,
				1, 1, (float) Math.toDegrees(car.getAngle()));
		}
		batch.end();
	}
	
	private void setVolume(Sound sound, long id, float volume) {
		sound.setVolume(id, volume*mainGame.getMasterVolume());
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		carTexture.dispose();
		trackTexture.dispose();
		engine.dispose();
		idle.dispose();
		trackData.dispose();
		
		batch.dispose();
		shapeRenderer.dispose();
        font.dispose();
	}

}
