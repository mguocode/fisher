package fisher.input;

import fisher.mixin.MouseInvoker;

public class MouseController {

    private static MouseInvoker invoker;
    private static final AtomicBoolean reflectionInitialized = new AtomicBoolean(false);

    private void initializeReflection() {
        MinecraftClient client = MinecraftClient.getInstance();
        invoker = (MouseInvoker) (Object) client.mouse;
        reflectionInitialized.set(true);
        FileUtils.writeStringToFile("[LuluTheFish] Successfully initialized reflection for onMouseButton");
    }

    private void callOnMouseButton(long window, int button, int action, int mods) {
        if (!reflectionInitialized) {
            initializeReflection();
        }
        invoker.invokeOnMouseButton(window, button, action, mods);
    }

    public void toggleMouse(boolean click) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        int button = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
        int action = click ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        callOnMouseButton(window, button, action, 0);

    }

}