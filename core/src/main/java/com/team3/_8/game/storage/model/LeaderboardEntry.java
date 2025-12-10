package com.team3._8.game.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that stores recent or best completion times
 *
 * @author Sharjil
 */
public class LeaderboardEntry {

    private String name;
    private float time;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String name, float time) {
        this.name = name;
        this.time = time;
    }

    public String getName() { return name; }
    public float getTime() { return time; }

    public void setName(String name) { this.name = name; }
    public void setTime(float time) { this.time = time; }
}
