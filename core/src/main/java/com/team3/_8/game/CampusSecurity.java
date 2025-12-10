package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the campus security geese characters
 *
 * @author Henry
 */
public class CampusSecurity extends InteractableEntity {
  // Return data map used to send data to calling instance
  final Map<String, Boolean> returnData = new HashMap<>();
  private Animation<TextureRegion> campusSecurity;
  // Variables used to configure sprite
  private float stateTime = 0f;
  private float x = 0;
  private float y = 0;
  private boolean initialisePos = true;
  private boolean up = false;

  /**
   * Instantiates campus security object
   *
   * @param sprite - sprite to draw
   * @param speed - movement speed
   */
  public CampusSecurity(Sprite sprite, float speed) {
    super(sprite, speed);
    // Loads animation textures
    loadTextures();
  }

  /** Configures interaction with entity */
  @Override
  public Map<String, Boolean> startInteraction() {
    // Sets command to MazeGame.java to reset
    // player position on collision with this sprite
    returnData.put("Reset Player Position", true);
    return returnData;
  }

  /** Configures end of interaction with entity */
  @Override
  public Map<String, Boolean> stopInteraction() {
    // Stops reset of player position, when interaction has ended
    returnData.put("Reset Player Position", false);
    return returnData;
  }

  /** Loads the animation textures from atlas into the animation variables */
  private void loadTextures() {
    // Loads sprites from Texture atlas
    // Used to hold animation textures
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/security_geese.atlas"));

    // Loads animation frames
    Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("walking");

    // Creates animation object
    this.campusSecurity = new Animation<TextureRegion>(0.1f, frames);
  }

  /**
   * Draws sprite and configures movement
   *
   * @param batch - Sprite to draw
   * @param x - x-coordinate to draw at
   * @param y - y-coordinate to draw at
   * @param movement_halter - A boolean array of size 4 which indicates which side of the wall is
   *     being hit 0-left, 1-top, 2-right, 3-bottom
   */
  public void draw(SpriteBatch batch, float x, float y, boolean[] movement_halter) {
    // Timer for animation
    stateTime += Gdx.graphics.getDeltaTime();

    // Sets animation frame
    TextureRegion current_animation = campusSecurity.getKeyFrame(stateTime, true);
    sprite.setRegion(current_animation);

    // Updates collision box and sprite position when moved
    if (this.sprite.getX() != this.x || this.sprite.getY() != this.y) {
      if (initialisePos) {
        sprite.setPosition(x, y);
        initialisePos = false;
      }
      updateCollisionBox();
      this.x = x;
      this.y = y;
    }

    // Configure the sprite to move up or down
    if (movement_halter[3]) {
      up = false;
    } else if (movement_halter[1]) {
      up = true;
    }
    // Runs movement
    move(up);
    sprite.draw(batch);
  }

  /**
   * Moves the campus security sprites up or down based in passed boolean value
   *
   * @param up - set to true to move up, false to move down
   */
  private void move(boolean up) {
    // Time used to calculate speed to move at
    float delta = Gdx.graphics.getDeltaTime();

    // Moves sprite up or down
    if (up) {
      sprite.translateY(this.speed * delta);
    } else {
      sprite.translateY(-this.speed * delta);
    }

    // Corrects collision box size
    float inset = 8f;
    float newWidth = Math.max(0f, this.sprite.getWidth() - inset * 2f);
    this.collisionBox.setX(this.sprite.getX() + inset);
    this.collisionBox.setY(this.sprite.getY());
    this.collisionBox.setWidth(newWidth);
    this.collisionBox.setHeight(this.sprite.getHeight());
  }
}
