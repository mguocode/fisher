
package fisher.fishing;

import java.util.concurrent.atomic.AtomicBoolean;

public class State implements FishingIntf {
    private static final State INSTANCE = new State();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private State() {
    }

    public static State getInstance() {
        return INSTANCE;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public void onStartFishing() {
        isRunning.set(true);
    }

    @Override
    public void onStopFishing() {
        isRunning.set(false);
    }

}
