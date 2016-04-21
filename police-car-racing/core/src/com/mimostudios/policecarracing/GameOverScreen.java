package com.mimostudios.policecarracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

@SuppressWarnings("static-access")
public class GameOverScreen implements Screen {

	final MyGame game;
	Stage stage;
	SpriteBatch batch;

	public GameOverScreen(final MyGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		stage = new Stage(new StretchViewport(game.WIDTH, game.HEIGHT));
		Gdx.input.setInputProcessor(stage);

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		table.align(Align.top);
		table.setBackground(new TextureRegionDrawable(new TextureRegion(Assets.mainMenuBackground)));
		stage.addActor(table);

		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final Label intro1 = new Label("\n\nGame Over!!!\n", game.skin);
		final Label intro2 = new Label("\nYour score: " + game.score + "\n\n\n\n\n\n", game.skin);
		final Label lf = new Label("\n\n", game.skin);
		final TextButton buttonPlay = new TextButton("  Try again  ", game.skin);
		final TextButton buttonOtherApps = new TextButton("  Other apps  ", game.skin);
		final TextButton buttonShare = new TextButton("  Share  ", game.skin);
		table.add(intro1);
		table.row();
		table.add(intro2);
		table.row();
		table.add(buttonPlay);
		table.add(lf);
		table.row();
		table.add(buttonOtherApps);
		table.add(lf);
		table.row();
		table.add(buttonShare);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		buttonPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (game.numberOfGames%3==0 && game.startAppAds!=null) {
					game.startAppAds.showInterstitial();
				} else {
					game.setScreen(new GameScreen(game));
				}
				game.numberOfGames++;
				//dispose();
			}
		});
		
		buttonOtherApps.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.shareInterface.openOtherApp();
			}
		});		
		
		buttonShare.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.shareInterface.shareScore("Police Car Racing", "I scored " + game.score +" on Police Car Racing. Try to beat me! " + 
						"https://play.google.com/store/apps/details?id=com.mimostudios.policecarracing");
			}
		});		
    }

    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
    }

    @Override
	public void resize(int width, int height) {
    	stage.getViewport().update(width, height, true);
	}
 
	@Override
	public void show() {
		Assets.backgroundMusic.stop();
		Gdx.input.setInputProcessor(stage);
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
		stage.dispose();
	}
}
