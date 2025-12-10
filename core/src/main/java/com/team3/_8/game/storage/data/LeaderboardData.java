package com.team3._8.game.storage.data;

import com.team3._8.game.storage.model.LeaderboardEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardData {

    private List<LeaderboardEntry> entries = new ArrayList<>();

    public List<LeaderboardEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LeaderboardEntry> entries) {
        this.entries = entries;
    }


    /**
     * Adds a new entry, sorts by time ascending, and keeps the best 5.
     */
    public void addEntry(String name, float time) {
        entries.add(new LeaderboardEntry(name, time));
        entries.sort(Comparator.comparingDouble(LeaderboardEntry::getTime));

        if (entries.size() > 5) {
            entries = entries.subList(0, 5);
        }
    }

    public boolean qualifies(float time) {
        if (entries.size() < 5) return true;
        return time < entries.get(entries.size() - 1).getTime();
    }
}
