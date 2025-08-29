package com.yourname.soundlistener.api;

import net.minecraft.client.sound.SoundInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central manager for handling sound listeners
 */
public class SoundListenerManager {
    private static SoundListenerManager INSTANCE;
    private final List<ISoundListener> listeners = new CopyOnWriteArrayList<>();
    
    private SoundListenerManager() {}
    
    public static SoundListenerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundListenerManager();
        }
        return INSTANCE;
    }
    
    /**
     * Register a sound listener
     * @param listener The listener to register
     */
    public void registerListener(ISoundListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Unregister a sound listener
     * @param listener The listener to unregister
     */
    public void unregisterListener(ISoundListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Clear all listeners
     */
    public void clearListeners() {
        listeners.clear();
    }
    
    // Internal methods called by mixins
    public void notifyPlay(SoundInstance soundInstance) {
        for (ISoundListener listener : listeners) {
            try {
                listener.onSoundPlay(soundInstance);
            } catch (Exception e) {
                // Log error but don't crash
                System.err.println("Error in sound listener: " + e.getMessage());
            }
        }
    }
    
    public void notifyStop(SoundInstance soundInstance) {
        for (ISoundListener listener : listeners) {
            try {
                listener.onSoundStop(soundInstance);
            } catch (Exception e) {
                System.err.println("Error in sound listener: " + e.getMessage());
            }
        }
    }
    
    public void notifyFinish(SoundInstance soundInstance) {
        for (ISoundListener listener : listeners) {
            try {
                listener.onSoundFinish(soundInstance);
            } catch (Exception e) {
                System.err.println("Error in sound listener: " + e.getMessage());
            }
        }
    }
    
    public void notifyManagerClear() {
        for (ISoundListener listener : listeners) {
            try {
                listener.onSoundManagerClear();
            } catch (Exception e) {
                System.err.println("Error in sound listener: " + e.getMessage());
            }
        }
    }
}
