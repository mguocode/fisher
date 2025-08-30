package fisher.handlers;

import fisher.fishing.FishingController;
import fisher.util.FileUtils;
import net.minecraft.client.sound.SoundInstance;

public class Sound {
    public static void onSoundPlay(SoundInstance sound) {
        if (passesCheck(sound)) {
            FileUtils.writeStringToFile("=== SOUND DEBUG ===");
            FileUtils.writeStringToFile("Sound ID: " + sound.getId().toString());
            FileUtils.writeStringToFile("Sound Pitch: " + sound.getPitch());
            FileUtils.writeStringToFile("Sound Volume: " + sound.getVolume());
            FileUtils.writeStringToFile("==================");
            FishingController.onDetect();
        }

    }

    private static boolean passesCheck(SoundInstance sound) {
        return sound.getId().toString().contains("note_block.pling");
    }
}
