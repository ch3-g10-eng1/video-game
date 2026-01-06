package com.team3._8.game.sound.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.team3._8.game.sound.type.SoundType;

/**
 * A centralised sound management system for the game
 *
 *  Features lazy loading, so sounds are only loaded when first played. Sounds are also kept in memory
 *  for quick replay. Also features volume control as well as mute functionality.
 *  Tracks sound instances which helps to stop/pause specific sounds
 *
 *  Quick Usage Guide:
 *  SoundManager.getInstance().play(SoundType.PICKUP_COIN);
 *  OR
 *  SoundManager.getInstance().playWithVolume(SoundType.PICKUP_COIN, 0.5f);
 *
 * @author Sharjil
 */
public class SoundManager implements Disposable {

    private static SoundManager instance;

    //Cache for loaded sounds
    private final Map<SoundType, Sound> soundCache;

    //Track playing sounds for instances for stopping/management
    private final Map<SoundType, List<Long>> playingInstances;

    //Settings
    private float masterVolume = 1.0f;
    private boolean isMuted = false;
    private boolean soundsEnabled = true;

    private int soundsLoaded = 0;
    private int soundsPlayed = 0;

    /**
     * Private constructor for singleton pattern
     */
    private SoundManager() {
        soundCache = new HashMap<>();
        playingInstances = new HashMap<>();

        loadSettings();
    }

    /**
     * Get the singleton instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }

        return instance;
    }

    /**
     * Play a sound with default settings
     *
     * @param type SoundType to be played
     * @return The sound instance ID, or -1 if the sound couldnt be played
     */
    public long play(SoundType type) {
        if (type == null) {
            Gdx.app.error("SoundManager", "SoundType is null");
            return -1;
        }

        return playWithVolume(type, type.getDefaultVolume());
    }

    /**
     * Play a sound with a custom volume
     *
     * @param type SoundType to be played
     * @param volume The volume (0.0 to 1.0)
     * @return The sound instance ID, or -1 if the sound couldnt be played
     */
    public long playWithVolume(SoundType type, float volume) {
        if(!canPlaySound()) {
            return -1;
        }

        Sound sound = getOrLoadSound(type);
        if (sound == null) {
            return -1;
        }

        float finalVolume = volume * masterVolume;
        long instanceId;

        if (type.isShouldLoop()) {
            instanceId = sound.loop(finalVolume);
        } else {
            instanceId = sound.play(finalVolume);
        }

        trackInstance(type, instanceId);

        soundsPlayed++;

        return instanceId;
    }

    /**
     * Play a sound with custom volume, pitch and pan
     *
     * @param type SoundType to be played
     * @param volume The volume (0.0 to 1.0)
     * @param pitch The pitch (0.5 to 2.0, where 1.0 is normal)
     * @param pan The pan (-1.0 to 1.0, where 0 is center)
     * @return The sound instance ID, or -1 if the sound couldnt be played
     */
    public long playWithEffects(SoundType type, float volume, float pitch, float pan) {
        if(!canPlaySound()) {
            return -1;
        }

        Sound sound = getOrLoadSound(type);
        if (sound == null) {
            return -1;
        }

        float finalVolume = volume * masterVolume;
        long instanceId = sound.play(finalVolume, pitch, pan);

        trackInstance(type, instanceId);

        soundsPlayed++;
        return instanceId;
    }

    /**
     * Stop a specific sound instance
     *
     * @param type SoundType to stop
     * @param instanceId The instance to stop
     */
    public void stopInstance(SoundType type, long instanceId) {
        Sound sound = soundCache.get(type);
        if (sound != null) {
            sound.stop(instanceId);
            removeInstance(type, instanceId);
        }
    }

    /**
     * Stop all instances of a sound type
     *
     * @param type SoundType to stop
     */
    public void stopAll(SoundType type) {
        Sound sound = soundCache.get(type);

        if (sound != null) {
            sound.stop();
            playingInstances.remove(type);
        }
    }

    /**
     * Stop all currently playing sounds
     */
    public void stopAllSounds() {
        for (Sound sound : soundCache.values()) {
            sound.stop();
        }
        playingInstances.clear();
    }

