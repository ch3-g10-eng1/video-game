package com.team3._8.game;


/**
 * This class will be for all the static methods that influence the game
 * and are currently taking up space
 * We can rename later if we need to
 * @author Lenny
 */
public class gameController {


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

}
