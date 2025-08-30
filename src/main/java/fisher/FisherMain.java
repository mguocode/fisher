package fisher;

// import fisher.FileUtils;
// import fisher.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.SoundInstance;

public class FisherMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FileUtils.initialize();
        KeyBindings.register();

        System.out.println("Sound Listener Mod initialized!");
    }

}
