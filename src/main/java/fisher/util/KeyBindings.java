package fisher.util;

import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final int startKeyBind = GLFW.GLFW_KEY_LEFT_BRACKET;
    private static final int endKeyBind = GLFW.GLFW_KEY_RIGHT_BRACKET;
    public static KeyBinding startKey;
    public static KeyBinding stopKey;

    public static void register() {
        startKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.luluthefish.start",
                startKeyBind,
                "category.luluthefish"));
        stopKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.luluthefish.stop",
                endKeyBind,
                "category.luluthefish"));
    }
}