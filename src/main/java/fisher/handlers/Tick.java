package fisher.handlers;

import fisher.fishing.SanityChecker;
import fisher.fishing.KeyPressListener;

public class Tick {

    public static void onTick() {
        SanityChecker.getInstance().onTick();
        KeyPressListener.onTick();
    }
}
