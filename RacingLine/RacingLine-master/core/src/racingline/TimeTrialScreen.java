package racingline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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
import java.util.Scanner;

public class TimeTrialScreen implements Screen {

	RacingLine mainGame;

	// Asset Tools
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	// Assets
	private Texture carTexture;
	private TextureRegion carSprite;
	private Texture trackTexture;
	private Pixmap trackData;
	private Sound idle;
	private Sound engine;

	// Game Loop
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

	// Game Logic
	private Car car;
	private boolean accel;
	private float timer;
	private float bestTime;
	private float timeDiff;
	private float tempTimer;
	private float timeCurrent;
	private float prevX;
	private float prevY;
	private Line2D movement;
	private int lapCheck;
	private Track track;
	private String trackName;
	private Line2D[] checkpoints;
	private boolean invalidLap;

	public TimeTrialScreen(RacingLine mainGame, FileHandle trackFile) {
		this.mainGame = mainGame;

		Gdx.app.log("INFO", "Setting up tools");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		Gdx.app.log("INFO", "Setting up tools - DONE");

		Gdx.app.log("INFO", "Loading map");
		Scanner trackScanner = new Scanner(trackFile.read());
		trackName = trackScanner.nextLine();
		trackTexture = new Texture(Gdx.files.internal("tracks/" + trackName + ".png"));
		trackTexture.getTextureData().prepare();
		trackData = trackTexture.getTextureData().consumePixmap();
		int checkpointCounter = Integer.parseInt(trackScanner.nextLine());
		checkpoints = new Line2D[checkpointCounter];
		for (int i = 0; i < checkpointCounter; i++) {
			Scanner checkpointScanner = new Scanner(trackScanner.nextLine());
			int x1 = checkpointScanner.nextInt();
			int y1 = checkpointScanner.nextInt();
			int x2 = checkpointScanner.nextInt();
			int y2 = checkpointScanner.nextInt();
			checkpoints[i] = new Line2D.Float(x1, y1, x2, y2);
			checkpointScanner.close();
		}
		track = new Track(checkpoints);
		trackScanner.close();
		Gdx.app.log("INFO", "Loading map - DONE");

		Gdx.app.log("INFO", "Loading assets");
		carTexture = new Texture(Gdx.files.internal("textures/f1_small.png"));
		axelWidth = 19;
		carHeight = 19;
		carWidth = 64;
		carSprite = new TextureRegion(carTexture, 0, 0, carWidth, carHeight);
		idle = Gdx.audio.newSound(Gdx.files.internal("sounds/idle.wav"));
		engine = Gdx.audio.newSound(Gdx.files.internal("sounds/engine2.wav"));
		Gdx.app.log("INFO", "Loading assets - DONE");

		Gdx.app.log("INFO", "Setting up game");
		car = new Car(800, 800);
		trueWheelbase = (int) (Constants.WHEELBASE - 6);
		wheelWidth = 10;
		wheelHeight = 6;
		frontLeft = new float[2];
		frontRight = new float[2];
		backLeft = new float[2];
		backRight = new float[2];
		prevX = car.getX();
		prevY = car.getY();

		timer = System.nanoTime();
		FileHandle recordFile = Gdx.files.external("RacingLine/"+trackName+".txt");
		if (recordFile.exists()) {
			bestTime = new Scanner(recordFile.read()).nextFloat();
		} else {
			bestTime = 999;
		}
		invalidLap = false;
		
		idleID = idle.loop();
		engineLowID = engine.loop();
		engineMidID = engine.loop();
		setVolume(idle, idleID, 0);
		setVolume(engine, engineLowID, 0);
		setVolume(engine, engineMidID, 0);
		Gdx.app.log("INFO", "Setting up game - DONE");
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		accel = false;
		wheelsOnGrass = 0;
		timeCurrent = (System.nanoTime()-timer)/1000000000;

		handleKeyboardInput();
		gameLogic();
		calculateSounds();
		draw();
	}

