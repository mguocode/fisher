package fisher.mixin;

import fisher.SoundListener;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"))
    private void onPlay(SoundInstance soundInstance, CallbackInfo ci) {
        SoundListener.onSoundPlay(soundInstance);
    }

    // @Inject(method = "stop(Lnet/minecraft/client/sound/SoundInstance;)V", at =
    // @At("HEAD"))
    // private void onStop(SoundInstance soundInstance, CallbackInfo ci) {
    // SoundListenerManager.getInstance().notifyStop(soundInstance);
    // }

    // @Inject(method = "stopAll()V", at = @At("HEAD"))
    // private void onStopAll(CallbackInfo ci) {
    // SoundListenerManager.getInstance().notifyManagerClear();
    // }
}
