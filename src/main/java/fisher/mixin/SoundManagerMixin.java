package fisher.mixin;

import fisher.handlers.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        System.out.println("[DEBUG] SoundManager initialized - mixin is working!");

        // Let's see what methods are available
        SoundManager manager = (SoundManager) (Object) this;
        System.out.println("[DEBUG] SoundManager class: " + manager.getClass().getName());

        // Print available methods
        java.lang.reflect.Method[] methods = manager.getClass().getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            if (method.getName().contains("play")) {
                System.out.println("[DEBUG] Found play method: " + method.getName() + " - " + method.toString());
            }
        }
    }

    // Inject into the play method that returns PlayResult
    // Injecting at retunr because some fields are not initialized
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;", at = @At("RETURN"))
    private void onPlay(SoundInstance soundInstance, CallbackInfoReturnable<Object> cir) {
        Sound.onSoundPlay(soundInstance);
    }
}