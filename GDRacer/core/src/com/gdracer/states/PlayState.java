package com.gdracer.states;

import com.gdracer.entities.Coin;
import com.gdracer.entities.Player;
import com.gdracer.game.Game;
import com.gdracer.handlers.EntityManager;
import com.gdracer.handlers.GameStateManager;
import com.gdracer.input.PlayStateIS;
import com.gdracer.tiled.StageMap;

public class PlayState extends GameState {

	EntityManager entities;

	StageMap map;

	public Player player;
	public float westLimit = 120, eastLimit = Game.V_WIDTH - 80;

	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {

		entities = new EntityManager();

		map = new StageMap("StageMaps/MyRacingMap2.tmx", camera);

		player = new Player(this);

		entities.addEntity(player);

		generateCoins(100);

		entities.init();

		for (int i = 0; i < entities.getEntityCount(); i++)
			map.addObject("objects", entities.getEntity(i));
		
		map.setPlayer(player);

		setInputSystem(new PlayStateIS(this));
		
	}

	@Override
	public void update(float delta) {

		entities.update(delta);

		// GAME LOGIC

		if (entities.getCoinCount() == 0)
			gsm.setState(States.RESULTS);

		map.update();

	}

	@Override
	public void render() {
		
		map.render();

	}

	@Override
	public void dispose() {
		entities.dispose();
	}

	public void generateCoins(int coinCount) {
		
		for (int i = 0; i < coinCount; i++) {

			float rangeX = (eastLimit - westLimit) + 1;
			float rangeY = (StageMap.MAP_HEIGHT - 0) + 1;

			float randomPosX = ((float) Math.random() * rangeX) + westLimit;
			float randomPosY = ((float) Math.random() * rangeY) + 0;

			entities.addEntity(new Coin(this, randomPosX, randomPosY));

		}

	}

	public EntityManager getEntities() {
		return entities;
	}

	public StageMap getMap() {
		return map;
	}

}
