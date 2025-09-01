package fisher.input;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeySimulator {
    public static void pressNumberKey(int number, boolean press) {
        MinecraftClient client = MinecraftClient.getInstance();
        long window = client.getWindow().getHandle();

        int key = GLFW.GLFW_KEY_1 + (number - 1);
        int scancode = GLFW.glfwGetKeyScancode(key);
        int action = press ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        int mods = 0;

        client.keyboard.onKey(window, key, scancode, action, mods);
    }
}
