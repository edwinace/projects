package com.shaggyhamster.animal.racing.manager;

import android.graphics.Color;
import com.shaggyhamster.animal.racing.util.AnimalType;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class ResourcesManager {

    private static final ResourcesManager INSTANCE = new ResourcesManager();
    private BaseGameActivity activity;
    private Engine engine;
    private Camera camera;
    private VertexBufferObjectManager vertexBufferObjectManager;

    private BitmapTextureAtlas splashTextureAtlas, menuFontTextureAtlas, gameFontTextureAtlas, greenFontTextureAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas, optionsTextureAtlas, aboutTextureAtlas, endGameTextureAtlas,
            recordTextureAtlas, gameTypeTextureAtlas, gameTextureAtlas, gameBackgroundTextureAtlas;

    // Game
    private ITextureRegion backgroundGameTextureRegion;
    private ITiledTextureRegion horseTiledTextureRegion;

    // Splash
    private ITextureRegion splashTextureRegion;

    // Menu
    private ITextureRegion buttonAboutTextureRegion, buttonExitTextureRegion, buttonNewGameTextureRegion,
            buttonOptionsTextureRegion, menuBackgroundTextureRegion, buttonHighScoreTextureRegion;

    // Help
    private ITextureRegion aboutBackgroundTextureRegion;

    // Options
    private ITextureRegion optionsBackgroundTextureRegion, optionsTextureRegion;

    // EndGame
    private ITextureRegion endGameBackgroundTextureRegion;

    // HighScore
    private ITextureRegion recordBackgroundTextureRegion;

    // Game Type
    private ITextureRegion backgroundGameTypeTextureRegion, starGoldTextureRegion, starWhiteTextureRegion,
            awesomeTextureRegion, lockTextureRegion, playButtonTextureRegion;


    private List<Sound> winSoundList, loseSoundList, halfWinSoundList;
    private Sound startGameSound, goodClickSound, wrongClickSound;
    private Font whiteFont, blackFont, greenFont;

    public static void prepareManager(Engine engine, BaseGameActivity activity, Camera camera, VertexBufferObjectManager vertexBufferObjectManager) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vertexBufferObjectManager = vertexBufferObjectManager;
    }

    public void loadOptionsResources() {
        loadOptionsGraphics();
    }

    public void loadAboutResources() {
        loadAboutGraphics();
    }

    public void loadMainMenuResources() {
        loadMainMenuGraphics();
        loadWhiteFont();
        loadBlackFont();
        loadGreenFont();
    }

    public void loadGameResources() {
        loadGameGraphics();
        loadGameMusic();
        loadEndGameResources();
    }

    public void loadEndGameResources() {
        loadEndGameGraphics();
    }

    public void loadRecordResources() {
        loadHighScoreGraphics();
    }

    public void loadGameTypeResources() {
        loadGameTypeGraphics();
    }

    private void loadGameTypeGraphics() {
        if (gameTypeTextureAtlas != null) {
            gameTypeTextureAtlas.load();
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/gametype/");
        gameTypeTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        backgroundGameTypeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "background.png");

        starGoldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "goldStar.png");
        starWhiteTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "whiteStar.png");
        awesomeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "awesome.png");
        lockTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "lock.png");
        playButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTypeTextureAtlas, activity, "playButton.png");

        try {
            gameTypeTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
            gameTypeTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScoreGraphics() {
        if (recordTextureAtlas != null) {
            recordTextureAtlas.load();
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/highscore/");

        recordTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 512, TextureOptions.BILINEAR);
        recordBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(recordTextureAtlas, activity, "background.png");

        try {
            recordTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            recordTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }


    }

    private void loadEndGameGraphics() {
        if (endGameTextureAtlas != null) {
            endGameTextureAtlas.load();
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/endgame/");
        endGameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 512, TextureOptions.BILINEAR);

        endGameBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(endGameTextureAtlas, activity, "background.png");

        try {
            endGameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            endGameTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    private void loadGameMusic() {

        SoundFactory.setAssetBasePath("mfx/other/");
        winSoundList = new ArrayList<Sound>();
        loseSoundList = new ArrayList<Sound>();
        halfWinSoundList = new ArrayList<Sound>();
        try {
            winSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "win.ogg"));

            loseSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "lose.ogg"));
            loseSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "lose1.ogg"));

            halfWinSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "halfwin0.ogg"));
            halfWinSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "halfwin1.ogg"));
            halfWinSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "halfwin2.ogg"));

            startGameSound = SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "go.ogg");
            goodClickSound = SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "goodClick.ogg");
            wrongClickSound = SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "wrongClick.ogg");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadAboutGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/about/");
        aboutTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        aboutBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(aboutTextureAtlas, activity, "background.jpg");

        try {
            aboutTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            aboutTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    private void loadOptionsGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/options/");
        optionsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        optionsBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "background.jpg");
        optionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "options.png");

        try {
            optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            optionsTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }


    private void loadGameGraphics() {

        if (gameTextureAtlas != null) {
            gameTextureAtlas.load();
            gameBackgroundTextureAtlas.load();
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        gameBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        backgroundGameTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundTextureAtlas, activity, "background.png");

        horseTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "horse2.png", 5, 3);

        try {
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            gameTextureAtlas.load();

            gameBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            gameBackgroundTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }

    }

    private void loadMainMenuGraphics() {

        if (menuTextureAtlas != null) {
            menuTextureAtlas.load();
            return;
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        menuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.jpg");
        buttonAboutTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_help.png");
        buttonExitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_exit.png");
        buttonNewGameTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_new.png");
        buttonOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_options.png");
        buttonHighScoreTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_high.png");

        try {
            menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            menuTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.jpg", 0, 0);
        splashTextureAtlas.load();
    }

    public void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    private void loadGreenFont() {
        if (greenFontTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        greenFontTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        greenFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), greenFontTextureAtlas, activity.getAssets(), "font1.ttf", 50, true, Color.GREEN, 2, Color.GREEN);
        greenFontTextureAtlas.load();
        greenFont.load();
    }

    private void loadBlackFont() {
        if (gameFontTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        gameFontTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        blackFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), gameFontTextureAtlas, activity.getAssets(), "font1.ttf", 50, true, Color.BLACK, 2, Color.BLACK);
        gameFontTextureAtlas.load();
        blackFont.load();
    }


    private void loadWhiteFont() {
        if (menuFontTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        menuFontTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        whiteFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), menuFontTextureAtlas, activity.getAssets(), "font2.ttf", 50, true, Color.WHITE, 2, Color.WHITE);
        menuFontTextureAtlas.load();
        whiteFont.load();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splashTextureRegion = null;
    }

    public void unloadOptionsTextures() {
        optionsTextureAtlas.unload();
    }

    public void unloadAboutTextures() {
        aboutTextureAtlas.unload();
    }

    public void unloadEndGameTextures() {
        endGameTextureAtlas.unload();
    }

    public void unloadRecordsTextures() {
        recordTextureAtlas.unload();
    }

    public void unloadGameTypeTextures() {
        gameTypeTextureAtlas.unload();
    }

    public void unloadGameTextures() {
        gameTextureAtlas.unload();
        gameBackgroundTextureAtlas.unload();
    }

    public void unloadMenuTextures() {
        menuTextureAtlas.unload();
    }


    public static ResourcesManager getInstance() {
        return INSTANCE;
    }

    public BaseGameActivity getActivity() {
        return activity;
    }

    public Engine getEngine() {
        return engine;
    }

    public Camera getCamera() {
        return camera;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager() {
        return vertexBufferObjectManager;
    }

    public ITextureRegion getSplashTextureRegion() {
        return splashTextureRegion;
    }

    public ITextureRegion getButtonAboutTextureRegion() {
        return buttonAboutTextureRegion;
    }

    public ITextureRegion getButtonExitTextureRegion() {
        return buttonExitTextureRegion;
    }

    public ITextureRegion getButtonNewGameTextureRegion() {
        return buttonNewGameTextureRegion;
    }

    public ITextureRegion getButtonOptionsTextureRegion() {
        return buttonOptionsTextureRegion;
    }

    public ITextureRegion getMenuBackgroundTextureRegion() {
        return menuBackgroundTextureRegion;
    }

    public ITextureRegion getAboutBackgroundTextureRegion() {
        return aboutBackgroundTextureRegion;
    }

    public ITextureRegion getOptionsBackgroundTextureRegion() {
        return optionsBackgroundTextureRegion;
    }

    public ITextureRegion getOptionsTextureRegion() {
        return optionsTextureRegion;
    }

    public Font getWhiteFont() {
        return whiteFont;
    }

    public Font getBlackFont() {
        return blackFont;
    }

    public Font getGreenFont() {
        return greenFont;
    }

    public ITextureRegion getEndGameBackgroundTextureRegion() {
        return endGameBackgroundTextureRegion;
    }

    public ITextureRegion getRecordBackgroundTextureRegion() {
        return recordBackgroundTextureRegion;
    }

    public ITextureRegion getButtonHighScoreTextureRegion() {
        return buttonHighScoreTextureRegion;
    }

    public ITextureRegion getBackgroundGameTypeTextureRegion() {
        return backgroundGameTypeTextureRegion;
    }

    public Sound getWinSound() {
        Collections.shuffle(winSoundList);
        return winSoundList.get(0);
    }

    public Sound getLoseSound() {
        Collections.shuffle(loseSoundList);
        return loseSoundList.get(0);
    }

    public Sound getHalfWinSound() {
        Collections.shuffle(halfWinSoundList);
        return halfWinSoundList.get(0);
    }

    public ITextureRegion getBackgroundGameTextureRegion() {
        return backgroundGameTextureRegion;
    }

    public ITextureRegion getStarGoldTextureRegion() {
        return starGoldTextureRegion;
    }

    public ITextureRegion getStarWhiteTextureRegion() {
        return starWhiteTextureRegion;
    }

    public ITextureRegion getAwesomeTextureRegion() {
        return awesomeTextureRegion;
    }

    public ITextureRegion getLockTextureRegion() {
        return lockTextureRegion;
    }

    public ITextureRegion getPlayButtonTextureRegion() {
        return playButtonTextureRegion;
    }

    public Sound getStartGameSound() {
        return startGameSound;
    }

    public Sound getGoodClickSound() {
        return goodClickSound;
    }

    public Sound getWrongClickSound() {
        return wrongClickSound;
    }

    public ITiledTextureRegion getAnimalTiledTextureRegionFor(AnimalType animalType) {
        switch (animalType) {
            case HORSE:
                return horseTiledTextureRegion;
            default:
                throw new UnsupportedOperationException();
        }

    }
}
