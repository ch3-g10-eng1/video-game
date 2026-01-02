package com.team3._8.game.storage.manager;

import com.team3._8.game.storage.StorageService;
import com.team3._8.game.storage.data.LeaderboardData;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles storing and retrieving maze completion times
 * Abstracts the underlying storage implementations (JSON, SQL)
 *
 * @author Sharjil
 */
public class TimeStorageManager {

    private final StorageService<LeaderboardData> storageService;

    public TimeStorageManager(StorageService<LeaderboardData> storageService) {
        this.storageService = storageService;
    }


    /**
     * Loads the stored times
     *
     */
    public LeaderboardData load() {
        try {
            LeaderboardData data = storageService.load();
            if (data.getEntries() == null) data.setEntries(new ArrayList<>());
            return data;
        } catch (IOException e) {
            return new LeaderboardData();
        }
    }

    /**
     * Loads the stored times, adds the new times, saves the updated list
     *
     * @param name name related to the completion time
     * @param time time in seconds
     */
    public void saveEntry(String name, float time) {
        try {
            LeaderboardData data = load();
            data.addEntry(name, time);
            storageService.save(data);
        } catch (IOException ignored) {}

    }

}
