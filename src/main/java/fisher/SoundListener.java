package fisher;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;

public class SoundListener {
    public static void onSoundPlay(SoundInstance sound) {
        if (passesCheck(sound)) {
            FileUtils.writeStringToFile("=== SOUND DEBUG ===");
            FileUtils.writeStringToFile("Sound ID: " + sound.getId().toString());
            FileUtils.writeStringToFile("Sound Pitch: " + sound.getPitch());
            FileUtils.writeStringToFile("Sound Volume: " + sound.getVolume());
            FileUtils.writeStringToFile("Sound Set weight " + soundSet.getWeight());
            FileUtils.writeStringToFile("==================");
            FishingController.handleSoundPlay(soundInstance);
        }

    }
}
