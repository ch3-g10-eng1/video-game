package com.team3._8.game.storage.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team3._8.game.storage.StorageService;

import java.io.IOException;

/**
 * Stores typed data in JSON format using LibGDX FileHandle
 *
 * @param <T> The type of object to serialise
 *
 * @author Sharjil
 */
public class JsonStorageService<T> implements StorageService<T> {

    private final FileHandle file;
    private final Gson gson;
    private final Class<T> type;

    public JsonStorageService(String filePath, Class<T> type) {
        this.file = Gdx.files.internal(filePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.type = type;
    }

    @Override
    public void save(T data) throws IOException {
        try {
            file.writeString(gson.toJson(data), false);
        } catch (Exception ex) {
            throw new IOException("Failed to save JSON file: " + file.path(), ex);
        }
    }

    @Override
    public T load() throws IOException {
        try {
            if (!file.exists()) {
                return gson.fromJson("{}", type);
            }
            return gson.fromJson(file.readString(), type);
        } catch (Exception ex) {
            throw new IOException("Failed to load JSON file: " + file.path(), ex);
        }
    }
}
