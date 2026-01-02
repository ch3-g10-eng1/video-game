package com.team3._8.game.sound.type;

/**
 * Enum defining all sound effects in the game
 * Each enum value maps to a specific sound file in the assets/sounds directory
 *
 * @author Sharjil
 */
public enum SoundType {

    PICKUP_COIN("pickup_coin.mp3", 0.7f, false);

    private final String filepath;
    private final float defaultVolume;
    private final boolean shouldLoop;

    /**
     * Constructor for the sound type
     *
     * @param filename Name of the file in the sounds directory
     * @param defaultVolume The default volume (0.0 to 1.0)
     * @param shouldLoop True if this sound should loop by default
     */
    SoundType(String filename, float defaultVolume, boolean shouldLoop) {
        this.filepath = "sounds/" + filename;
        this.defaultVolume = defaultVolume;
        this.shouldLoop = shouldLoop;
    }

    public String getFilepath() {
        return filepath;
    }

    public float getDefaultVolume() {
        return defaultVolume;
    }

    public boolean isShouldLoop() {
        return shouldLoop;
    }

    /**
     * Get just the filename without the full path
     */
    public String getFilename() {
        return filepath.substring(filepath.lastIndexOf("/") + 1);
    }
}