    /**
     * Pause all currently playing sounds
     */
    public void pauseAllSounds() {
        for (Sound sound : soundCache.values()) {
            sound.pause();
        }
    }

    /**
     * Resume all paused sounds
     */
    public void resumeAllSounds() {
        for (Sound sound : soundCache.values()) {
            sound.resume();
        }
    }

    /**
     * Set the master volume for all sounds
     *
     * @param volume The volume (0.0 to 1.0)
     */
    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0f, Math.min(1f, volume));
        saveSettings();
    }

    /**
     * Get the current master volume
     */
    public float getMasterVolume() {
        return masterVolume;
    }

    /**
     * Mute/unmute all sounds
     * @param muted True to mute, False to unmute
     */
    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (muted) {
            pauseAllSounds();
        }  else {
            resumeAllSounds();
        }

        saveSettings();
    }

    /**
     * Check if sounds are muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Enable or disable sound effects entirely
     *
     * @param enabled True to enable, False to disable
     */
    public void setSoundsEnabled(boolean enabled) {
        this.soundsEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
        saveSettings();
    }

    /**
     * Check if sounds are enabled
     */
    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }

    /**
     * Preload specific sounds for better performance
     *
     * @param types SoundType's to preload
     */
    public void preloadSounds(SoundType... types) {
        for (SoundType type : types) {
            getOrLoadSound(type);
        }
    }

    /**
     * Preload all sounds
     * NOTE: This should not be used often, will lead to a high memory usage
     */
    public void preloadAllSounds() {
        for (SoundType type : SoundType.values()) {
            getOrLoadSound(type);
        }
    }

    /**
     * Clear the sound cache to free memory
     * NOTE: This will cause sounds to be reloaded the next time they are played
     */
    public void clearCache() {
        stopAllSounds();

        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }

        soundCache.clear();
        playingInstances.clear();
        soundsLoaded = 0;
    }

    @Override
    public void dispose() {
        clearCache();
        instance = null;
    }

    private boolean canPlaySound() {
        return soundsEnabled && !isMuted;
    }

    private Sound getOrLoadSound(SoundType type) {
        if (soundCache.containsKey(type)) {
            return soundCache.get(type);
        }

        try {
            FileHandle file = Gdx.files.internal(type.getFilepath());
            if (!file.exists()) {
                Gdx.app.error("SoundManager", "Sound file not found: " + type.getFilepath() + " .Please add this file to the assets/sounds folder!");
                return null;
            }

            Sound sound = Gdx.audio.newSound(file);
            soundCache.put(type, sound);
            soundsLoaded++;

            return sound;
        } catch (Exception e) {
            Gdx.app.error("SoundManager", "Failed to load sound: " + type.getFilepath(),  e);
            return null;
        }
    }

    private void trackInstance(SoundType type, long instanceId) {
        if (!playingInstances.containsKey(type)) {
            playingInstances.put(type, new ArrayList<>());
        }

        playingInstances.get(type).add(instanceId);
    }

    private void removeInstance(SoundType type, long instanceId) {
        List<Long> instances = playingInstances.get(type);

        if (instances != null) {
            instances.remove(instanceId);
            if (instances.isEmpty()) {
                playingInstances.remove(type);
            }
        }
    }

    private void loadSettings() {
        //Load from LibGDX preferences
        Preferences prefs = Gdx.app.getPreferences("GameSettings");
        masterVolume = prefs.getFloat("masterVolume", masterVolume);
        isMuted = prefs.getBoolean("soundsMuted", isMuted);
        soundsEnabled = prefs.getBoolean("soundsEnabled", soundsEnabled);
    }

    private void saveSettings() {
        //Save to LibGDX preferences
        Preferences prefs = Gdx.app.getPreferences("GameSettings");
        prefs.putFloat("masterVolume", masterVolume);
        prefs.putBoolean("soundsMuted", isMuted);
        prefs.putBoolean("soundsEnabled", soundsEnabled);
        prefs.flush();
    }

    // Used for testing purposes only
    public static void setInstance(SoundManager soundManager) {
        instance = soundManager;
    }

}

