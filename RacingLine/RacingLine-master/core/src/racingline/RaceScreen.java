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

public class RaceScreen implements Screen {

	RacingLine mainGame;
	private int players;

	// Asset Tools
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	// Assets
	private Texture carTexture;
	private TextureRegion[] carSprites;
	private Texture trackTexture;
	private Pixmap trackData;
	private Sound idle;
	private Sound engine;

	// Game Loop
	private int axelWidth;
	private int carHeight;
	private int carWidth;
	private long[] idleID;
	private long[] engineLowID;
	private long[] engineMidID;
	private int trueWheelbase;
	private int wheelWidth;
	private int wheelHeight;
	private float[][] frontLeft;
	private float[][] frontRight;
	private float[][] backLeft;
	private float[][] backRight;
	private int[] wheelsOnGrass;
	private float[] engineSpeed;
	private int[][] controls;

	// Game Logic
	private Car[] car;
	private boolean[] accel;
	private float[] timer;
	private float[] bestTime;
	private float[] timeDiff;
	private float[] tempTimer;
	private float[] timeCurrent;
	private float[] prevX;
	private float[] prevY;
	private Line2D[] movement;
	private int[] lapCheck;
	private Track[] track;
	private String trackName;
	private Line2D[] checkpoints;
	private boolean[] invalidLap;

	public RaceScreen(RacingLine mainGame, FileHandle trackFile, int players) {
		this.mainGame = mainGame;
		this.players = players;

		track = new Track[players];
		car = new Car[players];
		prevX = new float[players];
		prevY = new float[players];
		frontLeft = new float[players][2];
		frontRight = new float[players][2];
		backLeft = new float[players][2];
		backRight = new float[players][2];
		timer = new float[players];
		bestTime = new float[players];
		invalidLap = new boolean[players];
		idleID = new long[players];
		engineLowID = new long[players];
		engineMidID = new long[players];
		accel = new boolean[players];
		wheelsOnGrass = new int[players];
		timeCurrent = new float[players];
		movement = new Line2D[players];
		lapCheck = new int[players];
		engineSpeed = new float[players];
		timeDiff = new float[players];
		tempTimer = new float[players];
		controls = new int[players][4];
		carSprites = new TextureRegion[players];

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
		for (int i = 0; i < players; i++) {
			track[i] = new Track(checkpoints);
		}
		trackScanner.close();
		Gdx.app.log("INFO", "Loading map - DONE");

		Gdx.app.log("INFO", "Loading assets");
		carTexture = new Texture(Gdx.files.internal("textures/car.png"));
		axelWidth = 22;
		carHeight = 22;
		carWidth = 60;
		
		for (int i = 0; i < players; i++) {
			carSprites[i] = new TextureRegion(carTexture, 0, 64*i, carWidth, carHeight);
		}
		idle = Gdx.audio.newSound(Gdx.files.internal("sounds/idle.wav"));
		engine = Gdx.audio.newSound(Gdx.files.internal("sounds/engine2.wav"));
		Gdx.app.log("INFO", "Loading assets - DONE");

		Gdx.app.log("INFO", "Setting up game");
		for (int i = 0; i < players; i++) {
			car[i] = new Car(800, 717 + i * 32);

			prevX[i] = car[i].getX();
			prevY[i] = car[i].getY();
		}
		trueWheelbase = (int) (Constants.WHEELBASE - 6);
		wheelWidth = 10;
		wheelHeight = 6;

		for (int i = 0; i < players; i++) {
			timer[i] = System.nanoTime();
			bestTime[i] = 999;
			invalidLap[i] = false;

			idleID[i] = idle.loop();
			setVolume(idle, idleID[i], 0);
			engineLowID[i] = engine.loop();
			setVolume(engine, engineLowID[i], 0);
			engineMidID[i] = engine.loop();
			setVolume(engine, engineMidID[i], 0);
		}
		
		Scanner controlsScanner = new Scanner(Gdx.files.external("RacingLine/controls.con").read());
		for (int i = 0; i < players; i++) {
			Scanner playerControlScanner = new Scanner(controlsScanner.nextLine());
			for (int j = 0; j < 4; j++) {
				controls[i][j] = playerControlScanner.nextInt();
			}
			playerControlScanner.close();
		}
		controlsScanner.close();
		Gdx.app.log("INFO", "Setting up game - DONE");
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		for (int i = 0; i < players; i++) {
			accel[i] = false;
			wheelsOnGrass[i] = 0;
			timeCurrent[i] = (System.nanoTime() - timer[i]) / 1000000000;
		}
		handleKeyboardInput();
		gameLogic();
		calculateSounds();
		draw();
	}

