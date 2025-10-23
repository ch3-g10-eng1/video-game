package com.team3._8.game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.lang.reflect.Array;

import org.w3c.dom.css.Rect;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;



public class Map {
	
	private TiledMap map;
	private TiledMapRenderer map_render;
	private MapObjects walls;

	/**
	 * loads a map render and passes it out
	 * @return the map renderer that can then be used to render the map
	 */
	public Map(String filename, String collision_layer) {
		map = new TmxMapLoader().load(filename);
		map_render = new OrthogonalTiledMapRenderer(map);
		walls = (map.getLayers().get(collision_layer)).getObjects();
	}

	/**
	 * renders the map using map_render and sets it to camera
	 * @param camera that the view of the map is set to 
	 * @param map_render that is used to render the map
	 */
	protected void renderMap(OrthographicCamera camera) {
        map_render.setView(camera);
        map_render.render();
	}

	public boolean[] hits_wall(Sprite entity, float speed, float delta) {
		Rectangle player_collision_box = new Rectangle(entity.getX(), entity.getY(), entity.getHeight(), entity.getWidth());
		//Make entity have collisison box 
		boolean[] movement_halter = new boolean[4];
		double right_wall_X;
		double left_wall_X;
		double wall_Y;
		Rectangle wall_collision = null;
		float effective_speed = speed*delta;
		//make entity have it's effective speed


		for (RectangleMapObject wall : walls.getByType(RectangleMapObject.class)) {
			wall_collision = wall.getRectangle();
			right_wall_X = wall_collision.getX() + wall_collision.getWidth() - effective_speed;
			//use entity speed to work out stopping point (hopefully)
			left_wall_X = wall_collision.getX() + effective_speed;
			wall_Y = wall_collision.getY() + effective_speed;
			if (Intersector.overlaps(wall_collision, player_collision_box)) {
				if ((left_wall_X) > player_collision_box.getX()+player_collision_box.getWidth()) {
					movement_halter[0] = true;
				}				
				else if (right_wall_X < player_collision_box.getX()) {
					movement_halter[2] = true;
				}				
				else if ((wall_Y > player_collision_box.getY())) {
					movement_halter[3] = true;
				}
				else if ((wall_Y < player_collision_box.getY()+player_collision_box.getHeight())) {
					movement_halter[1] = true;
				}

			}
		}
		return movement_halter;
	}
}
