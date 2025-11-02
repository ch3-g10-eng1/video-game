package com.team3._8.game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;

import java.util.LinkedList;


/**
 * Class defines a Map object that renders background map
 * @author Isaac M
 */
public class Map {
	
	private TiledMap map;
	private TiledMapRenderer map_render;
	private LinkedList<MapObjects> collidable_objects;

    /**
     * constructor for map that defines a map, its renderer and its collision objects 
     * @param filename that the map is stored under
     * @param collision_layer name of the object layer where collision items are found
     */
	public Map(String filename, String[] collision_layers) {
		map = new TmxMapLoader().load(filename);
		map_render = new OrthogonalTiledMapRenderer(map);
		collidable_objects = new LinkedList<MapObjects>();
		for (String layer : collision_layers) {
			collidable_objects.add((map.getLayers().get(layer)).getObjects());
		}
	}

	/**
	 * renders the map and sets it's view to camera, called every frame
	 * @param camera that the view of the map is set to 
	 */
	protected void renderMap(OrthographicCamera camera) {
        map_render.setView(camera);
        map_render.render();
	}

    /**
     * detects whether the entity is colliding with a wall of the current map, 
     * called whenether moved
     * @param entity that is moving
     * @param delta since the last frame of movement
     * @return a boolean array of size 4 which indicates which side of the wall is being hit 
     *      0-left, 1-top, 2-right, 3-bottom
     */
	public boolean[] hitsWall(CollidableEntity entity, float delta) {
		boolean[] movement_halter = new boolean[4];
		double right_wall_X;
		double left_wall_X;
		double wall_Y;
		Rectangle wall_collision = null;
        float effective_speed = entity.getSpeed() * delta *2;
		for (MapObjects collidable_layer : collidable_objects) {
			for (RectangleMapObject wall : collidable_layer.getByType(RectangleMapObject.class)) {
				wall_collision = wall.getRectangle();

				//define different points to measure where entity is in comparison
				right_wall_X = wall_collision.getX() + wall_collision.getWidth() - effective_speed;
				left_wall_X = wall_collision.getX() + effective_speed;
				wall_Y = wall_collision.getY() + effective_speed;

				if (Intersector.overlaps(wall_collision, entity.collisionBox)) {
					if ((left_wall_X) > entity.collisionBox.getX()+entity.collisionBox.getWidth()) {
						movement_halter[0] = true;
					}				
					else if (right_wall_X < entity.collisionBox.getX()) {
						movement_halter[2] = true;
					}				
					else if ((wall_Y > entity.collisionBox.getY())) {
						movement_halter[3] = true;
					}
					else if ((wall_Y < entity.collisionBox.getY()+entity.collisionBox.getHeight())) {
						movement_halter[1] = true;
					}

				}
			}
		}
		return movement_halter;
	}

	public void addCollisionLayer(String collision_layer) {
		collidable_objects.add((map.getLayers().get(collision_layer)).getObjects());
	}

	public boolean removeCollisionLayer(String collision_layer) {
		return collidable_objects.remove((map.getLayers().get(collision_layer)).getObjects());	
	} 
}