	private void handleKeyboardInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
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
		// P1
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			car[0].turn(Car.TURN_LEFT);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			car[0].turn(Car.TURN_RIGHT);
		} else {
			car[0].turn(Car.STRAIGHT);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			car[0].accelerate(Car.ACCELERATE);
			accel[0] = true;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			car[0].accelerate(Car.BRAKE);
		} else {
			car[0].accelerate(Car.NO_POWER);
		}
		// P2
		if (Gdx.input.isKeyPressed(Keys.A)) {
			car[1].turn(Car.TURN_LEFT);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			car[1].turn(Car.TURN_RIGHT);
		} else {
			car[1].turn(Car.STRAIGHT);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			car[1].accelerate(Car.ACCELERATE);
			accel[1] = true;
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			car[1].accelerate(Car.BRAKE);
		} else {
			car[1].accelerate(Car.NO_POWER);
		}
		
		for (int i = 0; i < players; i++) {
			if (Gdx.input.isKeyPressed(controls[i][0])) {
				car[i].accelerate(Car.ACCELERATE);
				accel[i] = true;
			} else if (Gdx.input.isKeyPressed(controls[i][2])) {
				car[i].accelerate(Car.BRAKE);
			} else {
				car[i].accelerate(Car.NO_POWER);
			} 
			
			if (Gdx.input.isKeyPressed(controls[i][3])) {
				car[i].turn(Car.TURN_LEFT);
			} else if (Gdx.input.isKeyPressed(controls[i][1])) {
				car[i].turn(Car.TURN_RIGHT);
			} else {
				car[i].turn(Car.STRAIGHT);
			}
		}
	}

	private void gameLogic() {
		for (int i = 0; i < players; i++) {
			car[i].move(Gdx.graphics.getDeltaTime());

			frontLeft[i][0] = (float) (car[i].getX() + (trueWheelbase / 2) * Math.cos(car[i].getAngle())
					- (axelWidth / 2) * Math.sin(car[i].getAngle()));
			frontLeft[i][1] = (float) (car[i].getY() + (trueWheelbase / 2) * Math.sin(car[i].getAngle())
					+ (axelWidth / 2) * Math.cos(car[i].getAngle()));

			frontRight[i][0] = (float) (car[i].getX() + (trueWheelbase / 2) * Math.cos(car[i].getAngle())
					+ (axelWidth / 2) * Math.sin(car[i].getAngle()));
			frontRight[i][1] = (float) (car[i].getY() + (trueWheelbase / 2) * Math.sin(car[i].getAngle())
					- (axelWidth / 2) * Math.cos(car[i].getAngle()));

			backLeft[i][0] = (float) (car[i].getX() - (trueWheelbase / 2) * Math.cos(car[i].getAngle())
					- (axelWidth / 2) * Math.sin(car[i].getAngle()));
			backLeft[i][1] = (float) (car[i].getY() - (trueWheelbase / 2) * Math.sin(car[i].getAngle())
					+ (axelWidth / 2) * Math.cos(car[i].getAngle()));

			backRight[i][0] = (float) (car[i].getX() - (trueWheelbase / 2) * Math.cos(car[i].getAngle())
					+ (axelWidth / 2) * Math.sin(car[i].getAngle()));
			backRight[i][1] = (float) (car[i].getY() - (trueWheelbase / 2) * Math.sin(car[i].getAngle())
					- (axelWidth / 2) * Math.cos(car[i].getAngle()));

			if (((trackData.getPixel((int) frontLeft[i][0], 900 - (int) frontLeft[i][1]) >> 8)
					& 0xFF) <= Constants.GRASS_UPPER
					&& ((trackData.getPixel((int) frontLeft[i][0], 900 - (int) frontLeft[i][1]) >> 8)
							& 0xFF) >= Constants.GRASS_LOWER) {
				wheelsOnGrass[i]++;
			}
			if (((trackData.getPixel((int) frontRight[i][0], 900 - (int) frontRight[i][1]) >> 8)
					& 0xFF) <= Constants.GRASS_UPPER
					&& ((trackData.getPixel((int) frontRight[i][0], 900 - (int) frontRight[i][1]) >> 8)
							& 0xFF) >= Constants.GRASS_LOWER) {
				wheelsOnGrass[i]++;
			}
			if (((trackData.getPixel((int) backLeft[i][0], 900 - (int) backLeft[i][1]) >> 8)
					& 0xFF) <= Constants.GRASS_UPPER
					&& ((trackData.getPixel((int) backLeft[i][0], 900 - (int) backLeft[i][1]) >> 8)
							& 0xFF) >= Constants.GRASS_LOWER) {
				wheelsOnGrass[i]++;
			}
			if (((trackData.getPixel((int) backRight[i][0], 900 - (int) backRight[i][1]) >> 8)
					& 0xFF) <= Constants.GRASS_UPPER
					&& ((trackData.getPixel((int) backRight[i][0], 900 - (int) backRight[i][1]) >> 8)
							& 0xFF) >= Constants.GRASS_LOWER) {
				wheelsOnGrass[i]++;
			}

			car[i].grass(wheelsOnGrass[i]);

			movement[i] = new Line2D.Float(prevX[i], 900 - prevY[i], car[i].getX(), 900 - car[i].getY());
			prevX[i] = car[i].getX();
			prevY[i] = car[i].getY();
			lapCheck[i] = track[i].carMove(movement[i]);
			if (lapCheck[i] == Track.LAP) {
				tempTimer[i] = (System.nanoTime() - timer[i]) / 1000000000;
				timeDiff[i] = tempTimer[i] - bestTime[i];
				if (tempTimer[i] < bestTime[i] && !invalidLap[i]) {
					bestTime[i] = tempTimer[i];
				}
				timer[i] = System.nanoTime();
				invalidLap[i] = false;
			} else if (lapCheck[i] == Track.CUT) {
				invalidLap[i] = true;
			}
		}
	}

	private void calculateSounds() {
		for (int i = 0; i < players; i++) {
			engineSpeed[i] = (float) (car[i].getSpeed() / (Constants.TOP_TURN_SPEED + 300));
			idle.setPitch(idleID[i], engineSpeed[i] + 1);
			engine.setPitch(engineLowID[i], engineSpeed[i]);
			setVolume(idle, idleID[i], 0.25f - engineSpeed[i] * 0.25f);
			setVolume(engine, engineLowID[i], 0.8f * engineSpeed[i] + 0.1f);
			if (!accel[i]) {
				engineSpeed[i] = 0;
			}
			engine.setPitch(engineMidID[i], (engineSpeed[i] + 1.5f));
			setVolume(engine, engineMidID[i], engineSpeed[i] * 0.5f);
		}
	}

	private void draw() {
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
			for (int i = 0; i < players; i++) {
				if (timeCurrent[i] > 2) {
					font.setColor(1, 1, 1, 1);
					font.draw(batch, String.format(" %.3f", timeCurrent[i]), 50 + 100 * i, 840);
				} else {
					if (timeDiff[i] >= 0) {
						font.setColor(1, 0, 0, 1);
						font.draw(batch, String.format("+%.3f", timeDiff[i]), 50 + 100 * i, 840);
					} else {
						font.setColor(0, 1, 0, 1);
						font.draw(batch, String.format("-%.3f", Math.abs(timeDiff[i])), 50 + 100 * i, 840);
					}
				}
				font.setColor(1, 1, 1, 1);
				font.draw(batch, String.format(" %.3f", bestTime[i]), 50 + 100 * i, 860);
				if (invalidLap[i]) {
					font.setColor(1, 0, 0, 1);
					font.draw(batch, " Invalid Lap", 50 + 100 * i, 820);
				}
			}
		}
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		for (int i = 0; i < players; i++) {
			shapeRenderer.identity();
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.translate(car[i].getX(), car[i].getY(), 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car[i].getAngle()));
			shapeRenderer.rect(-wheelWidth / 2 - Constants.WHEELBASE / 2, -wheelHeight / 2 + axelWidth / 2, wheelWidth,
					wheelHeight); // back left wheel
			shapeRenderer.rect(-wheelWidth / 2 - Constants.WHEELBASE / 2, -wheelHeight / 2 - axelWidth / 2, wheelWidth,
					wheelHeight); // back right wheel

			// front left wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontLeft[i][0], frontLeft[i][1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car[i].getAngle() + car[i].getTurnAngle()));
			shapeRenderer.rect(-wheelWidth / 2, -wheelHeight / 2, wheelWidth, wheelHeight);

			// front right wheel
			shapeRenderer.identity();
			shapeRenderer.translate(frontRight[i][0], frontRight[i][1], 0);
			shapeRenderer.rotate(0, 0, 1, (float) Math.toDegrees(car[i].getAngle() + car[i].getTurnAngle()));
			shapeRenderer.rect(-wheelWidth / 2, -wheelHeight / 2, wheelWidth, wheelHeight);

		}
		shapeRenderer.end();

		batch.begin();
		for (int i = 0; i < players; i++) {
			batch.draw(carSprites[i], car[i].getX() - (carWidth / 2), car[i].getY() - (carHeight / 2), carWidth / 2,
					carHeight / 2, carWidth, carHeight, 1, 1, (float) Math.toDegrees(car[i].getAngle()));
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
