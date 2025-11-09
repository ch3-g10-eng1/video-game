package com.team3._8.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 * Class defines a Maze object that renders background map
 *
 * @author Isaac M
 */
public class Maze {

  private final TiledMap map; // the map
  private final TiledMapRenderer map_render; // the rendere
  private final Set<MapObjects> collidable_objects; // Map object set of all collidable layers
  private final int[] visible_layers;
  // Index of every map layer, set to its own index if it should be visible.
  // Layer 0 must always be visible
  // Needs to be int[] because the MapRenderer.render method is shit
  private final MapObject win_layer;
  private final MapObject event_layer;
  private boolean event_triggered;

  /**
   * Maze constructor that defines a map, renderer, visible layers and its collision objects
   *
   * <p>Adds every collision layer to the list.
   *
   * @param filename that the map is stored under
   * @param visible_layer_names of the layers to be made visible
   * @param collision_layers name of the object layer where collision items are found
   */
  public Maze(
      String filename,
      String[] visible_layer_names,
      String[] collision_layers,
      String progress_layer,
      String event_trigger_layer) {
    map = new TmxMapLoader().load(filename);
    visible_layers = new int[map.getLayers().getCount()];
    map_render = new OrthogonalTiledMapRenderer(map);

    for (String layer : visible_layer_names) {
      this.addVisibleLayer(layer);
    }

    collidable_objects = new HashSet<MapObjects>();
    for (String layer : collision_layers) {
      collidable_objects.add((map.getLayers().get(layer)).getObjects());
    }
    win_layer = map.getLayers().get(progress_layer).getObjects().get(0);
    event_layer = map.getLayers().get(event_trigger_layer).getObjects().get(0);
    event_triggered = false;
  }

  /**
   * Maze constructor that defines a map, renderer and collision objects.
   *
   * <p>Makes every layer in the map visible
   *
   * @param filename that the map is stored under
   * @param collision_layers name of the object layer where collision items are found
   */
  public Maze(
      String filename,
      String[] collision_layers,
      String progress_layer,
      String event_trigger_layer) {
    this(filename, new String[0], collision_layers, progress_layer, event_trigger_layer);
    for (int index = 0; index < visible_layers.length; index++) {
      visible_layers[index] = index;
    }
  }

  /**
   * Renders the map and sets its view to camera, called every frame
   *
   * @param camera the view of the map is set to
   */
  protected void renderMap(OrthographicCamera camera) {
    map_render.setView(camera);
    map_render.render(visible_layers);
  }

  /**
   * Detects whether the entity is colliding with a wall of the current map, called whenever moved
   *
   * @param entity that is moving
   * @param delta since the last frame of movement
   * @return a boolean array of size 4 which indicates which side of the wall is being hit 0-left,
   *     1-top, 2-right, 3-bottom
   */
  public boolean[] hitsWall(CollidableEntity entity, float delta) {
    boolean[] movement_halter = new boolean[4];
    // Walls of the collision box, used to determine which direction entity is colliding in
    double right_wall_X;
    double left_wall_X;
    double wall_Y;
    Rectangle wall_collision = null; // collision box of the map
    // Distance bob moves every time function is called (+allowance for changes to delta)
    // System.out.println(delta);
    float effective_speed = entity.getSpeed() * delta * 3;

    for (MapObjects collidable_layer : collidable_objects) {
      for (RectangleMapObject wall : collidable_layer.getByType(RectangleMapObject.class)) {
        wall_collision = wall.getRectangle();

        // Define different points to measure where entity is in comparison
        right_wall_X = wall_collision.getX() + wall_collision.getWidth() - effective_speed;
        left_wall_X = wall_collision.getX() + effective_speed;
        wall_Y = wall_collision.getY() + effective_speed;

        if (Intersector.overlaps(wall_collision, entity.collisionBox)) {
          // If entity collides with box, which side of the box
          // Use a whiteboard to visualise
          if ((left_wall_X) > entity.collisionBox.getX() + entity.collisionBox.getWidth()) {
            movement_halter[0] = true;
          } else if (right_wall_X < entity.collisionBox.getX()) {
            movement_halter[2] = true;
          } else if ((wall_Y > entity.collisionBox.getY())) {
            movement_halter[3] = true;
          } else if ((wall_Y < entity.collisionBox.getY() + entity.collisionBox.getHeight())) {
            movement_halter[1] = true;
          }
        }
      }
    }
    return movement_halter;
  }

  /**
   * Method to add a new collision layer to the collidable objects linked list
   *
   * @param collision_layer name of the object layer to be made collidable
   */
  public void addCollisionLayer(String collision_layer) {
    collidable_objects.add((map.getLayers().get(collision_layer)).getObjects());
  }

  /**
   * Method to remove a collision layer from the collidable objects linked list, if it is present
   *
   * @param collision_layer name of the object layer to be made non-collidable
   * @return boolean to indicate success of removal, true if successful
   */
  public boolean removeCollisionLayer(String collision_layer) {
    return collidable_objects.remove((map.getLayers().get(collision_layer)).getObjects());
  }

  /**
   * Adds a new layer to be rendered every frame
   *
   * @param new_layer name of new layer to be rendered
   * @return boolean to indicate success of addition, true if successful
   */
  public boolean addVisibleLayer(String new_layer) {
    int layer_index = map.getLayers().getIndex(new_layer);
    if (layer_index == -1) {
      return false;
    } else {
      visible_layers[layer_index] = layer_index;
      return true;
    }
  }

  /**
   * Removes a layer so that it is no longer rendered
   *
   * @param layer name of layer to be removed
   * @return boolean to indicate success of removal, true if removed successfully
   */
  public boolean removeVisibleLayer(String layer) {
    int layer_index = map.getLayers().getIndex(layer);
    if (layer_index == -1) {
      return false;
    } else {
      visible_layers[layer_index] = 0;
      return true;
    }
  }

  /**
   * Determines if the win layer has been hit
   *
   * @param player entity that is being tracked
   * @return true if the win layer was hit
   */
  public boolean HitsWinLayer(CollidableEntity player) {
    Rectangle wall_collision = ((RectangleMapObject) win_layer).getRectangle();
    return Intersector.overlaps(wall_collision, player.collisionBox);
  }

  /**
   * Determines if the event layer has been hit
   *
   * @param player entity that is being tracked
   * @return true if the event layer was hit
   */
  public boolean HitsEventLayer(CollidableEntity player) {
    if (event_triggered) {
      return false;
    }
    Rectangle wall_collision = ((RectangleMapObject) event_layer).getRectangle();
    if (Intersector.overlaps(wall_collision, player.collisionBox)) {
      event_triggered = true;
      return true;
    }
    return false;
  }

  public void dispose() {
    map.dispose();
  }
}
