package com.team3._8.game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;


public class MapGen {
	
	/**
	 * loads a map render and passes it out
	 * @return the map renderer that can then be used to render the map
	 */
	protected static TiledMapRenderer generateMaze() {
		TiledMap map = new TmxMapLoader().load("test_map.tmx");
		OrthogonalTiledMapRenderer map_render = new OrthogonalTiledMapRenderer(map);
		return map_render;
	}

	/**
	 * renders the map using map_render and sets it to camera
	 * @param camera that the view of the map is set to 
	 * @param map_render that is used to render the map
	 */
	protected static void renderMap(OrthographicCamera camera, TiledMapRenderer map_render) {
        map_render.setView(camera);
        map_render.render();
	}
}
