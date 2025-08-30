package fisher.fishing;

import fisher.util.FileUtils;
import fisher.util.KeyBindings;

public class KeyPressListener {
    public static void onTick() {
        // Handle toggle key - only process once per key press
        if (KeyBindings.startKey.wasPressed()) {
            FileUtils.writeStringToFile("start key pressed");
            if (!State.getInstance().isRunning()) {
                Manager.startFishing();
            }
        }

        // Handle stop key - only process once per key press
        if (KeyBindings.stopKey.wasPressed()) {
            FileUtils.writeStringToFile("stop key pressed");
            Manager.stopFishing();
            FileUtils.writeStringToFile("[LuluTheFish] Stop activated! (Stop key pressed)");
        }
    }
}