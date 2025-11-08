package com.team3._8.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Map;
import java.util.HashMap;

/**
 * This class will be for all the static methods that influence the game
 * and are currently taking up space
 * We can rename later if we need to
 * @author Lenny
 */
public class gameController {
    // Enables developer features (e.g. camera zooming)
    static boolean developerMode = true;

    /**
     * This method is used to format the time in minutes and seconds
     * for the timer, to display to the user.
     * @param currentTime  float: The current time
     * @return Formatted string for the timer
     */
    public static String formatTime(float currentTime){
        int mins = 0;
        int secs = 0;
        currentTime = (int)currentTime;

        mins = (int)currentTime / 60;
        secs = (int)currentTime % 60;

        return "Timer: " + mins + ":" + String.format("%02d", secs);
    }

    /**
     * This method handles the inputs that aren't related to the player input
     * this includes: camera zooming and pausing
     * @param camera Orthographic camera: The camera that can zoom in and out
     * @param paused boolean: The paused state of the game (true if paused)
     * @param zoom boolean: whether or not the function should allow zooming 
     * (true for zooming enabled)
     * @return the paused state of the game
     */
    public static boolean handleInput(OrthographicCamera camera, boolean paused, boolean zoom) {
        if (zoom) {
            //When Q is pressed, the camera is zoomed in, and zoomed out when E is pressed
            if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && developerMode) {
                camera.zoom += 0.02f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) && developerMode) {
                camera.zoom -= 0.02f;
            }
        }
        //This is to pause the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        return paused;
    }

    /**
     * sets the events map with intial values of 0
     * @return the events map of event type to number of events triggered 
     */
    public static Map<String, Integer> setEventMap() {
        Map<String, Integer> event_map = new HashMap<String, Integer>();
        event_map.put("Positive", 0);
        event_map.put("Negative", 0);
        event_map.put("Suprise", 0);
        return event_map;
    }

}
