package com.yourname.soundlistener.api;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;

/**
 * Interface for listening to sound events in the client's sound manager
 */
public interface ISoundListener {
    
    /**
     * Called when a sound is about to be played
     * @param soundInstance The sound instance that will be played
     */
    default void onSoundPlay(SoundInstance soundInstance) {}
    
    /**
     * Called when a sound is stopped



     package com.yourname.soundlistener.api;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;

/**
 * Interface for listening to sound events in the client's sound manager
 */
public interface ISoundListener {

    /**
     * Called when a sound is about to be played
     * @param soundInstance The sound instance that will be played
     */
    default void onSoundPlay(SoundInstance soundInstance) {}

    /**
     * Called when a sound is stopped
     * @param soundInstance The sound instance that was stopped
     */
    default void onSoundStop(SoundInstance soundInstance) {}

    /**
     * Called when a sound finishes playing naturally
     * @param soundInstance The sound instance that finished
     */
    default void onSoundFinish(SoundInstance soundInstance) {}

    /**
     * Called when the sound manager is cleared (e.g., when changing dimensions)
     */
    default void onSoundManagerClear() {}
}

