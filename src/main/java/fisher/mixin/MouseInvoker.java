package mypackage.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseInvoker {
    // @Invoker("method_1611") // This is the obfuscated name for onMouseButton
    // @Invoker("method_1601") // Correct intermediary name from your lookup
    @Invoker("method_22686") // Correct intermediary name from your lookup
    void invokeOnMouseButton(long window, int button, int action, int mods);
}