package com.yourname.soundlistener;

import com.yourname.soundlistener.api.ISoundListener;
import com.yourname.soundlistener.api.SoundListenerManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.SoundInstance;

/**
 * Client-side mod initializer
 */
public class SoundListenerClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        System.out.println("Sound Listener Mod initialized!");
        
        // Example: Register a simple test listener
        registerExampleListener();
    }
    
    /**
     * Example method showing how to register a listener
     */
    private void registerExampleListener() {
        SoundListenerManager.getInstance().registerListener(new ISoundListener() {
            @Override
            public void onSoundPlay(SoundInstance soundInstance) {
                // Example: Log when sounds are played
                System.out.println("Sound played: " + soundInstance.getId());
            }
            
            @Override
            public void onSoundStop(SoundInstance soundInstance) {
                System.out.println("Sound stopped: " + soundInstance.getId());
            }
        });
    }
    
    /**
     * Public API method for other mods to register listeners
     * @param listener The sound listener to register
     */
    public static void registerSoundListener(ISoundListener listener) {
        SoundListenerManager.getInstance().registerListener(listener);
    }
    
    /**
     * Public API method for other mods to unregister listeners
     * @param listener The sound listener to unregister
     */
    public static void unregisterSoundListener(ISoundListener listener) {
        SoundListenerManager.getInstance().unregisterListener(listener);
    }
}
