package com.gdracer.tiled;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.gdracer.entities.Player;
import com.gdracer.game.Game;

public class StageMap {
	
	private TiledMap map;
	private OrthographicCamera cam;
	private MapRenderer mr;
	
	//Game Stuff
	
	float scrollSpeed = 32 / 16;
	
	public static float TILES_X = 20, TILES_Y = 100;
	
	public static float TILED_DIMS = 32;
	
	public static float MAP_HEIGHT = TILES_Y * TILED_DIMS;
	
	float mapPosY = 0;
	
	Player player;
	
	public StageMap(String mapDir, OrthographicCamera cam) {
		this.cam = cam;
		map = new TmxMapLoader().load(mapDir);
		mr = new MapRenderer(map, cam);
	}
	
	public void update(){
		
		if(!player.isBlocked())
		scrollMap();
		
		cam.update();
	}
	
	public void render() {
		mr.setView(cam);
		mr.render();
	}
	
	private void scrollMap(){
		
		boolean stop = cam.position.y + Game.V_HEIGHT/2 + player.getSpeed() >= MAP_HEIGHT;
		float d2P = 32;
		
		if(!stop)
		cam.position.y = (player.getY() - d2P) + (Game.V_HEIGHT/2);
		
		//Reset
		else if(player.isFinished())
			cam.position.y = Player.START_POS_Y + (Game.V_HEIGHT/2);
		
	}
	
	public void accelerate(){
		scrollSpeed *= 2;
	}
	
	public void decelerate(){
		scrollSpeed /= 2;
	}

	public void addLayer(MapLayer layer){
		 map.getLayers().add(layer);
	}
	
	public void toggleLayer(String layer, boolean visible){
		map.getLayers().get(layer).setVisible(visible);
	}
	
	public void toggleObject(String objLayer, String objName, boolean visible){
		map.getLayers().get(objLayer).getObjects().get(objName).setVisible(visible);
	}
	
	public void addObject(String objLayer, MapObject obj){
		map.getLayers().get(objLayer).getObjects().add(obj);
	}
	
	public void removeObject(String objLayer, MapObject obj){
		map.getLayers().get(objLayer).getObjects().remove(obj);
	}
	
	public MapObjects getObjects(String objLayer){
		return map.getLayers().get(objLayer).getObjects();
	}
	
	public void setPlayer(Player p){
		player = p;
	}

}
