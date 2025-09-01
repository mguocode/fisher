
package fisher.fishing;

import java.util.concurrent.atomic.AtomicBoolean;

public class State implements FishingIntf {
    private static final long MIN_TIME_BETWEEN_TOGGLES = 500;
    private static final State INSTANCE = new State();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean inAbilityMode = new AtomicBoolean();
    private volatile long lastToggleTime = System.currentTimeMillis();

    private State() {
    }

    public static State getInstance() {
        return INSTANCE;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public boolean inAbilityMode() {
        return inAbilityMode.get();
    }

    @Override
    public void onStartFishing() {
        isRunning.set(true);
    }

    @Override
    public void onStopFishing() {
        isRunning.set(false);
    }

    public void handleFishingModeToggleKeyPress() {
        long now = System.currentTimeMillis();
        if (now - lastToggleTime > MIN_TIME_BETWEEN_TOGGLES) {
            inAbilityMode.set(!inAbilityMode.get());
            lastToggleTime = now;
        }
    }

}