	private void handleKeyboardInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			FileHandle lapRecord = Gdx.files.external("RacingLine/" + trackName + ".txt");
			lapRecord.writeString(String.format("%.3f", bestTime), false);
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyJustPressed(Keys.F)) {

			if (Gdx.graphics.isFullscreen()) {
				Gdx.app.log("INFO", "Changing to windowed");
				Gdx.graphics.setDisplayMode(mainGame.getresolutionWidth(), mainGame.getresolutionHeight(), false);
			} else {
				Gdx.app.log("INFO", "Changing to fullscreen");
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width,
						Gdx.graphics.getDesktopDisplayMode().height, true);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			car.turn(Car.TURN_LEFT);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			car.turn(Car.TURN_RIGHT);
		} else {
			car.turn(Car.STRAIGHT);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			car.accelerate(Car.ACCELERATE);
			accel = true;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			car.accelerate(Car.BRAKE);
		} else {
			car.accelerate(Car.NO_POWER);
		}
	}

	private void gameLogic() {

		car.move(Gdx.graphics.getDeltaTime());

		frontLeft[0] = (float) (car.getX() + (trueWheelbase / 2) * Math.cos(car.getAngle())
				- (axelWidth / 2) * Math.sin(car.getAngle()));
		frontLeft[1] = (float) (car.getY() + (trueWheelbase / 2) * Math.sin(car.getAngle())
				+ (axelWidth / 2) * Math.cos(car.getAngle()));

		frontRight[0] = (float) (car.getX() + (trueWheelbase / 2) * Math.cos(car.getAngle())
				+ (axelWidth / 2) * Math.sin(car.getAngle()));
		frontRight[1] = (float) (car.getY() + (trueWheelbase / 2) * Math.sin(car.getAngle())
				- (axelWidth / 2) * Math.cos(car.getAngle()));

		backLeft[0] = (float) (car.getX() - (trueWheelbase / 2) * Math.cos(car.getAngle())
				- (axelWidth / 2) * Math.sin(car.getAngle()));
		backLeft[1] = (float) (car.getY() - (trueWheelbase / 2) * Math.sin(car.getAngle())
				+ (axelWidth / 2) * Math.cos(car.getAngle()));

		backRight[0] = (float) (car.getX() - (trueWheelbase / 2) * Math.cos(car.getAngle())
				+ (axelWidth / 2) * Math.sin(car.getAngle()));
		backRight[1] = (float) (car.getY() - (trueWheelbase / 2) * Math.sin(car.getAngle())
				- (axelWidth / 2) * Math.cos(car.getAngle()));

		if (((trackData.getPixel((int) frontLeft[0], 900 - (int) frontLeft[1]) >> 8) & 0xFF) <= Constants.GRASS_UPPER
				&& ((trackData.getPixel((int) frontLeft[0], 900 - (int) frontLeft[1]) >> 8)
						& 0xFF) >= Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) frontRight[0], 900 - (int) frontRight[1]) >> 8) & 0xFF) <= Constants.GRASS_UPPER
				&& ((trackData.getPixel((int) frontRight[0], 900 - (int) frontRight[1]) >> 8)
						& 0xFF) >= Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) backLeft[0], 900 - (int) backLeft[1]) >> 8) & 0xFF) <= Constants.GRASS_UPPER
				&& ((trackData.getPixel((int) backLeft[0], 900 - (int) backLeft[1]) >> 8)
						& 0xFF) >= Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}
		if (((trackData.getPixel((int) backRight[0], 900 - (int) backRight[1]) >> 8) & 0xFF) <= Constants.GRASS_UPPER
				&& ((trackData.getPixel((int) backRight[0], 900 - (int) backRight[1]) >> 8)
						& 0xFF) >= Constants.GRASS_LOWER) {
			wheelsOnGrass++;
		}

		car.grass(wheelsOnGrass);

		movement = new Line2D.Float(prevX, 900 - prevY, car.getX(), 900 - car.getY());
		prevX = car.getX();
		prevY = car.getY();
		lapCheck = track.carMove(movement);
		if (lapCheck == Track.LAP) {
			tempTimer = (System.nanoTime() - timer) / 1000000000;
			timeDiff = tempTimer - bestTime;
			if (tempTimer < bestTime && !invalidLap) {
				bestTime = tempTimer;
			}
			timer = System.nanoTime();
			invalidLap = false;
		} else if (lapCheck == Track.CUT) {
			invalidLap = true;
		}
	}

	private void calculateSounds() {
		engineSpeed = (float) (car.getSpeed() / (Constants.TOP_TURN_SPEED + 300));
		idle.setPitch(idleID, engineSpeed + 1);
		engine.setPitch(engineLowID, engineSpeed);
		setVolume(idle, idleID, 0.25f - engineSpeed * 0.25f);
		setVolume(engine, engineLowID, 0.8f * engineSpeed + 0.1f);
		if (!accel) {
			engineSpeed = 0;
		}
		engine.setPitch(engineMidID, (engineSpeed + 1.5f));
		setVolume(engine, engineMidID, engineSpeed * 0.5f);
	}

	private void draw() {
		speedo = (float) ((Constants.TOP_TURN_SPEED + 100) / car.getSpeed());
		speedo = (float) (Math.PI / speedo);
		speedo += Math.PI - Math.PI / 4;

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		// Draw the track, times
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		{
			batch.disableBlending();
			batch.draw(trackTexture, 0, 0);
			batch.enableBlending();
			if (timeCurrent > 2) {
				font.setColor(1, 1, 1, 1);
				font.draw(batch, String.format(" %.3f", timeCurrent), 200, 50);
			} else {
				if (timeDiff >= 0) {
					font.setColor(1, 0, 0, 1);
					font.draw(batch, String.format("+%.3f", timeDiff), 200, 50);
				} else {
					font.setColor(0, 1, 0, 1);
					font.draw(batch, String.format("-%.3f", Math.abs(timeDiff)), 200, 50);
				}
			}
				font.setColor(1, 1, 1, 1);
				font.draw(batch, String.format(" %.3f", bestTime), 200, 70);
				if (invalidLap) {
					font.setColor(1,0,0,1);
					font.draw(batch, "Invalid Lap", 200, 30);
				}
		}
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		{
			shapeRenderer.identity();
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.translate(car.getX(), car.getY(), 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle()));
			shapeRenderer.rect(-wheelWidth / 2 - Constants.WHEELBASE / 2, -wheelHeight / 2 + axelWidth / 2, wheelWidth,
					wheelHeight); // back left wheel
			shapeRenderer.rect(-wheelWidth / 2 - Constants.WHEELBASE / 2, -wheelHeight / 2 - axelWidth / 2, wheelWidth,
					wheelHeight); // back right wheel

			// front left wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontLeft[0], frontLeft[1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle() + car.getTurnAngle()));
			shapeRenderer.rect(-wheelWidth / 2, -wheelHeight / 2, wheelWidth, wheelHeight);

			// front right wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontRight[0], frontRight[1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car.getAngle() + car.getTurnAngle()));
			shapeRenderer.rect(-wheelWidth / 2, -wheelHeight / 2, wheelWidth, wheelHeight);

			// speedometer
			shapeRenderer.identity();
			shapeRenderer.setColor(1, 1, 1, 0.5f);
			shapeRenderer.circle(100, 100, 50);
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.scale(4, 4, 0);
			shapeRenderer.line(25, 25, 25 + (float) (10 * Math.cos(speedo)), 25 + (float) (-10 * Math.sin(speedo)));
		}
		shapeRenderer.end();

		batch.begin();
		{
			batch.draw(carSprite, car.getX() - (carWidth / 2), car.getY() - (carHeight / 2), carWidth / 2,
					carHeight / 2, carWidth, carHeight, 1, 1, (float) Math.toDegrees(car.getAngle()));
		}
		batch.end();
	}

	private void setVolume(Sound sound, long id, float volume) {
		sound.setVolume(id, volume * mainGame.getMasterVolume());
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
