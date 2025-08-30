
package fisher;

import java.util.concurrent.atomic.AtomicBoolean;

public class FishingState {
    public static final AtomicBoolean isRunning = new AtomicBoolean(false);

    public static boolean isRunning() {
        return isRunning.get();
    }
}
