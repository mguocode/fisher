package fisher;

import fisher.fishing.SanityChecker;
import fisher.fishing.KeyPressListener;
import fisher.util.FileUtils;
import net.fabricmc.api.ClientModInitializer;
// import net.minecraft.client.sound.SoundInstance;

public class FisherMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FileUtils.initialize();
        KeyBindings.register();

        System.out.println("Sound Listener Mod initialized!");
    }

    public static void onTick() {
        SanityChecker.getInstance().onTick();
        KeyPressListener.onTick();
    }

}
