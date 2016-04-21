package com.gdracer.tiled;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.gdracer.entities.Entity;
import com.gdracer.game.Game;

public class MapRenderer extends OrthogonalTiledMapRenderer {
	
	OrthographicCamera cam;
	
	public MapRenderer(TiledMap map, OrthographicCamera cam) {
        super(map);
        this.cam = cam;
    }
 
    @Override
    public void renderObject(MapObject object) {
    	
    	if(object.isVisible() && isOnScreen(object)){
    		if(object instanceof Entity)
    			((Entity)object).render((SpriteBatch)batch);
    	}
      
    }
    
    private boolean isOnScreen(MapObject mo){
    	
    	Rectangle screenRect = new Rectangle(0, 0, Game.V_WIDTH, Game.V_HEIGHT);
    	
    	Rectangle objRect = null;
    	Vector3 pc = null;
    	
    	
    	if(mo instanceof RectangleMapObject){
    		objRect = ((RectangleMapObject)mo).getRectangle();
    		pc = cam.project(new Vector3(objRect.x, objRect.y, 0));
    		
    		objRect.setPosition(pc.x, pc.y);
    	}
    		
    	
    	else if(mo instanceof Entity){
    		Entity e = (Entity)mo;
    		pc = cam.project(new Vector3(e.getX(), e.getY(), 0));
    		objRect = new Rectangle(pc.x, pc.y, e.getW(), e.getH());
    	}
    	    	
    	return screenRect.contains(objRect);
    }

}
