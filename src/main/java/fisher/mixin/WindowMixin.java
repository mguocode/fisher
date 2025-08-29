package mypackage.mixin;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    private void preventFocusLoss(long window, boolean focused, CallbackInfo ci) {
        // Cancel focus loss events - only allow focus gain
        if (!focused) {
            ci.cancel(); // This prevents the focus loss from being processed
        }
    }
}