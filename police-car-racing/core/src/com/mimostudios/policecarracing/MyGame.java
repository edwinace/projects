package com.mimostudios.policecarracing;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mimostudios.policecarracing.StartAppAds;

public class MyGame extends Game {
	public final static int WIDTH = 480;//Gdx.graphics.getWidth();//480;
	public final static int HEIGHT = 800;//Gdx.graphics.getHeight();//800;
	private MenuScreen menuScreen;
	
	SpriteBatch batch;
	//BitmapFont font;
	long score;
	int numberOfGames=1; //games played
	final StartAppAds startAppAds; //interface for startapp integration
	final ShareInterface shareInterface; //interface for startapp integration
	BitmapFont fontSmall;
	BitmapFont fontMedium;
	BitmapFont fontBig;
	Skin skin;
	
	 
	public MyGame() {
		this.startAppAds = null;
		this.shareInterface = null;
	}

	public MyGame(StartAppAds startAppAds, ShareInterface shareInterface) {
		this.startAppAds = startAppAds;
		this.shareInterface = shareInterface;
	}	 

	@Override
	public void create() {
		Assets.load();
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("gooddog-plain.regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		fontSmall = generator.generateFont(parameter);
		fontSmall.setColor(Color.WHITE);
		parameter.size = 32;
		fontMedium = generator.generateFont(parameter);
		fontMedium.setColor(Color.WHITE);
		parameter.size = 72;
		fontBig = generator.generateFont(parameter);
		fontBig.setColor(Color.WHITE);		
		generator.dispose();
		
		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", this.fontMedium);
		skin.add("small", this.fontSmall);
		skin.add("big", this.fontBig);

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		
		//Configure a LabelStyle and name it "default".
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		skin.add("default", labelStyle);
		//Configure a LabelStyle and name it "small".
		LabelStyle labelStyleSmall = new LabelStyle();
		labelStyleSmall.font = skin.getFont("small");
		skin.add("small", labelStyleSmall);
		//Configure a LabelStyle and name it "big".
		LabelStyle labelStyleBig = new LabelStyle();
		labelStyleBig.font = skin.getFont("big");
		skin.add("big", labelStyleBig);		
		
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		Assets.dispose();
		menuScreen.dispose();
	}

	public StartAppAds getStartAppAds() {
		return startAppAds;
	}

}
