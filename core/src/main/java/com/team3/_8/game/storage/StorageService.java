package com.team3._8.game.storage;

import java.io.IOException;

/**
 * Represents a generic storage service capable of saving and loading typed data
 * Implementations may use any method for storage e.g JSON, MYSQL, MONGO
 *
 * @param <T> the type of data being stored
 *
 * @author Sharjil
 */
public interface StorageService<T> {

    /**
     * Saves the given object to the persistent storage
     *
     * @param data The data to be saved
     * @throws IOException if saving fails
     */
    void save (final T data) throws IOException;

    /**
     * Loads and returns stored data
     *
     * @return The laoded data, or a default instance if none exists
     * @throws IOException if loading fails
     */
    T load () throws IOException;
}
