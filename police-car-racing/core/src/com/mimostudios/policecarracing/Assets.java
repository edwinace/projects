package com.mimostudios.policecarracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static TextureAtlas atlas;
	public static TextureRegion car, eCar1, eCar2, badCar;
	public static TextureRegion road;
	public static TextureRegion tryAgain, share;
	public static Texture mainMenuBackground;
	public static Animation carExplosion;
	public static Sound expSound, hitSound;
	public static Music backgroundMusic;

	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("images.atlas"));
		eCar1 = atlas.findRegion("car1");		
		car = atlas.findRegion("car2");
		eCar2 = atlas.findRegion("car3");
		badCar = atlas.findRegion("car4");
		road = atlas.findRegion("road");
		mainMenuBackground = new Texture(Gdx.files.internal("mainMenuBackground.png"));
		tryAgain = atlas.findRegion("tryAgain");
		share = atlas.findRegion("share");
		carExplosion = new Animation(0.2f, 	atlas.findRegion("exp1"),
											atlas.findRegion("exp2"),
											atlas.findRegion("exp3"),
											atlas.findRegion("exp4"),
											atlas.findRegion("exp5"));
		expSound = Gdx.audio.newSound(Gdx.files.internal("exp.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("whatYouWant.mp3"));
		backgroundMusic.setVolume(0.5f);
		backgroundMusic.setLooping(true);
	}

	public static void dispose() {
		atlas.dispose();
	}
}
