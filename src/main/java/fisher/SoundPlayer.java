package fisher;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

public class SoundPlayer {
    public final SoundEvent startSound = SoundEvents.BLOCK_ANVIL_FALL;
    public final SoundEvent stopSound = SoundEvents.ENTITY_WITHER_SPAWN;
    public final SoundEvent sanitySound = SoundEvents.ENTITY_ENDER_DRAGON_DEATH;
    public final SoundEvent interestingSound = SoundEvents.ENTITY_WITHER_SPAWN;

    public static void play(SoundEvent sound) {
        play(sound, 1.0F, 1.5F);
    }

    public static void play(SoundEvent sound, float volume, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.getSoundManager().play(
                    PositionedSoundInstance.master(sound, volume, pitch));
        }
    }
}
