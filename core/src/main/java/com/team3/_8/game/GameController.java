package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be for all the static methods that influence the game and are currently taking up
 * space We can rename later if we need to
 *
 * @author Lenny
 */
public class GameController {
  // Enables developer features (e.g. camera zooming)
  static final boolean developerMode = true;

  /**
   * Formats the time in minutes and seconds
   *
   * @param currentTime float: The current time
   * @return Formatted string for the timer
   */
  public static String formatTime(float currentTime) {
    currentTime = (int) currentTime;

    int mins = (int) currentTime / 60;
    int secs = (int) currentTime % 60;

    return "Timer: " + mins + ":" + String.format("%02d", secs);
  }

  /**
   * Handles non-player input: camera zooming and pausing
   *
   * @param camera Orthographic camera: The camera that can zoom in and out
   * @param paused boolean: The paused state of the game (true if paused)
   * @param zoom boolean: whether the function should allow zooming (true for zooming enabled)
   * @return the paused state of the game
   */
  public static boolean handleInput(OrthographicCamera camera, boolean paused, boolean zoom) {
    if (zoom) {
      // These magic numbers weren't our doing, promise!
      // Q :: Zoom in
      if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && developerMode) {
        camera.zoom += 0.02f;
      }
      // E :: Zoom out
      if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) && developerMode) {
        camera.zoom -= 0.02f;
      }
    }
    // Pause
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      paused = !paused;
    }

    return paused;
  }

  /**
   * Sets the events map with initial values of 0
   *
   * @return the events map of event type to number of events triggered
   */
  public static Map<String, Integer> setEventMap() {
    Map<String, Integer> event_map = new HashMap<String, Integer>();
    event_map.put("Positive", 0);
    event_map.put("Negative", 0);
    event_map.put("Hidden", 0);
    return event_map;
  }
}
