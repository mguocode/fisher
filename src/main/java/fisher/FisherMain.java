package fisher;

import com.yourname.soundlistener.api.ISoundListener;
import com.yourname.soundlistener.api.SoundListenerManager;

import mypackage.FileUtils;
import mypackage.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.SoundInstance;

public class FisherMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FileUtils.initialize();
        KeyBindings.register();

        System.out.println("Sound Listener Mod initialized!");

        // Example: Register a simple test listener
        registerExampleListener();
    }

}
