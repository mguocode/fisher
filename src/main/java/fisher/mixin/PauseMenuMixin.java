package mypackage.mixin;

import mypackage.FileUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class PauseMenuMixin {

    @Inject(method = "openPauseMenu", at = @At("HEAD"))
    private void logPauseMenuTrigger(boolean pauseOnly, CallbackInfo ci) {
        // Log the stack trace to see what called this
        FileUtils.writeStringToFile("[LuluTheFish] PAUSE MENU TRIGGERED - pauseOnly: " + pauseOnly);

        // Get stack trace to see what called it
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(stack.length, 5); i++) {
            FileUtils.writeStringToFile("  " + i + ": " + stack[i].getClassName() + "." + stack[i].getMethodName());
        }
    }
}