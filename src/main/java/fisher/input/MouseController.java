
package fisher.input;

import fisher.FileUtils;
import fisher.mixin.MouseInvoker;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.util.concurrent.atomic.AtomicBoolean;

public class MouseController {

    private static MouseInvoker invoker;
    private static final AtomicBoolean reflectionInitialized = new AtomicBoolean(false);

    private static void initializeReflection() {
        MinecraftClient client = MinecraftClient.getInstance();
        invoker = (MouseInvoker) (Object) client.mouse;
        reflectionInitialized.set(true);
        FileUtils.writeStringToFile("[LuluTheFish] Successfully initialized reflection for onMouseButton");
    }

    private static void callOnMouseButton(long window, int button, int action, int mods) {
        if (!reflectionInitialized.get()) {
            initializeReflection();
        }
        invoker.invokeOnMouseButton(window, button, action, mods);
    }

    public static void toggleMouse(boolean click) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        int button = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
        int action = click ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        callOnMouseButton(window, button, action, 0);

    }

}